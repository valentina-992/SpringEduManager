package com.vale.springedumanager.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.vale.springedumanager.dto.evaluacion.EvaluacionesItemDTO;
import com.vale.springedumanager.dto.evaluacion.TableroCursoDTO;
import com.vale.springedumanager.entity.Evaluacion;
import com.vale.springedumanager.exception.RecursoNoEncontradoException;
import com.vale.springedumanager.repository.EvaluacionRepository;
import com.vale.springedumanager.repository.InscripcionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EvaluacionService {

	private final EvaluacionRepository repo;
	private final InscripcionRepository repoIn;
	
	public Evaluacion grabar(Long inscripcionId, Evaluacion eva) {
		var c = repoIn.findById(inscripcionId)
				.orElseThrow(() -> new RecursoNoEncontradoException("Inscripcion no encontrada"));
		eva.setInscripcion(c);
		return repo.save(eva);
	}
	
	public List<Evaluacion> listar(){
		return repo.findAll();
	}
	
	public List<TableroCursoDTO> obtenerTablero(Long estudianteId) {
		
		var listaPlana = repoIn.findDashboardByEstudiante(estudianteId);
		
		Map<Long, TableroCursoDTO> mapa = new LinkedHashMap<>();
		
		for(var row: listaPlana) {
			mapa.putIfAbsent(row.getCursoId(), 
					TableroCursoDTO.builder()
					.cursoId(row.getCursoId())
					.nombreCurso(row.getNombreCurso())
					.evaluaciones(new ArrayList<>())
					.build()
			);
			
			if (row.getEvaluacionId() != null) {
				mapa.get(row.getCursoId()).getEvaluaciones().add(
						EvaluacionesItemDTO.builder()
						.evaluacionId(row.getEvaluacionId())
						.evaluacionNombre(row.getEvaluacionNombre())
						.puntuacion(row.getPuntuacion())
						.build()
				);
			}
		}
		return new ArrayList<>(mapa.values());
	}
}
