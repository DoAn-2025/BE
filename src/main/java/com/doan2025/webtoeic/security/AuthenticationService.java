package com.doan2025.webtoeic.security;

import com.doan2025.webtoeic.constants.enums.ERole;
import com.doan2025.webtoeic.constants.enums.ResponseCode;
import com.doan2025.webtoeic.constants.enums.ResponseObject;
import com.doan2025.webtoeic.domain.*;
import com.doan2025.webtoeic.dto.request.AuthenticationRequest;
import com.doan2025.webtoeic.dto.request.IntrospectRequest;
import com.doan2025.webtoeic.dto.request.RegisterRequest;
import com.doan2025.webtoeic.dto.response.AuthenticationResponse;
import com.doan2025.webtoeic.dto.response.IntrospectResponse;
import com.doan2025.webtoeic.exception.WebToeicException;
import com.doan2025.webtoeic.repository.*;
import com.doan2025.webtoeic.utils.JwtUtil;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    private final UserRepository userRepository;
    private final InvalidatedTokenRepository invalidatedTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final ManagerRepository managerRepository;
    private final ConsultantRepository consultantRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final JwtUtil jwtUtil;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;

    @NonFinal
    @Value("${jwt.issuer}")
    protected String ISSUER;

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();
        boolean isValid = true;

        try {
            verifyToken(token, false);
        } catch (WebToeicException e) {
            isValid = false;
        }

        return IntrospectResponse.builder().valid(isValid).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userRepository
                .findByEmail((request.getEmail()).trim())
                .orElseThrow(() -> new WebToeicException(ResponseCode.NOT_EXISTED, ResponseObject.EMAIL));

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticated) throw new WebToeicException(ResponseCode.INVALID_PASSWORD, ResponseObject.PASSWORD);

        var token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .role(user.getRole().getValue())
                .build();
    }

    public void logout(HttpServletRequest request) throws ParseException, JOSEException {
        try {
            var signToken = verifyToken(jwtUtil.getJwtFromRequest(request), true);

            String jit = signToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

            InvalidatedToken invalidatedToken =
                    InvalidatedToken.builder().token(jit).expiryTime(expiryTime).build();

            invalidatedTokenRepository.save(invalidatedToken);
        } catch (WebToeicException exception) {
            log.info("Token already expired");
        }
    }

    public AuthenticationResponse refreshToken(HttpServletRequest request) throws ParseException, JOSEException {
        // Parse và kiểm tra chữ ký
        var signedJWT = verifyToken(jwtUtil.getJwtFromRequest(request), true);

        var jit = signedJWT.getJWTClaimsSet().getJWTID();
        var expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken =
                InvalidatedToken.builder().token(jit).expiryTime(expiryTime).build();

        invalidatedTokenRepository.save(invalidatedToken);

        var email = signedJWT.getJWTClaimsSet().getSubject();

        var user = userRepository.findByEmail(email)
                        .orElseThrow(() -> new WebToeicException(ResponseCode.UNAUTHENTICATED, ResponseObject.EMAIL));

        var token = generateToken(user);

        return AuthenticationResponse.builder().token(token).authenticated(true).role(user.getRole().getValue()).build();
    }

    public void register(RegisterRequest request) {
        boolean emailExists = userRepository.existsByEmail(request.getEmail());
        if(emailExists) throw new WebToeicException(ResponseCode.EXISTED, ResponseObject.EMAIL);
        ERole role;
        try {
            if(request.getRole() == null){
                role = ERole.STUDENT;
            }else{
                role = ERole.fromValue(request.getRole());
            }
        } catch (IllegalArgumentException e) {
            throw new WebToeicException(ResponseCode.INVALID, ResponseObject.ROLE);
        }
        User user = new User(request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getFirstName(),
                request.getLastName(),
                role);
        User savedUser = userRepository.save(user);
        if(savedUser.getRole().equals(ERole.MANAGER)){
            Manager manager = new Manager();
            manager.setUser(savedUser);
            savedUser.setManager(manager);
            managerRepository.save(manager);
        } else if(savedUser.getRole().equals(ERole.CONSULTANT)){
            Consultant consultant = new Consultant();
            consultant.setUser(savedUser);
            savedUser.setConsultant(consultant);
            consultantRepository.save(consultant);
        } else if(savedUser.getRole().equals(ERole.TEACHER)){
            Teacher teacher = new Teacher();
            teacher.setUser(savedUser);
            savedUser.setTeacher(teacher);
            teacherRepository.save(teacher);
        } else if(savedUser.getRole().equals(ERole.STUDENT)){
            Student student = new Student();
            student.setUser(savedUser);
            savedUser.setStudent(student);
            studentRepository.save(student);
        }
    }

    private String generateToken(User user) {
        SecretKey key = Keys.hmacShaKeyFor(SIGNER_KEY.getBytes());
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuer(ISSUER)
                .setIssuedAt(new Date())
                .setExpiration(new Date(Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .setId(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {

        //  Parse và kiểm tra chữ ký
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);
        //  Kiểm tra thời gian hết hạn
        Date expiryTime = (isRefresh)
                ? new Date(signedJWT.getJWTClaimsSet().getIssueTime()
                .toInstant().plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS).toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        if (!signedJWT.verify(verifier)) {
            throw new WebToeicException(ResponseCode.INVALID_TOKEN, ResponseObject.TOKEN);
        }
        if (expiryTime.before(new Date())) {
            throw new WebToeicException(ResponseCode.TOKEN_EXPIRED, ResponseObject.TOKEN);
        }
        if (invalidatedTokenRepository.existsByToken(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new WebToeicException(ResponseCode.TOKEN_INVALIDATED, ResponseObject.TOKEN);
        }
        return signedJWT;
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if(user.getRole() != null){
            stringJoiner.add( "ROLE_" + user.getRole().getCode());
        }
        return stringJoiner.toString();
    }
}
