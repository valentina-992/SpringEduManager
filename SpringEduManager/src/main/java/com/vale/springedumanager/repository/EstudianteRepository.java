package com.vale.springedumanager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vale.springedumanager.entity.Estudiante;

public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
	Optional<Estudiante> findByCorreo(String correo);
	Optional<Estudiante> findByUsuarioUsername(String username);

}
