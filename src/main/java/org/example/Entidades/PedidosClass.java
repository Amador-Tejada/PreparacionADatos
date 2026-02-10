package org.example.Entidades;


import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "PEDIDOS")
public class PedidosClass {

    // Primary Key autogenerada
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremental
    @Id
    @Column (name = "ID")
    private Integer ID;

    @Basic
    @Column (name = "NOMBRE")
    private String NOMBRE;

    @Basic
    @Column (name = "FECHA")
    private Date FECHA;

    @Basic
    @Column (name = "PRECIO")
    private Double PRECIO;

    @Basic
    @Column (name = "CANTIDAD")
    private Integer CANTIDAD;

    @Basic
    @Column (name = "ESTADO")
    private String ESTADO;

    // Relación ManyToOne hacia CompradorClass (clave foránea)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPRADOR_ID")
    private CompradorClass comprador;

    public PedidosClass() {
    }

    public PedidosClass(String NOMBRE, Date FECHA, Double PRECIO, Integer CANTIDAD, String ESTADO) {
        this.NOMBRE = NOMBRE;
        this.FECHA = FECHA;
        this.PRECIO = PRECIO;
        this.CANTIDAD = CANTIDAD;
        this.ESTADO = ESTADO;
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

    public Date getFECHA() {
        return FECHA;
    }

    public void setFECHA(Date FECHA) {
        this.FECHA = FECHA;
    }

    public Double getPRECIO() {
        return PRECIO;
    }

    public void setPRECIO(Double PRECIO) {
        this.PRECIO = PRECIO;
    }

    public Integer getCANTIDAD() {
        return CANTIDAD;
    }

    public void setCANTIDAD(Integer CANTIDAD) {
        this.CANTIDAD = CANTIDAD;
    }

    public String getESTADO() {
        return ESTADO;
    }

    public void setESTADO(String ESTADO) {
        this.ESTADO = ESTADO;
    }

    // Getter/Setter para la relación con Comprador
    public CompradorClass getComprador() {
        return comprador;
    }

    public void setComprador(CompradorClass comprador) {
        this.comprador = comprador;
    }
}
