package com.javier.lagord.trabajofinalcapit.model;

import java.io.Serializable;


public class Curso implements Comparable<Curso>, Serializable{
	private static final long serialVersionUID = 1L;
	private String nombre;
	private String imagen;
	private String descripcion;
	private String carga_horaria;
	private String comienzo;
	private String modalidad;
	private String dias_horarios;
	
	public Curso() {}
	
	public Curso(String nombre, String imagen, String descripcion, String carga_horaria, String comienzo, String modalidad,	String dias_horarios) {
		this.nombre = nombre;
		this.imagen = imagen;
		this.descripcion = descripcion;
		this.carga_horaria = carga_horaria;
		this.comienzo = comienzo;
		this.modalidad = modalidad;
		this.dias_horarios = dias_horarios;
	}
	
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getImagen() {
		return imagen;
	}
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getCarga_horaria() {
		return carga_horaria;
	}
	public void setCarga_horaria(String carga_horaria) {
		this.carga_horaria = carga_horaria;
	}
	public String getComienzo() {
		return comienzo;
	}
	public void setComienzo(String comienzo) {
		this.comienzo = comienzo;
	}
	public String getModalidad() {
		return modalidad;
	}
	public void setModalidad(String modalidad) {
		this.modalidad = modalidad;
	}
	public String getDias_horarios() {
		return dias_horarios;
	}
	public void setDias_horarios(String dias_horarios) {
		this.dias_horarios = dias_horarios;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Curso other = (Curso) obj;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Curso [nombre=" + nombre + ", descripcion=" + descripcion
				+ ", modalidad=" + modalidad + ", comienzo=" + comienzo
				+ ", carga_horaria=" + carga_horaria + ", dias_horarios="
				+ dias_horarios + ", imagen=" + imagen + "]";
	}
	@Override
	public int compareTo(Curso another) {
		return this.nombre.compareTo(another.getNombre());
	}
}
