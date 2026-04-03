package com.vale.springedumanager.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vale.springedumanager.dto.evaluacion.EvaluacionRequestDTO;
import com.vale.springedumanager.dto.evaluacion.EvaluacionResponseDTO;
import com.vale.springedumanager.dto.evaluacion.TableroCursoDTO;
import com.vale.springedumanager.mapper.EvaluacionMapper;
import com.vale.springedumanager.service.EvaluacionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/evaluaciones")
@RequiredArgsConstructor
public class EvaluacionController {

	private final EvaluacionService service;
	
	@PostMapping("/inscripcion/{id}")
	public ResponseEntity<EvaluacionResponseDTO> grabar(
			@PathVariable Long id, 
			@RequestBody EvaluacionRequestDTO dto) {
		
		var eval = EvaluacionMapper.toEntity(dto);
		var grabado = service.grabar(id, eval);
		return ResponseEntity.status(HttpStatus.CREATED).body(EvaluacionMapper.toDTO(grabado));
	}
	
	@GetMapping
	public List<EvaluacionResponseDTO> listar(){		
		return service.listar()
				.stream()
				.map(EvaluacionMapper::toDTO)
				.toList();
	}
	
	@GetMapping("/tablero/{estudianteId}")
	public List<TableroCursoDTO> tablero(@PathVariable Long estudianteId) {
		return service.obtenerTablero(estudianteId);
	}
}
