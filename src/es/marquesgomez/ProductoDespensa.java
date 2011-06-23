package es.marquesgomez;

public class ProductoDespensa {
	
	private int idDespensa;
	private long idProducto;
	private String nombre;
	private int stock;
	private int stockMin;
	private int cantidadAComprar;
	private int cantidadComprada;
	private int enListaCompra;
	private int comprado;
	private String codigoBarras;
	private int multiplicador;
	
	
	public ProductoDespensa() {
		// TODO Auto-generated constructor stub
	}

	public ProductoDespensa(int idDespensa, long idProducto, String nombre, int stock, int stockMin, String codBarras){
		this.idDespensa= idDespensa;
		this.idProducto = idProducto;
		this.nombre=nombre;
		this.stock=stock;
		this.stockMin=stockMin;
		this.codigoBarras=codBarras;
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

	public int getCantidadAComprar() {
		return cantidadAComprar;
	}

	public void setCantidadAComprar(int cantidadAComprar) {
		this.cantidadAComprar = cantidadAComprar;
	}

	public int getCantidadComprada() {
		return cantidadComprada;
	}

	public void setCantidadComprada(int cantidadComprada) {
		this.cantidadComprada = cantidadComprada;
	}

	public boolean getEnListaCompra() {
		if (enListaCompra>0)
			return true;
		else
			return false;
	}

	public void setEnListaCompra(boolean enListaCompra) {
		if (enListaCompra)
			this.enListaCompra = 1;
		else
			this.enListaCompra = 0;
	}

	public int getComprado() {
		return comprado;
	}

	public void setComprado(int comprado) {
		if (comprado>0)
			this.comprado = 1;
		else
			this.comprado = 0;
	}

	public int getMultiplicador() {
		return multiplicador;
	}

	public void setMultiplicador(int multiplicador) {
		this.multiplicador = multiplicador;
	}
	
}
