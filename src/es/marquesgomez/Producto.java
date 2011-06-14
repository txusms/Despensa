package es.marquesgomez;

public class Producto {
	
	private long id;
	private String nombre;
	private String codigoBarras;
	private String notas;
	private int idCategoria;
	private String categoria;

	public Producto(){
		
	}
	
	public Producto(long id, String nombre, String codebar, String notas, int idCat, String cat){
		
		this.id = id;
		this.nombre = nombre;
		this.codigoBarras = codebar;
		this.notas = notas;
		this.idCategoria = idCat;
		this.categoria = cat;
		
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getCodigoBarras() {
		return codigoBarras;
	}
	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
	}
	public String getNotas() {
		return notas;
	}
	public void setNotas(String notas) {
		this.notas = notas;
	}
	

	public int getIdCategoria() {
		return idCategoria;
	}

	public void setIdCategoria(int idCategoria) {
		this.idCategoria = idCategoria;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	
	public String toString(){
		return this.getNombre();
	}

}
