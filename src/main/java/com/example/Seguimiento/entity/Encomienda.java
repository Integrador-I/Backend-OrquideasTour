package com.example.Seguimiento.entity;

import com.example.login.model.Cliente;

import jakarta.persistence.*;

@Entity
@Table(name = "Encomienda")
public class Encomienda
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idenco")
    private Integer idEnco;
    
    private String descripcion;
    private String estado;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "iddestino")
    private Destino destino;

    public Integer getIdEnco() {
        return idEnco;
    }

    public void setIdEnco(Integer idEnco) {
        this.idEnco = idEnco;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Destino getDestino() {
        return destino;
    }

    public void setDestino(Destino destino) {
        this.destino = destino;
    }
}
