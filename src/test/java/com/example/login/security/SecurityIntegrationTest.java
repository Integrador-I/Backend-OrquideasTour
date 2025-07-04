package com.example.login.security;

import com.example.login.config.SecurityConfig;
import com.example.login.controller.LoginController;
import com.example.login.model.Cliente;
import com.example.login.repository.ClienteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = LoginController.class)
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
    @MockBean
    private ClienteRepository clienteRepository;

    @SuppressWarnings("removal")
    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @SuppressWarnings("removal")
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @SuppressWarnings("removal")
    @MockBean
    private AuthEntryPoint authEntryPoint;

    private Cliente cliente;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setCorreo("test@correo.com");
        cliente.setPassword("123456");
    }

    @Test
    void accederEndpointPublicoSinToken() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/seguimiento/EC123"))
            .andReturn();

        System.out.println("Response status: " + result.getResponse().getStatus());
        System.out.println("Response body: " + result.getResponse().getContentAsString());
    }

    @Test
    void accederEndpointProtegidoSinToken() throws Exception {
        mockMvc.perform(get("/clientes"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void accederEndpointProtegidoConTokenInvalido() throws Exception {
        when(jwtTokenProvider.validateToken("bad-token")).thenReturn(false);

        mockMvc.perform(get("/clientes")
                        .header("Authorization", "Bearer bad-token"))
                .andExpect(status().isUnauthorized());
    }
}
