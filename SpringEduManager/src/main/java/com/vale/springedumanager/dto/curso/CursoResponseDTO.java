package com.vale.springedumanager.dto.curso;

import lombok.Builder;

@Builder
public record CursoResponseDTO(
		Long id,
		String nombre
		) {

}
