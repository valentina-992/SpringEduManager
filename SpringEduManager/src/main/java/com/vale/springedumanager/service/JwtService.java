package com.vale.springedumanager.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	private static final String SECRET = "mi_super_clave_segura_de_al_menos_32_bytes!!_ojala_no_me_quede_corto";
	
	
	public String generarToken(UserDetails userDetail) {
		
		Map<String, Object> claims = new HashMap<>();
		
		claims.put("roles", userDetail.getAuthorities()
				.stream()
				.map(GrantedAuthority::getAuthority)
				.toList());
		
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(userDetail.getUsername())
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis()+1000*60*60))
				.signWith(getKey())
				.compact();
	}

	public String extractUsername(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(getKey())
				.build()
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
	}
	
	public List<String> extraerRoles(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(getKey())
				.build()
				.parseClaimsJws(token)
				.getBody()
				.get("roles", List.class);
	}
	
	private SecretKey getKey() {
		return Keys.hmacShaKeyFor(SECRET.getBytes());
	}
}
