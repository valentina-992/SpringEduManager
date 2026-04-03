package com.vale.springedumanager.dto.auth;

import java.util.List;

public record AuthResponseDTO(
		String token,
		String username,
		List<String> role,
		Long estudianteId
		) {

}
