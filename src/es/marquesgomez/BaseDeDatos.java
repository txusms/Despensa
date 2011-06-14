package es.marquesgomez;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
//import android.widget.Toast;


public class BaseDeDatos extends SQLiteOpenHelper{
	
	//Definición de las tablas y sus campos.
	private class TablaDespensas {
		static final String tabla = "Despensas";
		static final String id = "_id";
		static final String nombre = "nombre";
		
	}
	
	private class TablaProductos{
		static final String tabla = "Productos";
		static final String id = "_id";
		static final String nombre = "nombre";
		static final String codBarras = "codbarras";
		static final String notas = "notas";
		static final String idCategoria = "idCategoria";		
	}
	
	private class TablaContenido{
		static final String tabla = "Contenido";
		static final String idDespensa = "idDespensa";
		static final String idProducto= "idProducto";
		static final String stock = "stock";
		static final String stockMin = "stockMin";
		static final String notas = "notas";
	}
	
	private class TablaCategorias{
		static final String tabla = "Categorias";
		static final String id = "_id";
		static final String nombre= "nombre";
	}
	
	private class TablaListaCompra{
		static final String tabla = "ListaCompra";
		static final String idDespensa = "idDespensa";
		static final String idProducto= "idProducto";
		static final String cantidadAComprar = "cantidadAComprar";
		static final String cantidadComprada = "cantidadComprada";
		static final String notas = "notas";
	}
	
	//Sentencias SQL para crear las tablas de la BD
	String sqlCreate_Despensas = " CREATE TABLE IF NOT EXISTS "+TablaDespensas.tabla+" (" +
												TablaDespensas.id+" INTEGER CONSTRAINT PK_Despensas PRIMARY KEY AUTOINCREMENT, " +
												TablaDespensas.nombre+" TEXT CONSTRAINT UN_Despensas_nom UNIQUE);";
	String sqlCreate_Productos = " CREATE TABLE IF NOT EXISTS "+TablaProductos.tabla+" (" +
												TablaProductos.id+" INTEGER CONSTRAINT PK_Productos PRIMARY KEY AUTOINCREMENT, " +
												TablaProductos.nombre+" TEXT NOT NULL, " +
												TablaProductos.codBarras+" TEXT CONSTRAINT UN_Productos_codb UNIQUE, " +
												TablaProductos.idCategoria+" INTEGER, " +
												TablaProductos.notas+" TEXT, " +
												"CONSTRAINT UN_Productos_nom UNIQUE ("+TablaProductos.nombre+"), " +
												"FOREIGN KEY (idCategoria) REFERENCES "+TablaCategorias.tabla+");";
	String sqlCreate_Contenido = " CREATE TABLE IF NOT EXISTS "+TablaContenido.tabla+" (" +
												TablaContenido.idDespensa+" INTEGER NOT NULL, " +
												TablaContenido.idProducto+" INTEGER NOT NULL, " +
												TablaContenido.stock+" INTEGER DEFAULT 0, " +
												TablaContenido.stockMin+" INTEGER DEFAULT 0, " +
												TablaContenido.notas+" TEXT, " +
												"FOREIGN KEY ("+TablaContenido.idDespensa+") REFERENCES "+TablaDespensas.tabla+", " +
												"FOREIGN KEY ("+TablaContenido.idProducto+") REFERENCES "+TablaProductos.tabla+");";
	String sqlCreate_Categorias = " CREATE TABLE IF NOT EXISTS "+TablaCategorias.tabla+" (" +
												TablaCategorias.id+" INTEGER CONSTRAINT PK_Categorias PRIMARY KEY AUTOINCREMENT, " +
												TablaCategorias.nombre+" TEXT CONSTRAINT UN_Categorias_nom UNIQUE);";
	String sqlCreate_ListaCompra = " CREATE TABLE IF NOT EXISTS "+TablaListaCompra.tabla+" (" +
												TablaListaCompra.idDespensa+" INTEGER, " +
												TablaListaCompra.idProducto+" INTEGER NOT NULL, " +
												TablaListaCompra.cantidadAComprar+" INTEGER, " +
												TablaListaCompra.cantidadComprada+" INTEGER, " +
												TablaListaCompra.notas+" TEXT, " +
												"FOREIGN KEY ("+TablaListaCompra.idDespensa+") REFERENCES "+TablaDespensas.tabla+", " +
												"FOREIGN KEY ("+TablaListaCompra.idProducto+") REFERENCES "+TablaProductos.tabla+");";
	
	String sqlCreate = sqlCreate_Despensas+sqlCreate_Categorias;
	/**
	 * Constructor
	 * 
	 * @param contexto
	 * @param nombre
	 * @param factory
	 * @param version
	 */
	public BaseDeDatos(Context contexto, String nombre,CursorFactory factory, int version) {
	super(contexto, nombre, factory, version);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(Constantes.LOG_TAG, "BaseDeDatos - onCreate()");
		//Se ejecuta la sentencia SQL de creación de la tabla
		db.execSQL(sqlCreate_Despensas);
		db.execSQL(sqlCreate_Categorias);
		db.execSQL(sqlCreate_Productos);
		db.execSQL(sqlCreate_Contenido);
		db.execSQL(sqlCreate_ListaCompra);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
		Log.d(Constantes.LOG_TAG, "BaseDeDatos - onUpgrade()");
	
//		NOTA: Por simplicidad del ejemplo aquí utilizamos directamente la opción de 
//		eliminar la tabla anterior y crearla de nuevo vacía con el nuevo formato. 
//		Sin embargo lo normal será que haya que migrar datos de la tabla antigua 
//		a la nueva, por lo que este método debería ser más elaborado.
		
		//Se elimina la versión anterior de la tabla
//		db.execSQL("DROP TABLE IF EXISTS Despensas");
		//Se crea la nueva versión de la tabla
//		db.execSQL(sqlCreate);
	}
	
	/**
	 * getDespensas
	 * 
	 * @return String[]
	 */
	public String[] getDespensasStr(){
		Log.d(Constantes.LOG_TAG,"getDespensas()");
		//private String[] despensas;
		String despensas[];
		
		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null){
			Log.d(Constantes.LOG_TAG, "getDespensas() - Antes del SELECT");
			Cursor c = db.rawQuery(" SELECT * FROM "+TablaDespensas.tabla+" ORDER BY "+TablaDespensas.nombre,null);
			Log.d(Constantes.LOG_TAG, "getDespensas() - Despues del SELECT");
			
			//Nos aseguramos de que existe al menos un registro
			if (c.moveToFirst()) {
				Log.d(Constantes.LOG_TAG, "getDespensas() - Dentro movefirst");
				//Recorremos el cursor hasta que no haya más registros
				
				despensas = new String[c.getCount()]; 
				int i=0;
				
				do {
					Log.d(Constantes.LOG_TAG, "getDespensas() - Dentro do-while "+i);
					
					despensas[i]= c.getString(1);				
					i++;

				} while(c.moveToNext());
				
			} else {
				despensas = new String[] {""};
			}
			
			
		} else {
			despensas = new String[] {""};
		}
		//Cerramos la base de datos
		db.close();
		Log.d(Constantes.LOG_TAG, "getIdDespensa() - Return: "+despensas.toString());
		return despensas;
		
	} //Fin getDespensas
	
	/**
	 * getDespensas
	 * 
	 * @return Despensa[]
	 */
	public Despensa[] getDespensas(){
		Log.d(Constantes.LOG_TAG,"getDespensas()");
		//private String[] despensas;
		Despensa despensas[];
		
		Despensa despensaVacio = new Despensa("", 0);
		
		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null){
			Log.d(Constantes.LOG_TAG, "getDespensas() - Antes del SELECT");
			Cursor c = db.rawQuery(" SELECT * FROM "+TablaDespensas.tabla+" ORDER BY "+TablaDespensas.nombre,null);
			Log.d(Constantes.LOG_TAG, "getDespensas() - Despues del SELECT");
			
			//Nos aseguramos de que existe al menos un registro
			if (c.moveToFirst()) {
				Log.d(Constantes.LOG_TAG, "getDespensas() - Dentro movefirst");
				//Recorremos el cursor hasta que no haya más registros
				
				despensas = new Despensa[c.getCount()]; 
				int i=0;
				
				do {
					Log.d(Constantes.LOG_TAG, "getDespensas() - Dentro do-while "+i);
					
					despensas[i]= new Despensa(c.getString(1), c.getInt(0));				
					i++;

				} while(c.moveToNext());
				
			} else {
				despensas = new Despensa[] {despensaVacio};
			}
			
			
		} else {
			despensas = new Despensa[] {despensaVacio};
		}
		//Cerramos la base de datos
		db.close();
		Log.d(Constantes.LOG_TAG, "getDespensa() - Return: "+despensas.toString());
		return despensas;
		
	} //Fin getDespensas
	
	/**
	 * añadirDespensa
	 * 
	 * @param desp
	 * @return resultado
	 */
	public boolean añadirDespensa(String desp){
		Log.d(Constantes.LOG_TAG,"añadirDespensas() - Despensa: "+desp);
		
		boolean result = false;
		
		SQLiteDatabase db = this.getWritableDatabase();
		//Si hemos abierto correctamente la base de datos
		if(db != null)
		{
			
			
			if (!existeDespensa(desp)){
				
				Log.d(Constantes.LOG_TAG, "añadirDespensas() - Antes del insert");
				//Insertamos los datos en la tabla Despensas
				db.execSQL("INSERT INTO "+TablaDespensas.tabla+" ("+TablaDespensas.nombre+") VALUES ('" + desp +"')");
				Log.d(Constantes.LOG_TAG, "añadirDespensas() - Despues del insert");
				
				result=true;
			} else {
				Log.d(Constantes.LOG_TAG, "añadirDespensas() - La despensa ya existe");
				
			}
		}
		
		//Cerramos la base de datos
		db.close();
		Log.d(Constantes.LOG_TAG, "añadirDespensas() - Return: "+result);
		return result;
		
	} //Fin añadirDespensa
	
	/**
	 * existeDespensa
	 * 
	 * @param desp
	 * @return resultado
	 */
	public boolean existeDespensa(String desp){
		Log.d(Constantes.LOG_TAG, "existeDespensa() - Despensa: "+desp);
		boolean result = false;
		
		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null){
			Log.d(Constantes.LOG_TAG, "existeDespensa() - Antes del SELECT");
			Cursor c = db.rawQuery(" SELECT * FROM "+TablaDespensas.tabla+" WHERE "+TablaDespensas.nombre+"='"+desp+"'",null);
			Log.d(Constantes.LOG_TAG, "existeDespensa() - Despues del SELECT");
			
			//Nos aseguramos de que existe al menos un registro
			if (c.moveToFirst()) {
				
				Log.d(Constantes.LOG_TAG, "existeDespensa() - Existe una");
				result = true;
				
			}
			
			
		}
		//Cerramos la base de datos
		//db.close();
		Log.d(Constantes.LOG_TAG, "existeDespensa() - Return: "+result);				
		return result;

	} //Fin existeDespensa
	
	/**
	 * eliminarDespensa
	 * 
	 * @param desp
	 * @return resultado
	 */
	public boolean eliminarDespensa(String desp){
		Log.d(Constantes.LOG_TAG, "eliminarDespensa() - Despensa: "+desp);
		boolean result = false;
		SQLiteDatabase db = this.getWritableDatabase();
		//Si hemos abierto correctamente la base de datos
		if(db != null){
			db.execSQL("DELETE FROM "+TablaDespensas.tabla+" WHERE "+TablaDespensas.nombre+"='"+desp+"' ");
			
			result = true;
		}
		Log.d(Constantes.LOG_TAG, "eliminarDespensa() - Return: "+result);
		return result;
		
	} //Fin eliminarDespensa
	
	/**
	 * getContenidoDespensa: Devuelve el contenido de la despensa visualizada
	 * 
	 * @param idDespensa
	 * 
	 */
	public void getContenidoDespensa(int idDespensa){
		
	}
	
	/**
	 * getDetalleProductoDespensa: Devuelve el detalle del producto de la despensa
	 * 
	 * @param idDespensa
	 * @param idProducto
	 * 
	 * @return Detalle producto
	 */
	public void getDetalleProductoDespensa(int idDespensa, int idProducto){
		
	}
	
	/**
	 * buscarCodBarrasTabla: Busca la existencia de un producto en la despensa
	 * 
	 * @param codBarras
	 * @param despensa
	 * @return
	 */
	private boolean buscarCodBarrasDespensa(String codBarras, int despensa){
		Log.d(Constantes.LOG_TAG, "buscarCodBarrasDespensa() - codBarras: "+codBarras+" Despensa: "+despensa);
		boolean result=false;
		
		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null){
			Log.d(Constantes.LOG_TAG, "Dentro buscarCodBarrasDespensa");
			Cursor c = db.rawQuery(" SELECT * FROM "+TablaContenido.tabla+" WHERE "+TablaContenido.idDespensa+"= "+despensa+" AND " +
																TablaContenido.idProducto+"=(SELECT "+TablaProductos.id+" FROM "+ TablaProductos.tabla +
																			" WHERE "+TablaProductos.codBarras+" = '"+codBarras+"' );",null);
			Log.d(Constantes.LOG_TAG, "Despues SELECT buscarCodBarrasDespensa");
			
			//String despensas[];
			//Nos aseguramos de que existe al menos un registro
			if (c.moveToFirst()) {
				
				Log.d(Constantes.LOG_TAG, "buscarCodBarrasDespensa() - Existe un producto");				
				result = true;
				
			}
			
			
		}
		//Cerramos la base de datos
		//db.close();
		Log.d(Constantes.LOG_TAG, "buscarCodBarrasDespensa() - Return: "+result);				
		return result;
	}
	
	/**
	 * buscarCodBarrasProducto: Busca la existencia de un producto en la los productos
	 * 
	 * @param codBarras
	 * @return
	 */
	private boolean buscarCodBarrasProducto(String codBarras){
		Log.d(Constantes.LOG_TAG, "buscarCodBarrasProducto() - codBarras: "+codBarras);
		boolean result=false;
		
		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null){
			//Log.d(Constantes.LOG_TAG, "Dentro buscarCodBarrasProducto");
			Cursor c = db.rawQuery(" SELECT * FROM "+TablaProductos.tabla+" WHERE "+TablaProductos.codBarras+"='"+codBarras+"'",null);
			//Log.d(Constantes.LOG_TAG, "Despues SELECT buscarCodBarrasProducto");
			
			//String despensas[];
			//Nos aseguramos de que existe al menos un registro
			if (c.moveToFirst()) {
								
				Log.d(Constantes.LOG_TAG, "buscarCodBarrasProducto() - Existe un producto");
				result = true;
				
			}
			
			
		}
		//Cerramos la base de datos
		//db.close();
		Log.d(Constantes.LOG_TAG, "buscarCodBarrasProducto() - Result: "+result);				
		return result;
	}
	
	/**
	 * buscarCodBarras: Busca si existe el producto en la tabla seleccionada
	 * 
	 * @param codBarras
	 * @param despensa
	 * @return resultado
	 */
	public int buscarCodBarras(String codBarras, int despensa){
		Log.d(Constantes.LOG_TAG,"buscarCodBarras() - codbarras: "+codBarras+" idDespensa: "+despensa);
		int result=0;
		
		if (buscarCodBarrasDespensa(codBarras, despensa)){
			result = Constantes.ESTADO_BARCODE_DESPENSA;
		} else if (buscarCodBarrasProducto(codBarras)){
			result = Constantes.ESTADO_BARCODE_PRODUCTOS;
		} else {
			result = Constantes.ESTADO_BARCODE_NO_EXISTE;
		}
		
		Log.d(Constantes.LOG_TAG,"buscarCodBarras() - Return: "+ result);
		return result;
	}
	
	/**
	 * detIdDespensa: Retorna el _id de la despensa
	 * 
	 * @param desp
	 * @return
	 */
	public int getIdDespensa(String desp){
		Log.d(Constantes.LOG_TAG, "getIdDespensa() - Despensa: "+desp);
		int result=0;
		
		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null){
			//Log.d(Constantes.LOG_TAG, "Dentro getIdDespensa");
			Cursor c = db.rawQuery(" SELECT "+TablaDespensas.id+" FROM "+TablaDespensas.tabla+" WHERE "+TablaDespensas.nombre+"='"+desp+"'",null);
			//Log.d(Constantes.LOG_TAG, "Despues SELECT getIdDespensa");
			
			//Nos aseguramos de que existe al menos un registro
			if (c.moveToFirst()) {
								
				Log.d(Constantes.LOG_TAG, "Dentro movefirst getIdDespensa");				
				Log.d(Constantes.LOG_TAG, "getIdDespensa() - Extrar el _id");
				result = c.getInt(0);
				
			}
			
			
		}
		//Cerramos la base de datos
		//db.close();
		Log.d(Constantes.LOG_TAG, "getIdDespensa() - Result: "+result);				
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
	public long insertarProducto(String nombre, String codigoBarras, int categoria, String notas){
		Log.d(Constantes.LOG_TAG, "insertarProducto() - ");
		long result = 0;
		
		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null){

			ContentValues nuevoRegistro = new ContentValues();
			
			nuevoRegistro.put(TablaProductos.nombre, nombre.toString());
			if (codigoBarras.trim().length() != 0)
				nuevoRegistro.put(TablaProductos.codBarras,codigoBarras.toString());
			
			nuevoRegistro.put(TablaProductos.idCategoria,categoria);
			
			nuevoRegistro.put(TablaProductos.notas,notas.toString());
			
			//Insertamos el registro en la base de datos
			result = db.insert(TablaProductos.tabla, null, nuevoRegistro);
			
			
			
		}

		return result;
	}
	
	public boolean insertarProductoADespensa(long idProducto, int idDespensa, int stock, int stockMin){
		Log.d(Constantes.LOG_TAG, "insertarProductoADespensa() - ");
		boolean result = false;
		
		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null){

			ContentValues nuevoRegistro = new ContentValues();
			
			nuevoRegistro.put(TablaContenido.idDespensa, idDespensa);			
			nuevoRegistro.put(TablaContenido.idProducto,idProducto);			
			nuevoRegistro.put(TablaContenido.stock,stock);
			nuevoRegistro.put(TablaContenido.stockMin,stockMin);
			
			//Insertamos el registro en la base de datos
			db.insert(TablaContenido.tabla, null, nuevoRegistro);
			
			result =true;  				
		}
	
		db.close();
		return result;
	}
	
	public long getIdProductoXBarCode(String codeBar){
		Log.d(Constantes.LOG_TAG, "getIdProductoXBarCode() - codeBar: "+codeBar);
		long result = 0;
		
		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null){
			String[] campos = new String[] {TablaProductos.id};
			String[] args = new String[] {codeBar};
			
			Cursor c = db.query(TablaProductos.tabla, campos, TablaProductos.codBarras+"=?", args, null, null, null);
			
			//Nos aseguramos de que existe al menos un registro
			if (c.moveToFirst()) {
				//Recorremos el cursor hasta que no haya más registros
				do {
					result = c.getLong(0);
				} while(c.moveToNext());
			}

		}
		db.close();
		return result;
	}
	
	public boolean actualizarStockXCode(String codBarras, int despensa, int cantidad){
		Log.d(Constantes.LOG_TAG, "aumentarStockXCode() - codBarras: "+codBarras+" Despensa: "+despensa+" Cantidad: "+cantidad);
		boolean result=false;
		
		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null){
			
			db.execSQL(" UPDATE "+TablaContenido.tabla+" SET "+TablaContenido.stock+"= "+TablaContenido.stock +"+"+cantidad +
																" WHERE "+TablaContenido.idDespensa+"= "+despensa+" AND " +
																TablaContenido.idProducto+"=(SELECT "+TablaProductos.id+" FROM "+ TablaProductos.tabla +
																			" WHERE "+TablaProductos.codBarras+" = '"+codBarras+"' );");
					
			result = true;
		}
		//Cerramos la base de datos
		db.close();
		Log.d(Constantes.LOG_TAG, "aumentarStockXCode() - Return: "+result);				
		return result;
	}
	
	public boolean actualizarStockXidProducto(long producto, int despensa, int cantidad){
		Log.d(Constantes.LOG_TAG, "aumentarStockXidProducto() - producto: "+producto+" Despensa: "+despensa+" Cantidad: "+cantidad);
		boolean result=false;
		
		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null){
			
			db.execSQL(" UPDATE "+TablaContenido.tabla+" SET "+TablaContenido.stock+"= "+TablaContenido.stock +"+"+cantidad +
																" WHERE "+TablaContenido.idDespensa+"= "+despensa+" AND " +
																TablaContenido.idProducto+"="+producto+";");
					
			result = true;
		}
		//Cerramos la base de datos
		db.close();
		Log.d(Constantes.LOG_TAG, "aumentarStockXidProducto() - Return: "+result);				
		return result;
	}
	
	public ProductoDespensa[] getProductosDespensa(int idDespensa){
		Log.d(Constantes.LOG_TAG, "getProductosDespensa()");
//		ArrayList<ProductoDespensa> listaProductos= new ArrayList<ProductoDespensa>();
		ProductoDespensa[] listaProductos;
		ProductoDespensa productovacio = new ProductoDespensa();
		
		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null){
			Log.d(Constantes.LOG_TAG, "getProductosDespensa() - Antes del SELECT");
			
			Cursor c = db.rawQuery(" SELECT "+TablaProductos.id+", "+TablaProductos.nombre+", "+TablaContenido.stock+", "+TablaContenido.stockMin+", " +TablaProductos.codBarras+
									" FROM "+TablaContenido.tabla+" JOIN "+TablaProductos.tabla+"" +
									" ON "+TablaContenido.idProducto+"="+TablaProductos.id+" WHERE "+TablaContenido.idDespensa+"= "+idDespensa,null);
//			
			Log.d(Constantes.LOG_TAG, "getProductosDespensa() - Despues del SELECT");
			
			//Nos aseguramos de que existe al menos un registro
			if (c.moveToFirst()) {
				Log.d(Constantes.LOG_TAG, "getProductosDespensa() - Dentro movefirst");
				//Recorremos el cursor hasta que no haya más registros
				listaProductos = new ProductoDespensa[c.getCount()];
				int i=0;
				
				do {
					Log.d(Constantes.LOG_TAG, "getProductosDespensa() - Dentro do-while "+i);
					
					listaProductos[i]= new ProductoDespensa(Var.despensaSelec.getId(), c.getLong(0), c.getString(1), c.getInt(2), c.getInt(3), c.getString(4));
//					despensas[i]= c.getString(1);				
					i++;

				} while(c.moveToNext());
				
			} else {
				
				listaProductos = new ProductoDespensa[] {productovacio};
			}

			if (c != null && !c.isClosed()) {
				Log.d(Constantes.LOG_TAG,"getProductosDespensa() - Cerrar cursor");
				c.close();
			}

			
			
		} else {
			//despensas = new String[] {""};
//			listaProductos[0] = new ProductoDespensa() ;
			listaProductos = new ProductoDespensa[] {productovacio};
		}
		
		
		//Cerramos la base de datos
		db.close();
//		Log.d(Constantes.LOG_TAG, "getIdDespensa() - Return: "+despensas.toString());
		
		Log.d(Constantes.LOG_TAG,"getProductosDespensa() - return");
		return listaProductos;
	}
	
	/**
	 * getProductos
	 * 
	 * @return Productos[]
	 */
	public Producto[] getProductos(){
		Log.d(Constantes.LOG_TAG,"getProductos()");
		//private String[] despensas;
		Producto productos[];
		
		Producto productoVacio = new Producto(0,"","","",0,"");
		
		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null){
			Log.d(Constantes.LOG_TAG, "getProductos() - Antes del SELECT");
			Cursor c = db.rawQuery(" SELECT " +TablaProductos.tabla+"."+TablaProductos.id+", "+TablaProductos.tabla+"."+TablaProductos.nombre+", "+TablaProductos.codBarras+", "+TablaProductos.notas+", "+TablaProductos.idCategoria+", "+TablaCategorias.tabla+"."+TablaCategorias.nombre+
									" FROM "+TablaProductos.tabla+ " LEFT JOIN "+TablaCategorias.tabla+" " +
											" ON "+TablaProductos.tabla+"."+TablaProductos.idCategoria+"="+TablaCategorias.tabla+"."+TablaCategorias.id+
									" ORDER BY "+TablaProductos.tabla+"."+TablaDespensas.nombre,null);
			Log.d(Constantes.LOG_TAG, "getProductos() - Despues del SELECT");
			
			//Nos aseguramos de que existe al menos un registro
			if (c.moveToFirst()) {
				Log.d(Constantes.LOG_TAG, "getProductos() - Dentro movefirst");
				//Recorremos el cursor hasta que no haya más registros
				
				productos = new Producto[c.getCount()]; 
				int i=0;
				
				do {
					Log.d(Constantes.LOG_TAG, "getProductos() - Dentro do-while "+i);
					
					productos[i]= new Producto(c.getLong(0),c.getString(1),c.getString(2),c.getString(3),c.getInt(4),c.getString(5));				
					i++;

				} while(c.moveToNext());
				
			} else {
				productos = new Producto[] {productoVacio};
			}
			
			
		} else {
			productos = new Producto[] {productoVacio};
		}
		//Cerramos la base de datos
		db.close();
		Log.d(Constantes.LOG_TAG, "getProductos() - Return: "+productos.toString());
		return productos;
		
	} //Fin getProductos
	
	public ProductoCompra[] getProductosCompra(int idDespensa){
		Log.d(Constantes.LOG_TAG, "getProductosCompra()");
//		ArrayList<ProductoDespensa> listaProductos= new ArrayList<ProductoDespensa>();
		ProductoCompra[] listaProductos;
		ProductoCompra productovacio = new ProductoCompra();
		
		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null){
			Log.d(Constantes.LOG_TAG, "getProductosCompra() - Antes del SELECT");
			
			Cursor c = db.rawQuery(" SELECT "+TablaProductos.id+", "+TablaProductos.nombre+", "+TablaListaCompra.cantidadAComprar+", "+TablaListaCompra.cantidadComprada+", " +TablaProductos.codBarras+
									" FROM "+TablaListaCompra.tabla+" JOIN "+TablaProductos.tabla+"" +
									" ON "+TablaListaCompra.idProducto+"="+TablaProductos.id+" WHERE "+TablaListaCompra.idDespensa+"= "+idDespensa,null);
//			
			Log.d(Constantes.LOG_TAG, "getProductosCompra() - Despues del SELECT");
			
			//Nos aseguramos de que existe al menos un registro
			if (c.moveToFirst()) {
				Log.d(Constantes.LOG_TAG, "getProductosCompra() - Dentro movefirst");
				//Recorremos el cursor hasta que no haya más registros
				listaProductos = new ProductoCompra[c.getCount()];
				int i=0;
				
				do {
					Log.d(Constantes.LOG_TAG, "getProductosCompra() - Dentro do-while "+i);
					
					listaProductos[i]= new ProductoCompra(Var.despensaSelec.getId(), c.getLong(0), c.getString(1), c.getInt(2), c.getInt(3), c.getString(4));
//					despensas[i]= c.getString(1);				
					i++;

				} while(c.moveToNext());
				
			} else {
				
				listaProductos = new ProductoCompra[] {productovacio};
			}

			if (c != null && !c.isClosed()) {
				Log.d(Constantes.LOG_TAG,"getProductosCompra() - Cerrar cursor");
				c.close();
			}

			
			
		} else {
			//despensas = new String[] {""};
//			listaProductos[0] = new ProductoDespensa() ;
			listaProductos = new ProductoCompra[] {productovacio};
		}
		
		
		//Cerramos la base de datos
		db.close();
//		Log.d(Constantes.LOG_TAG, "getIdDespensa() - Return: "+despensas.toString());
		
		Log.d(Constantes.LOG_TAG,"getProductosCompra() - return");
		return listaProductos;
	}
	
	public boolean generarListaCompra(int idDespensa){
		Log.d(Constantes.LOG_TAG,"generarListaCompra()");
		boolean result = false;
		
		
		
		return result;
	}

}
