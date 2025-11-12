package com.example.ecolimsac.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "residuos")
public class Residuo {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String nombre;
    public String tipo;
    public String categoria;
    public double peso;
    public String fecha;
    public String codigo;
    public String descripcion;
    public String origen;
    public double valorAproximado;
    public String estado;
    public String responsable;
    public String ubicacion;

    public Residuo(String nombre, String tipo, String categoria, double peso, String fecha, String codigo,
                   String descripcion, String origen, double valorAproximado,
                   String estado, String responsable, String ubicacion) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.categoria = categoria;
        this.peso = peso;
        this.fecha = fecha;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.origen = origen;
        this.valorAproximado = valorAproximado;
        this.estado = estado;
        this.responsable = responsable;
        this.ubicacion = ubicacion;
    }
}
