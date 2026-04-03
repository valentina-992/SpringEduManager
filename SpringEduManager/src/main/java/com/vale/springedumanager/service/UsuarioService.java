package com.vale.springedumanager.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vale.springedumanager.entity.Estudiante;
import com.vale.springedumanager.entity.Usuario;
import com.vale.springedumanager.exception.RecursoNoEncontradoException;
import com.vale.springedumanager.repository.EstudianteRepository;
import com.vale.springedumanager.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UsuarioService {

	private final UsuarioRepository repo;
	private final EstudianteRepository estudianteRepo;
	
	public List<Usuario> listar() {
		return repo.findAll();
	}
	
	public Usuario obtener(Long id) {
		return repo.findById(id)
				.orElseThrow(()-> new RecursoNoEncontradoException("Usuario no Encontrado"));
	}
	
	public Usuario crear(Usuario u) {
		Usuario guardado = repo.save(u);
		
		// Creación de estudiante a partir de usuario con rol USER
		if ("USER".equals(u.getRole())) {
			Estudiante estudiante = Estudiante.builder()
					.nombre(u.getNombre())
					.correo(u.getEmail())
					.usuario(guardado)
					.build();
			
			estudianteRepo.save(estudiante);
			
			guardado.setEstudiante(estudiante);
		}
		
		return guardado;
	}
	
	public Usuario actualizar(Long id, Usuario u) {
	    Usuario existente = repo.findById(id)
	            .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));
	    
	    existente.setUsername(u.getUsername());
	    existente.setPassword(u.getPassword());
	    existente.setRole(u.getRole());
	    existente.setEmail(u.getEmail());
	    existente.setNombre(u.getNombre());

	    // Si es ROLE_USER y no tiene estudiante, se crea
	    if ("USER".equals(u.getRole()) && existente.getEstudiante() == null) {
	        Estudiante estudiante = Estudiante.builder()
	                .nombre(u.getNombre())
	                .correo(u.getEmail())
	                .usuario(existente)
	                .build();
	        existente.setEstudiante(estudiante);
	        estudianteRepo.save(estudiante);
	    }

	    return repo.save(existente);
	}
	
	public void eliminar(Long usuarioId) {
        Usuario usuario = repo.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        usuario.setEstudiante(null);

        repo.delete(usuario);
    }
	
}

