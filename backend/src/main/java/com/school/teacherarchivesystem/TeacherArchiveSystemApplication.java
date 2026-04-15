package com.school.teacherarchivesystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TeacherArchiveSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(TeacherArchiveSystemApplication.class, args);
    }
}
