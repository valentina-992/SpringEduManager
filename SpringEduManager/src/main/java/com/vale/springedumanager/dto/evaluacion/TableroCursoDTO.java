package com.vale.springedumanager.dto.evaluacion;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TableroCursoDTO {
	private Long cursoId;
	private String nombreCurso;
	private List<EvaluacionesItemDTO> evaluaciones;
}
