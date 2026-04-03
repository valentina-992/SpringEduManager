package com.vale.springedumanager.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vale.springedumanager.dto.inscripcion.InscripcionRequestDTO;
import com.vale.springedumanager.dto.inscripcion.InscripcionResponseDTO;
import com.vale.springedumanager.mapper.InscripcionMapper;
import com.vale.springedumanager.service.InscripcionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/inscripciones")
@RequiredArgsConstructor
public class InscripcionController {

	private final InscripcionService service;
	
	@PostMapping
	public ResponseEntity<InscripcionResponseDTO> grabar(@RequestBody InscripcionRequestDTO dto) {
		var inscripcion = service.grabar(dto.estudianteId(), dto.cursoId());
		
		return ResponseEntity.status(HttpStatus.CREATED).body(InscripcionMapper.toDTO(inscripcion));
	}
	
	@GetMapping
	public List<InscripcionResponseDTO> listar(){
		return service.listar()
				.stream()
				.map(InscripcionMapper::toDTO)
				.toList();
	}
}
