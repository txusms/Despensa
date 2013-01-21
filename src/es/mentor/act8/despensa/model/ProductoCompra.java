package es.mentor.act8.despensa.model;


public class ProductoCompra extends ProductoDespensa{
	
	public ProductoCompra(){
		super();
	}
	public ProductoCompra(int idDespensa, long idProducto, String nombre, int stock, int stockMin, String codBarras){
		super(idDespensa, idProducto, nombre, stock, stockMin, codBarras);
	}

}
