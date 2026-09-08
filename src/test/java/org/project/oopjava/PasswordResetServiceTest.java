package org.project.oopjava;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.ObjectProvider;

@ExtendWith(MockitoExtension.class)
class PasswordResetServiceTest {
    @Mock
    private JdbcTemplate jdbcTemplate;
    @Mock
    private JavaMailSender mailSender;

    @Test
    void deliveryFailsExplicitlyWhenNotConfigured() {
        PasswordResetService service = new PasswordResetService(jdbcTemplate, provider(), false);

        assertThrows(PasswordResetUnavailableException.class,
                () -> service.deliver("user@example.com", "token"));
    }

    @Test
    void createsHighEntropyTokenAndStoresOnlyHash() {
        when(jdbcTemplate.queryForObject(any(String.class), eq(Integer.class), eq("user@example.com")))
                .thenReturn(7);
        when(jdbcTemplate.update(any(String.class), any(Object[].class))).thenReturn(1);

        PasswordResetService service = new PasswordResetService(jdbcTemplate, provider(), true);
        String token = service.createToken("user@example.com");

        assertTrue(token.matches("[0-9a-f]{64}"));
    }

    private ObjectProvider<JavaMailSender> provider() {
        return new ObjectProvider<>() {
            @Override public JavaMailSender getObject(Object... args) { return mailSender; }
            @Override public JavaMailSender getIfAvailable() { return mailSender; }
            @Override public JavaMailSender getIfUnique() { return mailSender; }
            @Override public JavaMailSender getObject() { return mailSender; }
        };
    }
}
