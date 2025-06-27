package com.example.login.controller;

import com.example.login.model.Cliente;
import com.example.login.service.ClienteService;
import com.google.common.base.Preconditions;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clientes")
@CrossOrigin(origins = "*")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public List<Cliente> listarClientes() {
        return clienteService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public Optional<Cliente> obtenerPorId(@PathVariable Long id) {
        return clienteService.obtenerPorId(id);
    }

    @PostMapping
    public Cliente guardarCliente(@RequestBody Cliente cliente) {
        Preconditions.checkNotNull(cliente.getCorreo(), "El correo no puede ser nulo");

        if (!EmailValidator.getInstance().isValid(cliente.getCorreo())) {
            throw new IllegalArgumentException("Correo inválido");
        }

        return clienteService.guardar(cliente);
    }

    @PutMapping("/{id}")
    public Cliente actualizarCliente(@PathVariable Long id, @RequestBody Cliente cliente) {
        cliente.setClienteId(id);
        return clienteService.guardar(cliente);
    }

    @DeleteMapping("/{id}")
    public String eliminarCliente(@PathVariable Long id) {
        boolean eliminado = clienteService.eliminar(id);
        return eliminado ? "Cliente eliminado correctamente" : "No se pudo eliminar el cliente";
    }

    @GetMapping("/buscar")
    public Cliente buscarPorCorreo(@RequestParam("correo") String correo) {
        return clienteService.obtenerPorCorreo(correo);
    }
}
