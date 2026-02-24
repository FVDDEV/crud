package com.employee.crud;

import com.employee.crud.entity.User;
import com.employee.crud.exception.UserNotFoundException;
import com.employee.crud.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class CrudApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrudApplication.class, args);
    }

    @Bean
    CommandLineRunner testUser(UserRepository repo, PasswordEncoder encoder) {
        return args -> {
            // delete old test user if exists
            repo.findByUname("test").ifPresent(repo::delete);

            User user = User.builder()
                    .uname("test")
                    .pwd(encoder.encode("test123"))
                    .roles("USER")
                    .build();

            User saved = repo.save(user);
            System.out.println("✅ Test user created: " + saved);

            repo.findAll().forEach(u -> System.out.println("Existing user: " + u));
        };
    }
}