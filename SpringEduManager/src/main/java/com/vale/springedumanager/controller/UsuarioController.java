package com.vale.springedumanager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vale.springedumanager.dto.usuario.UsuarioRequestDTO;
import com.vale.springedumanager.dto.usuario.UsuarioResponseDTO;
import com.vale.springedumanager.entity.Usuario;
import com.vale.springedumanager.mapper.UsuarioMapper;
import com.vale.springedumanager.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

	@Autowired
	UsuarioService servicio;
	@Autowired
	UsuarioMapper mapper;
	@Autowired
	PasswordEncoder encoder;
	
	@GetMapping
	public List<UsuarioResponseDTO> listar() {
		return servicio.listar()
				.stream()
                .map(UsuarioMapper::mapToResponse)
                .toList();
	}

	@GetMapping("/{id}")
	public Usuario obtener(@PathVariable Long id) {
		return servicio.obtener(id);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<UsuarioResponseDTO> crear(@Valid @RequestBody UsuarioRequestDTO dto) {
		Usuario u = Usuario.builder()
				.username(dto.username())
				.password(encoder.encode(dto.password()))
				.role(dto.role())
				.email(dto.email())
				.nombre(dto.nombre())
				.build();
		Usuario guardado = servicio.crear(u);
		return ResponseEntity.status(HttpStatus.CREATED).body(mapper.mapToResponse(guardado));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<UsuarioResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody UsuarioRequestDTO dto) {
		Usuario u = Usuario.builder()
				.id(id)
				.username(dto.username())
				.password(encoder.encode(dto.password()))
				.role(dto.role())
				.email(dto.email())
				.nombre(dto.nombre())
				.build();
		Usuario modificado = servicio.crear(u);
		return ResponseEntity.ok().body(mapper.mapToResponse(modificado));
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public void eliminar(@PathVariable Long id) {
		System.out.println("Auth en controller: " + SecurityContextHolder.getContext().getAuthentication());
		servicio.eliminar(id);
	}
	
}

