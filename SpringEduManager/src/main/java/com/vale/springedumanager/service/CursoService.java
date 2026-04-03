package com.vale.springedumanager.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.vale.springedumanager.entity.Curso;
import com.vale.springedumanager.repository.CursoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CursoService {

	private final CursoRepository repo;
	
	public Curso grabar(Curso curso) {
		return repo.save(curso);
	}
	
	public List<Curso> listar(){
		return repo.findAll();
	}
	
	public void eliminar(Long id) {
		repo.deleteById(id);
	}
}