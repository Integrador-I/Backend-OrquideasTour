package com.example.login.controller;

import com.example.login.model.Cliente;
import com.example.login.repository.ClienteRepository;
import com.example.login.security.JwtTokenProvider;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(LoginControllerTest.class);

    @InjectMocks
    private LoginController loginController;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cliente = new Cliente();
        cliente.setCorreo("test@example.com");
        cliente.setPassword("123456");
        cliente.setNombre("Test");
        cliente.setApellido("User");
        cliente.setFechaRegistro(LocalDateTime.now());
    }

    @SuppressWarnings("deprecation")
    @Test
    void testLoginExitoso() {
        // Arrange
        when(clienteRepository.findByCorreo(cliente.getCorreo())).thenReturn(cliente);
        when(jwtTokenProvider.generateToken(cliente.getCorreo())).thenReturn("mocked-jwt-token");

        // Act
        ResponseEntity<?> response = loginController.login(cliente);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof Map);

        Map<?, ?> body = (Map<?, ?>) response.getBody();

        assertTrue(body.containsKey("token"));
        assertTrue(body.containsKey("cliente"));
        assertEquals("mocked-jwt-token", body.get("token"));
        assertEquals(cliente.getCorreo(), ((Cliente) body.get("cliente")).getCorreo());

        logger.info("✅ Test login exitoso ejecutado correctamente. Token: {}", body.get("token"));
    }

    @SuppressWarnings("deprecation")
    @Test
    void testLoginCredencialesInvalidas() {
        Cliente almacenado = new Cliente();
        almacenado.setCorreo(cliente.getCorreo());
        almacenado.setPassword("correcta");

        when(clienteRepository.findByCorreo(cliente.getCorreo())).thenReturn(almacenado);

        ResponseEntity<?> response = loginController.login(cliente);

        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Credenciales inválidas", response.getBody());

        logger.info("✅ Test login fallido por credenciales inválidas ejecutado correctamente.");
    }

    @SuppressWarnings("deprecation")
    @Test
    void testRegistroExitoso() {
        when(clienteRepository.findByCorreo(cliente.getCorreo())).thenReturn(null);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        ResponseEntity<?> response = loginController.register(cliente);

        assertEquals(201, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof Cliente);

        logger.info("✅ Test registro exitoso ejecutado correctamente.");
    }

    @SuppressWarnings("deprecation")
    @Test
    void testRegistroCorreoYaExiste() {
        when(clienteRepository.findByCorreo(cliente.getCorreo())).thenReturn(cliente);

        ResponseEntity<?> response = loginController.register(cliente);

        assertEquals(409, response.getStatusCodeValue());
        assertEquals("El correo ya está registrado", response.getBody());

        logger.info("✅ Test registro fallido por correo duplicado ejecutado correctamente.");
    }
}
