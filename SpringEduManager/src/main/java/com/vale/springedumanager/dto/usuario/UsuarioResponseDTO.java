package com.vale.springedumanager.dto.usuario;

import lombok.Builder;

@Builder
public record UsuarioResponseDTO(
		Long id,
		String username,
		String role,
		String email,
		String nombre,
		Long estudianteId
		) {

}
