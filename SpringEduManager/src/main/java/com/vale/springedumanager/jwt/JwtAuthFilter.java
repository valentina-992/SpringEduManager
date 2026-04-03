package com.vale.springedumanager.jwt;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.vale.springedumanager.service.CustomUserDetailsService;
import com.vale.springedumanager.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("JwtAuthFilter ejecutándose: " + request.getServletPath());

        // Permitir preflight OPTIONS y rutas de auth sin JWT
        if ("OPTIONS".equalsIgnoreCase(request.getMethod()) || request.getServletPath().startsWith("/api/auth")) {
            response.setStatus(HttpServletResponse.SC_OK); // importante para CORS
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        System.out.println("Authorization header: " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = authHeader.substring(7);
            String username = jwtService.extractUsername(token);
            List<String> roles = jwtService.extraerRoles(token);

            System.out.println("Username extraído: " + username);
            System.out.println("Roles extraídos: " + roles);

            // Convertir roles a GrantedAuthority con prefijo ROLE_ si falta
            List<SimpleGrantedAuthority> authorities = roles.stream()
                    .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                authorities // usar authorities extraídas del JWT
                        );

                authToken.setDetails(request);
                SecurityContextHolder.getContext().setAuthentication(authToken);

                System.out.println("Usuario autenticado correctamente");
                System.out.println("Auth establecida: " + SecurityContextHolder.getContext().getAuthentication());
            }

        } catch (Exception ex) {
            System.out.println("Error JWT: " + ex.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
