package com.vale.springedumanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vale.springedumanager.entity.Curso;

public interface CursoRepository extends JpaRepository<Curso, Long> {

}
