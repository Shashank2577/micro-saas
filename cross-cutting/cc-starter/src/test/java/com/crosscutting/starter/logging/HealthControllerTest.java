package com.crosscutting.starter.logging;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import javax.sql.DataSource;
import java.sql.Connection;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HealthController.class)
@AutoConfigureMockMvc(addFilters = false)
class HealthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DataSource dataSource;

    @Test
    void healthReturnsUp() throws Exception {
        mockMvc.perform(get("/cc/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void readyReturnsUpWhenDatabaseIsHealthy() throws Exception {
        Connection connection = mock(Connection.class);
        when(connection.isValid(2)).thenReturn(true);
        when(dataSource.getConnection()).thenReturn(connection);

        mockMvc.perform(get("/cc/health/ready"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.checks.database").value("UP"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void readyReturnsDownWhenDatabaseIsUnhealthy() throws Exception {
        Connection connection = mock(Connection.class);
        when(connection.isValid(2)).thenReturn(false);
        when(dataSource.getConnection()).thenReturn(connection);

        mockMvc.perform(get("/cc/health/ready"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DOWN"))
                .andExpect(jsonPath("$.checks.database").value("DOWN"));
    }

    @Test
    void readyReturnsDownWhenDatabaseConnectionFails() throws Exception {
        when(dataSource.getConnection()).thenThrow(new RuntimeException("Connection refused"));

        mockMvc.perform(get("/cc/health/ready"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DOWN"))
                .andExpect(jsonPath("$.checks.database").value("DOWN"));
    }
}
