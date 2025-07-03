package com.example.login.service;

import com.example.login.model.Cliente;
import com.example.login.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.Optional;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente;

    private static final Logger logger = LoggerFactory.getLogger(ClienteServiceTest.class);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        cliente = new Cliente();
        cliente.setClienteId(1L);
        cliente.setCorreo("ejemplo@gmail.com");
        cliente.setNombre("Juan");
    }

    @Test
    void testObtenerTodos() {
        List<Cliente> mockClientes = Arrays.asList(new Cliente(), new Cliente());
        when(clienteRepository.findAll()).thenReturn(mockClientes);

        List<Cliente> resultado = clienteService.obtenerTodos();

        assertEquals(2, resultado.size());
        logger.info("testObtenerTodos ejecutado con éxito. Total clientes: {}", resultado.size());
    }

    @Test
    void testGuardarCliente() {
        when(clienteRepository.save(cliente)).thenReturn(cliente);
        Cliente result = clienteService.guardar(cliente);

        assertNotNull(result);
        assertEquals("Juan", result.getNombre());
        logger.info("testGuardarCliente ejecutado con éxito. Cliente guardado: {}", result.getNombre());
    }

    @Test
    void testObtenerPorId() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        Optional<Cliente> result = clienteService.obtenerPorId(1L);

        assertTrue(result.isPresent());
        assertEquals("Juan", result.get().getNombre());
        logger.info("testObtenerPorId ejecutado con éxito. Cliente encontrado: {}", result.get().getNombre());
    }

    @Test
    void testEliminarClienteExistente() {
        when(clienteRepository.existsById(1L)).thenReturn(true);
        boolean eliminado = clienteService.eliminar(1L);

        assertTrue(eliminado);
        verify(clienteRepository, times(1)).deleteById(1L);
        logger.info("testEliminarClienteExistente ejecutado con éxito. Cliente eliminado con ID: {}", 1L);
    }

    @Test
    void testEliminarClienteInexistente() {
        when(clienteRepository.existsById(2L)).thenReturn(false);
        boolean eliminado = clienteService.eliminar(2L);

        assertFalse(eliminado);
        verify(clienteRepository, never()).deleteById(2L);
        logger.info("testEliminarClienteInexistente ejecutado con éxito. Cliente con ID {} no existía.", 2L);
    }
}
