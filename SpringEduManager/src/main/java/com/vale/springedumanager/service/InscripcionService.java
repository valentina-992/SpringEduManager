package com.vale.springedumanager.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.vale.springedumanager.entity.Inscripcion;
import com.vale.springedumanager.exception.RecursoNoEncontradoException;
import com.vale.springedumanager.repository.CursoRepository;
import com.vale.springedumanager.repository.EstudianteRepository;
import com.vale.springedumanager.repository.InscripcionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InscripcionService {

	private final InscripcionRepository repoIns;
	private final EstudianteRepository repoEst;
	private final CursoRepository repoC;
	
	public Inscripcion grabar(Long estudianteId, Long cursoId) {
		var estudiante = repoEst.findById(estudianteId)
				.orElseThrow(() -> new RecursoNoEncontradoException("Estudiante no encontrado"));
		var curso = repoC.findById(cursoId)
				.orElseThrow(() -> new RecursoNoEncontradoException("Curso no encontrado"));
		
		var inscripcion = Inscripcion.builder()
				.estudiante(estudiante)
				.curso(curso)
				.fecha_inscripcion(LocalDate.now())
				.build();
		
		return repoIns.save(inscripcion);
	}
	
	public List<Inscripcion> listar(){
		return repoIns.findAll();
	}
}

