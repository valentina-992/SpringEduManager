package com.vale.springedumanager.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vale.springedumanager.dto.curso.CursoRequestDTO;
import com.vale.springedumanager.dto.curso.CursoResponseDTO;
import com.vale.springedumanager.mapper.CursoMapper;
import com.vale.springedumanager.service.CursoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cursos")
@RequiredArgsConstructor
public class CursoController {

	private final CursoService service;
	
	@PostMapping
	public ResponseEntity<CursoResponseDTO> grabar(@RequestBody CursoRequestDTO dto) {
		var curso = CursoMapper.toEntity(dto);
		var grabado = service.grabar(curso);
		return ResponseEntity.status(HttpStatus.CREATED).body(CursoMapper.toDTO(grabado));
	}
	
	@GetMapping
	public List<CursoResponseDTO> listar(){
		return service.listar()
				.stream()
				.map(CursoMapper::toDTO)
				.toList();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable Long id) {
	    service.eliminar(id);
	    return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/test")
	public String test() {
	    return "OK";
	}

	
}
