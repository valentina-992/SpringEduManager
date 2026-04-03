package com.vale.springedumanager.dto.usuario;

import jakarta.validation.constraints.NotBlank;

public record UsuarioRequestDTO(
		@NotBlank
		String username,
		
		@NotBlank
		String password,
		
		@NotBlank
		String role,
		
		@NotBlank
		String email,
		
		@NotBlank
		String nombre
		) {

}
