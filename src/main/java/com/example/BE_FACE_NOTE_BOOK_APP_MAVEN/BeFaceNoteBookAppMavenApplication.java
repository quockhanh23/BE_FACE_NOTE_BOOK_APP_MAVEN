package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.common.Constants;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.model.Role;
import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BeFaceNoteBookAppMavenApplication {

	public static void main(String[] args) {
		SpringApplication.run(BeFaceNoteBookAppMavenApplication.class, args);
	}

	@Bean
	CommandLineRunner run(RoleRepository roleRepository) {
		return args ->
		{
			if (roleRepository.findByName(Constants.Roles.ROLE_USER) == null) {
				roleRepository.save(new Role(Constants.Roles.ROLE_USER));
			}
			if (roleRepository.findByName(Constants.Roles.ROLE_ADMIN) == null) {
				roleRepository.save(new Role(Constants.Roles.ROLE_ADMIN));
			}
		};
	}
}
