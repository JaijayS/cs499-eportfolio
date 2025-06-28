
// src/main/java/com/example/DriverPass/config/DatabaseSeeder.java
package com.example.DriverPass.config;

import com.example.DriverPass.model.*;
import com.example.DriverPass.repository.CourseRepository;
import com.example.DriverPass.repository.UserRepository;
import com.example.DriverPass.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DatabaseSeeder {

    private final UserRepository userRepo;
    private final CourseRepository courseRepo;
    private final VehicleRepository vehicleRepo;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner seedDatabase() {
        return args -> {
            // Clean DB
            courseRepo.deleteAll();
            vehicleRepo.deleteAll();
            userRepo.deleteAll();

            // Create users with encoded password
            User admin = User.builder()
                    .firstName("Admin")
                    .lastName("User")
                    .email("admin@example.com")
                    .password(passwordEncoder.encode("password123"))
                    .role(Role.ROLE_ADMIN)
                    .build();

            User student = User.builder()
                    .firstName("Student")
                    .lastName("User")
                    .email("student@example.com")
                    .password(passwordEncoder.encode("password123"))
                    .role(Role.ROLE_STUDENT)
                    .build();

            User teacher = User.builder()
                    .firstName("Teacher")
                    .lastName("User")
                    .email("teacher@example.com")
                    .password(passwordEncoder.encode("password123"))
                    .role(Role.ROLE_TEACHER)
                    .build();

            userRepo.saveAll(List.of(admin, student, teacher));

            teacher = userRepo.findByEmail("teacher@example.com").orElse(null);
            student = userRepo.findByEmail("student@example.com").orElse(null);

            Course course1 = Course.builder()
                    .name("Beginner Driving Course")
                    .description("Learn the basics of driving")
                    .teacher(teacher)
                    .build();

            Course course2 = Course.builder()
                    .name("Advanced Driving Course")
                    .description("Refine your driving skills")
                    .teacher(teacher)
                    .build();

            courseRepo.saveAll(List.of(course1, course2));

            course1.setStudents(Set.of(student));
            courseRepo.save(course1);

            Vehicle v1 = Vehicle.builder()
                    .make("Toyota").model("Camry").plateNumber("ABC123")
                    .checkedOut(false).assignedTo(null).assignedToName("")
                    .build();

            Vehicle v2 = Vehicle.builder()
                    .make("Honda").model("Civic").plateNumber("XYZ789")
                    .checkedOut(false).assignedTo(null).assignedToName("")
                    .build();

            vehicleRepo.saveAll(List.of(v1, v2));
        };
    }

}
