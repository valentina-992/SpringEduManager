package com.vale.springedumanager.mapper;

import org.springframework.stereotype.Component;

import com.vale.springedumanager.dto.estudiante.EstudianteRequestDTO;
import com.vale.springedumanager.dto.estudiante.EstudianteResponseDTO;
import com.vale.springedumanager.entity.Estudiante;

@Component
public class EstudianteMapper {

	public static Estudiante toEntity(EstudianteRequestDTO dto) {
		return Estudiante.builder()
				.nombre(dto.nombre())
				.correo(dto.correo())
				.build();
	}
	
	public static EstudianteResponseDTO toDTO(Estudiante entity) {
		return EstudianteResponseDTO.builder()
				.id(entity.getId())
				.nombre(entity.getNombre())
				.correo(entity.getCorreo())
				.usuarioId(entity.getUsuario() != null ? entity.getUsuario().getId() : null)
				.build();
	}
}

