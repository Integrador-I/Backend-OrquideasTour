package com.example.login.security;

import com.example.login.model.Cliente;
import com.example.login.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Cliente cliente = clienteRepository.findByCorreo(correo);
        if (cliente == null) {
            throw new UsernameNotFoundException("Usuario no encontrado con el correo: " + correo);
        }

        return User.builder()
                .username(cliente.getCorreo())
                .password("{noop}" + cliente.getPassword()) // {noop} para evitar encriptación temporalmente
                .roles("USER") // puedes usar cliente.getRol() si tienes roles
                .build();
    }
}
