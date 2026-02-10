package org.example.Entidades;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "COMPRADOR")
public class CompradorClass {

    // Primary Key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremental
    @Id
    @Column (name = "ID")
    private Integer ID;

    @Basic
    @Column (name = "NOMBRE")
    private String NOMBRE;

    @Basic
    @Column (name = "DIRECCION")
    private String DIRECCION;

    @Basic
    @Column (name = "TELEFONO")
    private int TELEFONO;

    @Basic
    @Column (name = "CORREO")
    private String CORREO;

    @Basic
    @Column (name = "CIUDAD")
    private String CIUDAD;

    // Relación OneToMany con PedidosClass
    @OneToMany(mappedBy = "comprador", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidosClass> pedidos = new ArrayList<>();


    public CompradorClass(String NOMBRE, String DIRECCION, int TELEFONO, String CORREO, String CIUDAD) {
        this.NOMBRE = NOMBRE;
        this.DIRECCION = DIRECCION;
        this.TELEFONO = TELEFONO;
        this.CORREO = CORREO;
        this.CIUDAD = CIUDAD;
    }

    public CompradorClass() {

    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getNOMBRE() {
        return NOMBRE;
    }

    public void setNOMBRE(String NOMBRE) {
        this.NOMBRE = NOMBRE;
    }

    public String getDIRECCION() {
        return DIRECCION;
    }

    public void setDIRECCION(String DIRECCION) {
        this.DIRECCION = DIRECCION;
    }

    public int getTELEFONO() {
        return TELEFONO;
    }

    public void setTELEFONO(int TELEFONO) {
        this.TELEFONO = TELEFONO;
    }

    public String getCORREO() {
        return CORREO;
    }

    public void setCORREO(String CORREO) {
        this.CORREO = CORREO;
    }

    public String getCIUDAD() {
        return CIUDAD;
    }

    public void setCIUDAD(String CIUDAD) {
        this.CIUDAD = CIUDAD;
    }

    // Getter/Setter para la relación pedidos
    public List<PedidosClass> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<PedidosClass> pedidos) {
        this.pedidos = pedidos;
    }

    // Helper para añadir/quitar pedidos manteniendo la relación
    public void addPedido(PedidosClass pedido) {
        pedidos.add(pedido);
        pedido.setComprador(this);
    }

    public void removePedido(PedidosClass pedido) {
        pedidos.remove(pedido);
        pedido.setComprador(null);
    }
}
