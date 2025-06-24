package com.doan2025.webtoeic.config;

import com.doan2025.webtoeic.constants.enums.ERole;
import com.doan2025.webtoeic.domain.User;
import com.doan2025.webtoeic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    @Value("${account.manager.email}")
    private String MANAGER_EMAIL;

    @Value("${account.manager.password}")
    private String MANAGER_PASSWORD;

    @Value("${account.consultant.email}")
    private String CONSULTANT_EMAIL;

    @Value("${account.consultant.password}")
    private String CONSULTANT_PASSWORD;

    @Value("${account.teacher.email}")
    private String TEACHER_EMAIL;

    @Value("${account.teacher.password}")
    private String TEACHER_PASSWORD;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void run(String... args){

        if(!userRepository.existsByEmail(MANAGER_EMAIL)) {
            User user = new User();
            user.setEmail(MANAGER_EMAIL);
            user.setPassword(passwordEncoder.encode(MANAGER_PASSWORD));
            user.setFirstName("User");
            user.setLastName("Admin");
            user.setRole(ERole.MANAGER);
            userRepository.save(user);
        }
        if(!userRepository.existsByEmail(CONSULTANT_EMAIL)) {
            User user = new User();
            user.setEmail(CONSULTANT_EMAIL);
            user.setPassword(passwordEncoder.encode(CONSULTANT_PASSWORD));
            user.setFirstName("User");
            user.setLastName("Consultant");
            user.setRole(ERole.CONSULTANT);
            userRepository.save(user);
        }
        if(!userRepository.existsByEmail(TEACHER_EMAIL)) {
            User user = new User();
            user.setEmail(TEACHER_EMAIL);
            user.setPassword(passwordEncoder.encode(TEACHER_PASSWORD));
            user.setFirstName("User");
            user.setLastName("Teacher");
            user.setRole(ERole.TEACHER);
            userRepository.save(user);
        }
    }
}
