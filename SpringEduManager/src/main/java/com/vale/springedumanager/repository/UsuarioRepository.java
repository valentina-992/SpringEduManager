package com.vale.springedumanager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vale.springedumanager.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	Optional<Usuario> findByUsername(String username);

}
