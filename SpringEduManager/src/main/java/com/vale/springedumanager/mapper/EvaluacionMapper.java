package com.vale.springedumanager.mapper;

import org.springframework.stereotype.Component;

import com.vale.springedumanager.dto.evaluacion.EvaluacionRequestDTO;
import com.vale.springedumanager.dto.evaluacion.EvaluacionResponseDTO;
import com.vale.springedumanager.entity.Evaluacion;

@Component
public class EvaluacionMapper {

	public static Evaluacion toEntity(EvaluacionRequestDTO dto) {
		return Evaluacion.builder()
				.nombre(dto.nombre())
				.puntuacion(dto.puntuacion())
				.build();
	}
	
	public static EvaluacionResponseDTO toDTO(Evaluacion entity) {
		return EvaluacionResponseDTO.builder()
				.id(entity.getId())
				.nombre(entity.getNombre())
				.puntuacion(entity.getPuntuacion())
				.inscripcionId(entity.getInscripcion().getId())
				.build();
	}
}

