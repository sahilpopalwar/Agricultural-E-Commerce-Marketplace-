package org.project.oopjava.security;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {
    private static final String FIND_USER_SQL = """
        SELECT email, password_hash, role
        FROM users
        WHERE email = ?
        """;

    private final JdbcTemplate jdbcTemplate;

    public DatabaseUserDetailsService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        final Map<String, Object> user;
        try {
            user = jdbcTemplate.queryForMap(FIND_USER_SQL, email);
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            throw new UsernameNotFoundException("No user found for email: " + email, ex);
        }

        Object roleValue = user.get("role");
        String role = roleValue == null ? "USER" : String.valueOf(roleValue).toUpperCase();
        GrantedAuthority authority = new SimpleGrantedAuthority(
            role.startsWith("ROLE_") ? role : "ROLE_" + role
        );

        return User.withUsername(String.valueOf(user.get("email")))
            .password(String.valueOf(user.get("password_hash")))
            .authorities(List.of(authority))
            .build();
    }
}
