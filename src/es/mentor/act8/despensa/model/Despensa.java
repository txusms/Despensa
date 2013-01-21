package es.mentor.act8.despensa.model;

public class Despensa {

	private String nombre;
	private int id;
	
	public Despensa() {
		// TODO Auto-generated constructor stub
	}
	
	public Despensa(String nombre, int id){
		this.nombre=nombre;
		this.id=id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String toString(){
		return getNombre();
	}
}
