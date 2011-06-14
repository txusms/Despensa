package es.marquesgomez;

public class ProductoDespensa {
	
	private int idDespensa;
	private long idProducto;
	private String nombre;
	private int stock;
	private int stockMin;
	private String codigoBarras;
	
	public ProductoDespensa() {
		// TODO Auto-generated constructor stub
	}
	
	public int getIdDespensa() {
		return idDespensa;
	}

	public void setIdDespensa(int idDespensa) {
		this.idDespensa = idDespensa;
	}

	public long getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(long idProducto) {
		this.idProducto = idProducto;
	}

	public ProductoDespensa(int idDespensa, long idProducto, String nombre, int stock, int stockMin, String codBarras){
		this.idDespensa= idDespensa;
		this.idProducto = idProducto;
		this.nombre=nombre;
		this.stock=stock;
		this.stockMin=stockMin;
		this.codigoBarras=codBarras;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public int getStockMin() {
		return stockMin;
	}

	public void setStockMin(int stockMin) {
		this.stockMin = stockMin;
	}
	
	public String getCodigoBarras() {
		return codigoBarras;
	}

	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
	}
	
	

}
