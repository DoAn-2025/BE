package com.doan2025.webtoeic.config;

import com.doan2025.webtoeic.constants.Constants;
import com.doan2025.webtoeic.constants.enums.ERole;
import com.doan2025.webtoeic.domain.Consultant;
import com.doan2025.webtoeic.domain.Manager;
import com.doan2025.webtoeic.domain.Teacher;
import com.doan2025.webtoeic.domain.User;
import com.doan2025.webtoeic.repository.ConsultantRepository;
import com.doan2025.webtoeic.repository.ManagerRepository;
import com.doan2025.webtoeic.repository.TeacherRepository;
import com.doan2025.webtoeic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Random;

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
    private final ManagerRepository managerRepository;
    private final ConsultantRepository consultantRepository;
    private final TeacherRepository teacherRepository;


    @Override
    public void run(String... args){

        if(!userRepository.existsByEmail(MANAGER_EMAIL)) {
            User user = new User();
            user.setEmail(MANAGER_EMAIL);
            user.setPassword(passwordEncoder.encode(MANAGER_PASSWORD));
            user.setFirstName("User");
            user.setLastName("Admin");
            user.setRole(ERole.MANAGER);
            boolean check = false;
            while(!check) {
                String code = generatedUserCode(ERole.MANAGER);
                if(!userRepository.existsByCode(code)){
                    user.setCode(code);
                    check = true;
                }
            }
            Manager manager = managerRepository.save(new Manager());
            user.setManager(manager);
            User u = userRepository.save(user);
            manager.setUser(u);
            managerRepository.save(manager);
        }
        if(!userRepository.existsByEmail(CONSULTANT_EMAIL)) {
            User user = new User();
            user.setEmail(CONSULTANT_EMAIL);
            user.setPassword(passwordEncoder.encode(CONSULTANT_PASSWORD));
            user.setFirstName("User");
            user.setLastName("Consultant");
            user.setRole(ERole.CONSULTANT);
            boolean check = false;
            while(!check) {
                String code = generatedUserCode(ERole.CONSULTANT);
                if(!userRepository.existsByCode(code)){
                    user.setCode(code);
                    check = true;
                }
            }
            Consultant consultant = consultantRepository.save(new Consultant());
            user.setConsultant(consultant);
            userRepository.save(user);
            consultant.setUser(user);
            consultantRepository.save(consultant);
        }
        if(!userRepository.existsByEmail(TEACHER_EMAIL)) {
            User user = new User();
            user.setEmail(TEACHER_EMAIL);
            user.setPassword(passwordEncoder.encode(TEACHER_PASSWORD));
            user.setFirstName("User");
            user.setLastName("Teacher");
            user.setRole(ERole.TEACHER);
            boolean check = false;
            while(!check) {
                String code = generatedUserCode(ERole.TEACHER);
                if(!userRepository.existsByCode(code)){
                    user.setCode(code);
                    check = true;
                }
            }
            Teacher teacher = teacherRepository.save(new Teacher("dai hoc 1", "Bang cap 1"));
            user.setTeacher(teacher);
            userRepository.save(user);
            teacher.setUser(user);
            teacherRepository.save(teacher);
        }
    }
    private String generatedUserCode(ERole role) {
        return switch (role) {
            case TEACHER -> Constants.PRE_CODE_TEACHER + new Random().nextLong(100_000_000, 999_999_999);
            case CONSULTANT -> Constants.PRE_CODE_CONSULTANT + new Random().nextLong(100_000_000, 999_999_999);
            case MANAGER -> Constants.PRE_CODE_MANAGER + new Random().nextLong(100_000_000, 999_999_999);
            default -> Constants.PRE_CODE_STUDENT + new Random().nextLong(100_000_000, 999_999_999);
        };
    }
}
