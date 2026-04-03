package com.vale.springedumanager.controller;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vale.springedumanager.dto.auth.AuthRequestDTO;
import com.vale.springedumanager.dto.auth.AuthResponseDTO;
import com.vale.springedumanager.entity.Estudiante;
import com.vale.springedumanager.repository.EstudianteRepository;
import com.vale.springedumanager.service.JwtService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final JwtService jwtService;
	private final AuthenticationManager authManager;
	private final EstudianteRepository estudianteRepository;
	
	@PostMapping("/login")
	public AuthResponseDTO login(@Valid @RequestBody AuthRequestDTO request) {
		
		try {
			System.out.println("Username :"+request.username());
			System.out.println("Password :"+request.password());
			
			Authentication auth = authManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							request.username(), 
							request.password()
							)
					);
			
			
			UserDetails userDetail = (UserDetails) auth.getPrincipal();
			
			String token = jwtService.generarToken(userDetail);
			
			List<String> roles = userDetail.getAuthorities()
			        .stream()
			        .map(GrantedAuthority::getAuthority)
			        .toList();
			
			Long estudianteId = estudianteRepository
					.findByUsuarioUsername(userDetail.getUsername())
					.map(Estudiante::getId)
					.orElse(null);

			    return new AuthResponseDTO(
			        token,
			        userDetail.getUsername(),
			        roles,
			        estudianteId
			    );
		}catch(Exception e) {
			System.out.println("Error auth: "+e.getMessage());
			throw e;
		}
	}
}
