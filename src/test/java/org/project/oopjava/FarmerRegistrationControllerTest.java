package org.project.oopjava;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.ui.Model;

@ExtendWith(MockitoExtension.class)
class FarmerRegistrationControllerTest {
    @Mock JdbcTemplate jdbcTemplate;
    @Mock Model model;

    @Test
    void registersFarmerWithEncodedPasswordAndFarmerRole() {
        when(jdbcTemplate.update(any(String.class), any(Object[].class))).thenReturn(1);
        FarmerRegistrationController controller = new FarmerRegistrationController(jdbcTemplate);

        String view = controller.register("Farmer", "farmer@example.com", "9876543210",
                "Strong@Pass1", "Strong@Pass1", "Green Farm", "Village Road", model);

        assertEquals("login", view);
        verify(jdbcTemplate).update(contains("'FARMER'"), eq("farmer"), eq("9876543210"),
                eq("farmer@example.com"), any(String.class), eq("Green Farm"), eq("Village Road"));
    }
}
