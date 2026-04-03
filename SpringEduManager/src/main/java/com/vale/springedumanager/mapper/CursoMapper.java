package com.vale.springedumanager.mapper;

import org.springframework.stereotype.Component;

import com.vale.springedumanager.dto.curso.CursoRequestDTO;
import com.vale.springedumanager.dto.curso.CursoResponseDTO;
import com.vale.springedumanager.entity.Curso;

@Component
public class CursoMapper {

	public static Curso toEntity(CursoRequestDTO dto) {
		return Curso.builder()
				.nombre(dto.nombre())
				.build();
	}
	
	public static CursoResponseDTO toDTO(Curso entity) {
		return CursoResponseDTO.builder()
				.id(entity.getId())
				.nombre(entity.getNombre())
				.build();
	}
}

