package org.example.academycorsi.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.academycorsi.data.entityAuthSecurity.Users;
import org.example.academycorsi.repository.basicAuth.UserRepo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class DBUserSecurityFilter extends OncePerRequestFilter {

    private final UserRepo userRepo;

    public DBUserSecurityFilter(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated()) {
            String username = auth.getName();
            Users user = userRepo.findByUsername(username).orElse(null);

            if (user == null) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Utente non trovato nel microservizio");
                return;
            }

            // altri controlli extra se servono
        }

        filterChain.doFilter(request, response);
    }
}
