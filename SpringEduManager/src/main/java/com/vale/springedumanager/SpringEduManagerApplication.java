package com.vale.springedumanager;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.vale.springedumanager.entity.Usuario;
import com.vale.springedumanager.repository.UsuarioRepository;

@SpringBootApplication
public class SpringEduManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringEduManagerApplication.class, args);
	}
	
	@Bean
	CommandLineRunner run(UsuarioRepository repo) {
	    return args -> {
	        if (repo.findByUsername("admin").isEmpty()) {
	            repo.save(
	                Usuario.builder()
	                    .username("admin")
	                    .password(new BCryptPasswordEncoder().encode("1234"))
	                    .role("ADMIN")
	                    .email("admin@test.com")
	                    .build()
	            );
	        }
	    };
	}
}

