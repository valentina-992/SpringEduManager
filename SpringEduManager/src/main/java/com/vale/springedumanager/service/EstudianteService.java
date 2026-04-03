package com.vale.springedumanager.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.vale.springedumanager.entity.Estudiante;
import com.vale.springedumanager.entity.Usuario;
import com.vale.springedumanager.exception.RecursoNoEncontradoException;
import com.vale.springedumanager.repository.EstudianteRepository;
import com.vale.springedumanager.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EstudianteService {

	private final EstudianteRepository repo;
	private final UsuarioRepository repoUsuario;
	
	public List<Estudiante> listar() {
		return repo.findAll();
	}
	
	public Estudiante obtenerPorId(Long id) {
		return repo.findById(id)
				.orElseThrow(() -> new RecursoNoEncontradoException("Estudiante no encontrado"));
	}
	
	public Estudiante grabar(Estudiante e) {
		return repo.save(e);
	}
	
	public void eliminar(Long estudianteId) {
	    Estudiante estudiante = repo.findById(estudianteId)
	            .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

	    // Desvincular del usuario
	    Usuario usuario = estudiante.getUsuario();
	    if (usuario != null) {
	        usuario.setEstudiante(null);
	        
	        repoUsuario.delete(usuario);
	    }

	    // Borrar el estudiante
	    repo.delete(estudiante);
	}
}
