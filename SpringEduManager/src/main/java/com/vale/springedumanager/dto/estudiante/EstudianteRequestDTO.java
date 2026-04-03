package com.vale.springedumanager.dto.estudiante;

import jakarta.validation.constraints.NotNull;

public record EstudianteRequestDTO(
		@NotNull
		String nombre,
		@NotNull
		String correo
		) {

}
