package es.mentor.act8.despensa.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import es.mentor.act8.despensa.App;
import es.mentor.act8.despensa.Constantes;
import es.mentor.act8.despensa.model.Categoria;
import es.mentor.act8.despensa.model.Despensa;
import es.mentor.act8.despensa.model.Producto;
import es.mentor.act8.despensa.model.ProductoDespensa;

public class BaseDeDatos extends SQLiteOpenHelper {

	// Definici�n de las tablas y sus campos.
	private class TablaDespensas {
		static final String tabla = "Despensas";
		static final String id = "_id";
		static final String nombre = "nombre";

	}

	private class TablaProductos {
		static final String tabla = "Productos";
		static final String id = "_id";
		static final String nombre = "nombre";
		static final String codBarras = "codbarras";
		static final String notas = "notas";
		static final String idCategoria = "idCategoria";
		static final String aGranell = "aGranell";
		static final String multiplicador = "multipicador";
		
	}

	private class TablaContenido {
		static final String tabla = "Contenido";
		static final String idDespensa = "idDespensa";
		static final String idProducto = "idProducto";
		static final String stock = "stock";
		static final String stockMin = "stockMin";
		static final String notas = "notas";
		static final String cantidadAComprar = "cantidadAComprar";
		static final String cantidadComprada = "cantidadComprada";
		static final String enListaCompra = "enListaCompra";
		static final String comprado = "comprado";
	}

	private class TablaCategorias {
		static final String tabla = "Categorias";
		static final String id = "_id";
		static final String nombre = "nombre";
	}

//	private class TablaListaCompra {
//		static final String tabla = "ListaCompra";
//		static final String idDespensa = "idDespensa";
//		static final String idProducto = "idProducto";
//		static final String cantidadAComprar = "cantidadAComprar";
//		static final String cantidadComprada = "cantidadComprada";
//		static final String notas = "notas";
//	}

	// Sentencias SQL para crear las tablas de la BD
	String sqlCreate_Despensas = " CREATE TABLE IF NOT EXISTS "
			+ TablaDespensas.tabla + " (" + TablaDespensas.id
			+ " INTEGER CONSTRAINT PK_Despensas PRIMARY KEY AUTOINCREMENT, "
			+ TablaDespensas.nombre
			+ " TEXT CONSTRAINT UN_Despensas_nom UNIQUE);";
	String sqlCreate_Productos = " CREATE TABLE IF NOT EXISTS "+ TablaProductos.tabla + " (" 
			+ TablaProductos.id	+ " INTEGER CONSTRAINT PK_Productos PRIMARY KEY AUTOINCREMENT, "
			+ TablaProductos.nombre + " TEXT NOT NULL, "
			+ TablaProductos.codBarras + " TEXT CONSTRAINT UN_Productos_codb UNIQUE, "
			+ TablaProductos.idCategoria + " INTEGER, " 
			+ TablaProductos.notas + " TEXT, " 
			+ TablaProductos.aGranell + " INTEGER DEFAULT 0, "
			+ TablaProductos.multiplicador + " INTEGER DEFAULT 1, "
			+ "CONSTRAINT UN_Productos_nom UNIQUE (" + TablaProductos.nombre+ "), " 
			+ "FOREIGN KEY (" + TablaProductos.idCategoria+ ") REFERENCES " + TablaCategorias.tabla + " ON DELETE SET NULL);";
	String sqlCreate_Contenido = " CREATE TABLE IF NOT EXISTS "+ TablaContenido.tabla + " (" 
			+ TablaContenido.idDespensa+ " INTEGER NOT NULL, "
			+ TablaContenido.idProducto+ " INTEGER NOT NULL, " 
			+ TablaContenido.stock + " INTEGER DEFAULT 0, " 
			+ TablaContenido.stockMin + " INTEGER DEFAULT 0, " 
			+ TablaContenido.notas + " TEXT, "
			+ TablaContenido.cantidadAComprar+ " INTEGER DEFAULT 0, "
			+ TablaContenido.cantidadComprada+ " INTEGER DEFAULT 0, "
			+ TablaContenido.enListaCompra+ " INTEGER DEFAULT 0, "
			+ TablaContenido.comprado+ " INTEGER DEFAULT 0, "
			+ "PRIMARY KEY (" + TablaContenido.idDespensa + ", "+ TablaContenido.idProducto + "), " 
			+ "FOREIGN KEY ("+ TablaContenido.idDespensa + ") REFERENCES "+ TablaDespensas.tabla + " ON DELETE CASCADE, " 
			+ "FOREIGN KEY ("+ TablaContenido.idProducto + ") REFERENCES "+ TablaProductos.tabla + " ON DELETE CASCADE);";
	String sqlCreate_Categorias = " CREATE TABLE IF NOT EXISTS "
			+ TablaCategorias.tabla + " (" + TablaCategorias.id
			+ " INTEGER CONSTRAINT PK_Categorias PRIMARY KEY AUTOINCREMENT, "
			+ TablaCategorias.nombre
			+ " TEXT CONSTRAINT UN_Categorias_nom UNIQUE);";
//	String sqlCreate_ListaCompra = " CREATE TABLE IF NOT EXISTS "
//			+ TablaListaCompra.tabla + " (" + TablaListaCompra.idDespensa
//			+ " INTEGER, " + TablaListaCompra.idProducto
//			+ " INTEGER NOT NULL, " + TablaListaCompra.cantidadAComprar
//			+ " INTEGER, " + TablaListaCompra.cantidadComprada + " INTEGER, "
//			+ TablaListaCompra.notas + " TEXT, " + "PRIMARY KEY ("
//			+ TablaListaCompra.idDespensa + ", " + TablaListaCompra.idProducto
//			+ ") " + "FOREIGN KEY (" + TablaListaCompra.idDespensa
//			+ ") REFERENCES " + TablaDespensas.tabla + " ON DELETE CASCADE, "
//			+ "FOREIGN KEY (" + TablaListaCompra.idProducto + ") REFERENCES "
//			+ TablaProductos.tabla + " ON DELETE CASCADE);";



	String sqlCreate = sqlCreate_Despensas + sqlCreate_Categorias;

	/**
	 * Constructor
	 * 
	 * @param contexto
	 * @param nombre
	 * @param factory
	 * @param version
	 */
	public BaseDeDatos(Context contexto) {
		super(contexto, Constantes.BD_NAME, null, Constantes.BD_VERSION);
	}
	

	@Override
	public void onCreate(SQLiteDatabase db) {
		// Se ejecuta la sentencia SQL de creación de la tabla
		db.execSQL(sqlCreate_Despensas);
		db.execSQL(sqlCreate_Categorias);
		db.execSQL(sqlCreate_Productos);
		db.execSQL(sqlCreate_Contenido);
//		db.execSQL(sqlCreate_ListaCompra);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int versionAnterior,
			int versionNueva) {

		// NOTA: Por simplicidad del ejemplo aquí utilizamos directamente la
		// opción de
		// eliminar la tabla anterior y crearla de nuevo vacía con el nuevo
		// formato.
		// Sin embargo lo normal será que haya que migrar datos de la tabla
		// antigua
		// a la nueva, por lo que este método debería ser más elaborado.

		// Se elimina la versión anterior de la tabla
		// db.execSQL("DROP TABLE IF EXISTS Despensas");
		// Se crea la nueva versión de la tabla
		// db.execSQL(sqlCreate);
	}

	@Override
	  public void onOpen(SQLiteDatabase db)
	  {
	    super.onOpen(db);
	    if (!db.isReadOnly())
	    {
	      // Clausula para poder utilizar las FOREIGN KEYs
	      // Enable foreign key constraints
	      db.execSQL("PRAGMA foreign_keys=ON;");
	    }
	  }

	/**
	 * getDespensas
	 * 
	 * @return Despensa[]
	 */
	public ArrayList<Despensa> getDespensas() {
		ArrayList<Despensa> despensas = new ArrayList<Despensa>();

		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null) {
			Cursor c = db.rawQuery(" SELECT * FROM " + TablaDespensas.tabla
					+ " ORDER BY " + TablaDespensas.nombre, null);

			// Nos aseguramos de que existe al menos un registro
			if (c.moveToFirst()) {
				// Recorremos el cursor hasta que no haya más registros

				
				do {
					despensas.add(new Despensa(c.getString(1), c.getInt(0)));
				} while (c.moveToNext());

			} 
			
			if (c != null && !c.isClosed()) {
				c.close();
			}
		} 
		
		
		// Cerramos la base de datos
		db.close();
		return despensas;

	} // Fin getDespensas

	/**
	 * añadirDespensa
	 * 
	 * @param desp
	 * @return resultado
	 */
	public long addDespensa(String desp) {

		long result = 0;

		SQLiteDatabase db = this.getWritableDatabase();
		// Si hemos abierto correctamente la base de datos
		if (db != null) {

			ContentValues nuevoRegistro = new ContentValues();

			nuevoRegistro.put(TablaDespensas.nombre, desp);

			// Insertamos el registro en la base de datos
			result = db.insert(TablaDespensas.tabla, null, nuevoRegistro);
		}

		// Cerramos la base de datos
		db.close();
		return result;

	} // Fin añadirDespensa

	/**
	 * existeDespensa
	 * 
	 * @param desp
	 * @return resultado
	 */
	public boolean existeDespensa(String desp) {
		boolean result = false;

		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null) {
			Cursor c = db.rawQuery(" SELECT * FROM " + TablaDespensas.tabla
					+ " WHERE " + TablaDespensas.nombre + "='" + desp + "'",
					null);

			// Nos aseguramos de que existe al menos un registro
			if (c.moveToFirst()) {

				result = true;

			}

		}
		// Cerramos la base de datos
		// db.close();
		return result;

	} // Fin existeDespensa

	/**
	 * eliminarDespensa
	 * 
	 * @param desp
	 * @return resultado
	 */
	public boolean eliminarDespensa(Despensa desp) {
		boolean result = false;
		SQLiteDatabase db = this.getWritableDatabase();
		// Si hemos abierto correctamente la base de datos
		if (db != null) {
			db.execSQL("DELETE FROM " + TablaDespensas.tabla + " WHERE "
					+ TablaDespensas.id + "='" + desp.getId() + "' ");

			result = true;
		}
		return result;

	} // Fin eliminarDespensa

	/**
	 * buscarCodBarrasDespensa: Busca la existencia de un producto en la despensa
	 * 
	 * @param codBarras
	 * @param despensa
	 * @return
	 */
	private boolean buscarCodBarrasDespensa(String codBarras, int despensa) {
		boolean result = false;

		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null) {
			Cursor c = db.rawQuery(" SELECT * FROM " + TablaContenido.tabla
					+ " WHERE " + TablaContenido.idDespensa + "= " + despensa
					+ " AND " + TablaContenido.idProducto + "=(SELECT "
					+ TablaProductos.id + " FROM " + TablaProductos.tabla
					+ " WHERE " + TablaProductos.codBarras + " = '" + codBarras
					+ "' );", null);

			// Nos aseguramos de que existe al menos un registro
			if (c.moveToFirst()) {

				result = true;

			}

		}
		return result;
	}

	/**
	 * buscarCodBarrasProducto: Busca la existencia de un producto en los
	 * productos
	 * 
	 * @param codBarras
	 * @return
	 */
	private boolean buscarCodBarrasProducto(String codBarras) {
		boolean result = false;

		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null) {
			
			Cursor c = db.rawQuery(" SELECT * FROM " + TablaProductos.tabla
					+ " WHERE " + TablaProductos.codBarras + "='" + codBarras
					+ "'", null);
			
			// Nos aseguramos de que existe al menos un registro
			if (c.moveToFirst()) {
				result = true;
			}

		}
		return result;
	}

	/**
	 * buscarCodBarras: Busca si existe el producto en la tabla seleccionada
	 * 
	 * @param codBarras
	 * @param despensa
	 * @return resultado
	 */
	public int buscarCodBarras(String codBarras, int despensa) {
		int result = 0;

		if (buscarCodBarrasDespensa(codBarras, despensa)) {
			result = Constantes.ESTADO_BARCODE_DESPENSA;
		} else if (buscarCodBarrasProducto(codBarras)) {
			result = Constantes.ESTADO_BARCODE_PRODUCTOS;
		} else {
			result = Constantes.ESTADO_BARCODE_NO_EXISTE;
		}

		return result;
	}

	/**
	 * detIdDespensa: Retorna el _id de la despensa
	 * 
	 * @param desp
	 * @return
	 */
	public int getIdDespensa(String desp) {
		int result = 0;

		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null) {
			// Log.d(Constantes.LOG_TAG, "Dentro getIdDespensa");
			Cursor c = db.rawQuery(" SELECT " + TablaDespensas.id + " FROM "
					+ TablaDespensas.tabla + " WHERE " + TablaDespensas.nombre
					+ "='" + desp + "'", null);
			// Log.d(Constantes.LOG_TAG, "Despues SELECT getIdDespensa");

			// Nos aseguramos de que existe al menos un registro
			if (c.moveToFirst()) {

				result = c.getInt(0);

			}

		}
		// Cerramos la base de datos
		 db.close();
		return result;
	}

	/**
	 * 
	 * @param nombre
	 * @param codigoBarras
	 * @param categoria
	 * @param notas
	 * @return long
	 */
	public long insertarProducto(Producto producto) {
		long result = 0;

		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null) {

			ContentValues nuevoRegistro = new ContentValues();

			nuevoRegistro.put(TablaProductos.nombre, producto.getNombre());
			if (producto.getCodigoBarras().trim().length() != 0)
				nuevoRegistro.put(TablaProductos.codBarras,
						producto.getCodigoBarras());

//			nuevoRegistro.put(TablaProductos.idCategoria,
//					producto.getIdCategoria());

			nuevoRegistro.put(TablaProductos.notas, producto.getNotas());

			// Insertamos el registro en la base de datos
			result = db.insert(TablaProductos.tabla, null, nuevoRegistro);

		}

		return result;
	}

	public boolean insertarProductoADespensa(ProductoDespensa producto) {
		
		boolean result = false;

		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null) {

			ContentValues nuevoRegistro = new ContentValues();

			nuevoRegistro.put(TablaContenido.idDespensa, producto.getIdDespensa());
			nuevoRegistro.put(TablaContenido.idProducto, producto.getIdProducto());
			nuevoRegistro.put(TablaContenido.stock, producto.getStock());
			nuevoRegistro.put(TablaContenido.stockMin, producto.getStockMin());
			nuevoRegistro.put(TablaContenido.cantidadAComprar, producto.getCantidadAComprar());

			// Insertamos el registro en la base de datos
			long code = db.insert(TablaContenido.tabla, null, nuevoRegistro);

			if (code > 0)
				result = true;
		}

		db.close();
		return result;
	}

	public long getIdProductoXBarCode(String codeBar) {
		long result = 0;

		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null) {
			String[] campos = new String[] { TablaProductos.id };
			String[] args = new String[] { codeBar };

			Cursor c = db.query(TablaProductos.tabla, campos,TablaProductos.codBarras + "=?", args, null, null, null);

			// Nos aseguramos de que existe al menos un registro
			if (c.moveToFirst()) {
				// Recorremos el cursor hasta que no haya más registros
				do {
					result = c.getLong(0);
				} while (c.moveToNext());
			}

		}
		db.close();
		return result;
	}

	public boolean actualizarStockXCode(String codBarras, int despensa,
			int cantidad) {
		boolean result = false;

		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null) {

			db.execSQL(" UPDATE " + TablaContenido.tabla + " SET "
					+ TablaContenido.stock + "= " + TablaContenido.stock + "+"
					+ cantidad + " WHERE " + TablaContenido.idDespensa + "= "
					+ despensa + " AND " + TablaContenido.idProducto
					+ "=(SELECT " + TablaProductos.id + " FROM "
					+ TablaProductos.tabla + " WHERE "
					+ TablaProductos.codBarras + " = '" + codBarras + "' );");

			result = true;
		}
		// Cerramos la base de datos
		db.close();
		return result;
	}

	public boolean actualizarStockXidProducto(long producto, int despensa,
			int cantidad) {
		boolean result = false;

		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null) {

			db.execSQL(" UPDATE " + TablaContenido.tabla + " SET "
					+ TablaContenido.stock + "= " + TablaContenido.stock + "+"
					+ cantidad + " WHERE " + TablaContenido.idDespensa + "= "
					+ despensa + " AND " + TablaContenido.idProducto + "="
					+ producto + ";");

			result = true;
		}
		// Cerramos la base de datos
		db.close();
		return result;
	}
	
	public boolean actualizarCantidadCompradaXidProducto(long producto, int despensa,
			int cantidad) {
		boolean result = false;

		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null) {

			db.execSQL(" UPDATE " + TablaContenido.tabla + " SET "
					+ TablaContenido.cantidadComprada + "= " + TablaContenido.cantidadComprada + "+"+ cantidad 
					+ " WHERE " + TablaContenido.idDespensa + "= "+ despensa 
					+ " AND " + TablaContenido.idProducto + "="+ producto + ";");

			result = true;
		}
		// Cerramos la base de datos
		db.close();
		return result;
	}

	public ArrayList<ProductoDespensa> getProductosDespensa(int idDespensa) {
		ArrayList<ProductoDespensa> listaProductos = new ArrayList<ProductoDespensa>();

		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null) {
			Cursor c = db.rawQuery(" SELECT " + TablaProductos.id + ", "
					+ TablaProductos.nombre + ", " + TablaContenido.stock
					+ ", " + TablaContenido.stockMin + ", "
					+ TablaProductos.codBarras + " FROM "
					+ TablaContenido.tabla + " JOIN " + TablaProductos.tabla
					+ "" + " ON " + TablaContenido.idProducto + "="
					+ TablaProductos.id + " WHERE " + TablaContenido.idDespensa
					+ "= " + idDespensa, null);

			// Nos aseguramos de que existe al menos un registro
			if (c.moveToFirst()) {
				// Recorremos el cursor hasta que no haya más registros
				do {
					listaProductos.add(new ProductoDespensa(
							App.despensaSelec.getId(), c.getLong(0),
							c.getString(1), c.getInt(2), c.getInt(3),
							c.getString(4)));
				} while (c.moveToNext());

			} 

			if (c != null && !c.isClosed())
				c.close();
		} 

		// Cerramos la base de datos
		db.close();

		return listaProductos;
	}
	
	public ProductoDespensa getProductoDespensa(long idProducto) {
		ProductoDespensa producto = new ProductoDespensa();

		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null) {
			Cursor c = db.rawQuery(" SELECT " + TablaProductos.id + ", "
					+ TablaProductos.nombre + ", " 
					+ TablaContenido.stock	+ ", " 
					+ TablaContenido.stockMin + ", "
					+ TablaProductos.codBarras + ", "
					+ TablaContenido.cantidadAComprar
					+ " FROM " + TablaContenido.tabla + " JOIN " + TablaProductos.tabla
					+ " ON " + TablaContenido.idProducto + "=" + TablaProductos.id 
					+ " WHERE " + TablaContenido.idDespensa	+ "= " + App.despensaSelec.getId()
					+ " AND "+ TablaContenido.idProducto +"="+idProducto, null);

			// Nos aseguramos de que existe al menos un registro
			if (c.moveToFirst()) {
				// Recorremos el cursor hasta que no haya más registros

				do {
					producto = new ProductoDespensa(
							App.despensaSelec.getId(), c.getLong(0),
							c.getString(1), c.getInt(2), c.getInt(3),
							c.getString(4));
					producto.setCantidadAComprar(c.getInt(5));
				} while (c.moveToNext());

			}

			if (c != null && !c.isClosed()) {
				c.close();
			}

		} 

		// Cerramos la base de datos
		db.close();

		return producto;
	}

	/**
	 * getProductos
	 * 
	 * @return Productos[]
	 */
	public ArrayList<Producto> getProductos() {
		ArrayList<Producto> productos = new ArrayList<Producto>();

		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null) {
			Cursor c = db.rawQuery(" SELECT " + TablaProductos.tabla + "."
					+ TablaProductos.id + ", " + TablaProductos.tabla + "."
					+ TablaProductos.nombre + ", " + TablaProductos.codBarras
					+ ", " + TablaProductos.notas + ", "
					+ TablaProductos.idCategoria + ", " + TablaCategorias.tabla
					+ "." + TablaCategorias.nombre + " FROM "
					+ TablaProductos.tabla + " LEFT JOIN "
					+ TablaCategorias.tabla + " " + " ON "
					+ TablaProductos.tabla + "." + TablaProductos.idCategoria
					+ "=" + TablaCategorias.tabla + "." + TablaCategorias.id
					+ " ORDER BY " + TablaProductos.tabla + "."
					+ TablaDespensas.nombre, null);

			// Nos aseguramos de que existe al menos un registro
			if (c.moveToFirst()) {
				// Recorremos el cursor hasta que no haya más registros

				do {
					productos.add(new Producto(c.getLong(0), c.getString(1),
							c.getString(2), c.getString(3), c.getInt(4),
							c.getString(5)));
				} while (c.moveToNext());
			}
		}
		// Cerramos la base de datos
		db.close();
		return productos;

	} // Fin getProductos

	public ArrayList<ProductoDespensa> getProductosCompra(int idDespensa) {
		
		ArrayList<ProductoDespensa> listaProductos = new ArrayList<ProductoDespensa>();

		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null) {
			Cursor c = db.rawQuery(" SELECT " 
					+ TablaProductos.id + ", "
					+ TablaProductos.nombre + ", " 
					+ TablaContenido.cantidadAComprar + ", " 
					+ TablaContenido.cantidadComprada + ", "
					+ TablaProductos.codBarras + ", "
					+ TablaProductos.multiplicador + ", "
					+ TablaContenido.comprado
					+ " FROM "+ TablaContenido.tabla + " JOIN " + TablaProductos.tabla
					+ " ON " + TablaContenido.idProducto + "=" + TablaProductos.id 
					+ " WHERE " + TablaContenido.idDespensa	+ "= " + idDespensa
					+ " AND "+TablaContenido.enListaCompra+" > 0;", null);

			// Nos aseguramos de que existe al menos un registro
			if (c.moveToFirst()) {
				// Recorremos el cursor hasta que no haya más registros
				ProductoDespensa producto;
				do {
					producto = new ProductoDespensa();
					producto.setIdDespensa(App.despensaSelec.getId());
					producto.setIdProducto(c.getLong(0));
					producto.setNombre(c.getString(1));
					producto.setCantidadAComprar(c.getInt(2));
					producto.setCantidadComprada(c.getInt(3));
					producto.setCodigoBarras(c.getString(4));
					producto.setMultiplicador(c.getInt(5));
					producto.setComprado(c.getInt(6));

					listaProductos.add(producto);

				} while (c.moveToNext());

			} 

			if (c != null && !c.isClosed()) {
				c.close();
			}

		} 

		// Cerramos la base de datos
		db.close();

		return listaProductos;
	}

	public boolean generarListaCompra(int idDespensa) {
		boolean result = false;
		SQLiteDatabase db = this.getWritableDatabase();
		// Si hemos abierto correctamente la base de datos
		if (db != null) {

//			String sql1 = "INSERT INTO ListaCompra (idDespensa,idProducto,cantidadAComprar) "
//					+ "SELECT idDespensa,idProducto,stock "
//					+ "FROM Contenido "
//					+ "WHERE idDespensa="
//					+ idDespensa
//					+ " AND stock<=stockMin AND "
//					+ "idProducto NOT IN (SELECT c.idProducto "
//					+ "FROM Contenido as c JOIN ListaCompra as lc "
//					+ "ON c.idDespensa=lc.idDespensa AND c.idProducto=lc.idProducto "
//					+ "WHERE c.idDespensa=" + idDespensa + ");";
			
			String sql1 = "UPDATE "+TablaContenido.tabla+" SET "+TablaContenido.enListaCompra+"=1, "
				+ TablaContenido.cantidadComprada+"="+TablaContenido.cantidadAComprar
				+ " WHERE "+TablaContenido.idDespensa+"="+ idDespensa
				+ " AND "+TablaContenido.stock+"<="+TablaContenido.stockMin+";";
			
			try {
				db.execSQL(sql1);
				result = true;
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		return result;
	}
	
	public boolean finalizarCompra(int idDespensa){
		//TODO: Implementar finalizar compra
		boolean result=false;
		
		SQLiteDatabase db = this.getWritableDatabase();
		// Si hemos abierto correctamente la base de datos
		if (db != null) {
			
			String sql1 = "UPDATE "+TablaContenido.tabla+" SET "
							+ TablaContenido.stock+"="+TablaContenido.stock+"+"+TablaContenido.cantidadComprada+", "
							+ TablaContenido.enListaCompra+"=0, "
							+ TablaContenido.comprado+"=0 "
							+ " WHERE "+TablaContenido.idDespensa+"="+ idDespensa
							+ " AND "+TablaContenido.comprado+"=1;";
			
			try {
				db.execSQL(sql1);
				result = true;
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		
		return result;
		
		
	}
	
	public boolean productoComprado(ProductoDespensa producto){
		//TODO: implementar el marcado de un producto comprado
		boolean result=false;
		
		SQLiteDatabase db = this.getWritableDatabase();
		// Si hemos abierto correctamente la base de datos
		if (db != null) {
			
			String sql1 = "UPDATE "+TablaContenido.tabla+" SET "+TablaContenido.comprado+"="+producto.getComprado()
				+ " WHERE "+TablaContenido.idDespensa+"="+ producto.getIdDespensa()
				+ " AND "+TablaContenido.idProducto+"="+producto.getIdProducto()+";";
			
			try {
				db.execSQL(sql1);
				result = true;
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		
		return result;
	}

	public boolean updateProducto(Producto producto) {
		boolean result = false;
		SQLiteDatabase db = this.getWritableDatabase();
		if (db != null) {
			
			String sql1=" UPDATE "+TablaProductos.tabla+" SET "
						+ TablaProductos.nombre+"='"+producto.getNombre()+"', "
						+ TablaProductos.codBarras+"="+producto.getCodigoBarras()
						+ " WHERE "+TablaProductos.id+"="+producto.getId()+";";
			
			try {
				db.execSQL(sql1);
				result = true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public boolean updateProductoDespensa(ProductoDespensa producto) {
		boolean result = false;
		
		SQLiteDatabase db = this.getWritableDatabase();
		if (db != null) {
			
			String sql1=" UPDATE "+TablaContenido.tabla+" SET "
						+ TablaContenido.stock+"="+producto.getStock()+", "
						+ TablaContenido.stockMin+"="+producto.getStockMin()+", "
						+ TablaContenido.cantidadAComprar+"="+producto.getCantidadAComprar()
						+ " WHERE "+TablaContenido.idProducto+"="+producto.getIdProducto()
						+ " AND "+TablaContenido.idDespensa+"="+producto.getIdDespensa()+";";
			
			try {
				db.execSQL(sql1);
				result = true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	public boolean eliminarProducto(Producto producto) {
		boolean result = false;

		SQLiteDatabase db = this.getWritableDatabase();
		// Si hemos abierto correctamente la base de datos
		if (db != null) {

			String sql1 = "DELETE FROM " + TablaProductos.tabla + " WHERE "
					+ TablaProductos.id + "=" + producto.getId() + ";";
			try {
				db.execSQL(sql1);
				result = true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	public boolean eliminarProductoDespensa(ProductoDespensa producto) {
		boolean result = false;

		SQLiteDatabase db = this.getWritableDatabase();
		// Si hemos abierto correctamente la base de datos
		if (db != null) {

			String sql1 = "DELETE FROM " + TablaContenido.tabla + " WHERE "
					+ TablaContenido.idDespensa + " = "
					+ producto.getIdDespensa() + " AND " + " "
					+ TablaContenido.idProducto + " = "
					+ producto.getIdProducto() + ";";
			try {
				db.execSQL(sql1);
				result = true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	public boolean eliminarProductoCompra(ProductoDespensa producto) {
		boolean result = false;

		SQLiteDatabase db = this.getWritableDatabase();
		// Si hemos abierto correctamente la base de datos
		if (db != null) {

			String sql1 = "UPDATE " + TablaContenido.tabla + " SET "+TablaContenido.enListaCompra+"= 0"
					+ " WHERE "	+ TablaContenido.idDespensa + " = "+ producto.getIdDespensa() + " AND " 
					+ TablaContenido.idProducto + " = "+ producto.getIdProducto() + ";";
			try {
				db.execSQL(sql1);
				result = true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return result;
	}
	
	public boolean eliminarProductosCompra() {
		boolean result = false;

		SQLiteDatabase db = this.getWritableDatabase();
		// Si hemos abierto correctamente la base de datos
		if (db != null) {

			String sql1 = "UPDATE " + TablaContenido.tabla + " SET "+TablaContenido.enListaCompra+"= 0"
					+ " WHERE "	+ TablaContenido.idDespensa + " = "+ App.despensaSelec.getId() + ";";
			try {
				db.execSQL(sql1);
				result = true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	public boolean insertarProductoACompra(ProductoDespensa producto) {
		boolean result = false;

		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null) {
			
			String sql1 = "UPDATE " + TablaContenido.tabla + " SET "+TablaContenido.enListaCompra+"= 1, "
						+ TablaContenido.cantidadComprada+"="+TablaContenido.cantidadAComprar
						+ " WHERE "	+ TablaContenido.idDespensa + " = "+ producto.getIdDespensa() + " AND " 
						+ TablaContenido.idProducto + " = "+ producto.getIdProducto() + ";";
			
			try {
				db.execSQL(sql1);
				result = true;
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		db.close();

		return result;
	}

	public boolean duplicarDespensa(Despensa despensaActual,
			Despensa despensaNueva) {
		boolean result = false;

		long idDespensa = addDespensa(despensaNueva.getNombre());
		if (idDespensa > 0) {
			despensaNueva.setId((int) idDespensa);

			SQLiteDatabase db = this.getWritableDatabase();
			// Si hemos abierto correctamente la base de datos
			if (db != null) {
				String sql1 = "CREATE TABLE IF NOT EXISTS tmp_contenido AS "
						+ " SELECT * "
						+ " FROM "+TablaContenido.tabla
						+ " WHERE "+TablaContenido.idDespensa+"="
						+ despensaActual.getId()+"; ";
						
				String sql2 = " UPDATE tmp_contenido SET "+TablaContenido.idDespensa+"="+despensaNueva.getId()+"; ";
				String sql3 = " INSERT INTO "+TablaContenido.tabla +" SELECT * FROM tmp_contenido; ";
				String sql4 = " DROP TABLE tmp_contenido;"; 
				try {
					db.execSQL(sql1);
					db.execSQL(sql2);
					db.execSQL(sql3);
					db.execSQL(sql4);
					result = true;
				} catch (SQLException e) {
					e.printStackTrace();
					result = false;
				}
				
			}
			db.close();
		}

		return result;
	}


	public boolean updateDespensa(Despensa despensa) {
		boolean result=false;
		
		SQLiteDatabase db = this.getWritableDatabase();
		// Si hemos abierto correctamente la base de datos
		if (db != null) {

			String sql1 = "UPDATE " + TablaDespensas.tabla + " SET "
					+ TablaDespensas.nombre + " = '" + despensa.getNombre()+"' " 
					+ " WHERE " + TablaDespensas.id + " = "	+ despensa.getId() + ";";
			try {
				db.execSQL(sql1);
				result = true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
		
		return result;
	}
	
	public boolean insertarCategoria(Categoria cat) {
		boolean result = false;

		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null) {

			ContentValues nuevoRegistro = new ContentValues();

			nuevoRegistro.put(TablaCategorias.nombre,cat.getNombre());

			// Insertamos el registro en la base de datos
			long code = db.insert(TablaCategorias.tabla, null, nuevoRegistro);

			if (code > 0)
				result = true;
		}

		db.close();

		return result;
	}

}
