package com.vale.springedumanager.mapper;

import org.springframework.stereotype.Component;

import com.vale.springedumanager.dto.usuario.UsuarioResponseDTO;
import com.vale.springedumanager.entity.Usuario;


@Component
public class UsuarioMapper {
	
	public static UsuarioResponseDTO mapToResponse(Usuario u) {
		return UsuarioResponseDTO.builder()
                .id(u.getId())
                .username(u.getUsername())
                .nombre(u.getNombre())
                .email(u.getEmail())
                .role(u.getRole())
                .estudianteId(u.getEstudiante() != null ? u.getEstudiante().getId() : null)
                .build();
	}
}

