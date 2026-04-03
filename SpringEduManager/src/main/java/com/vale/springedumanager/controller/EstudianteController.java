package com.vale.springedumanager.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vale.springedumanager.dto.estudiante.EstudianteRequestDTO;
import com.vale.springedumanager.dto.estudiante.EstudianteResponseDTO;
import com.vale.springedumanager.mapper.EstudianteMapper;
import com.vale.springedumanager.service.EstudianteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/estudiantes")
@RequiredArgsConstructor
public class EstudianteController {

	private final EstudianteService service;
	
	@GetMapping
	public List<EstudianteResponseDTO> listar(){
		return service.listar()
				.stream()
				.map(EstudianteMapper::toDTO)
				.collect(Collectors.toList());
	}
	
	@GetMapping("/{id}")
	public EstudianteResponseDTO obtenerPorId(@PathVariable Long id) {
		return EstudianteMapper.toDTO(service.obtenerPorId(id));
	}
	
	
	@PostMapping
	public ResponseEntity<EstudianteResponseDTO> grabar(@RequestBody EstudianteRequestDTO dto) {
		var stud = EstudianteMapper.toEntity(dto);
		var grabado = service.grabar(stud);
		return ResponseEntity.status(HttpStatus.CREATED).body(EstudianteMapper.toDTO(grabado));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> eliminar(@PathVariable Long id) {
		service.eliminar(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
