package es.marquesgomez;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListaProductos extends Activity {
	
	private BaseDeDatos conexion;
	private Producto[] listProductos;
	private ListView lstProductos;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		Log.d(Constantes.LOG_TAG, "ListaProductos.java - onCreate()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.productos);
		
		this.conexion = Var.conexion;
		
		listarProductos();
	}
	
	// EVITAR QUE NUESTRA ACTIVIDAD COMIENCE DE NUEVO AL ROTAR LA PANTALLA
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
	
	public void listarProductos() {
		Log.d(Constantes.LOG_TAG,"listarProductos() - ");
		
		listProductos = conexion.getProductos();
		AdaptadorProductos adaptador;
		lstProductos = (ListView) findViewById(R.id.LstProductos);
		
		Log.d(Constantes.LOG_TAG,"listarProductos() - Antes del if null");

		if (listProductos[0].getId() != 0) {
			Log.d(Constantes.LOG_TAG, "listarProductos() - Existen productos en despensa");

			adaptador = new AdaptadorProductos(this);
			Log.d(Constantes.LOG_TAG,"listarProductos() - despues de init adaptador");

			lstProductos.setVisibility(View.VISIBLE);
//			registerForContextMenu(lstProductos);
			Log.d(Constantes.LOG_TAG,"listarProductos() - Despues de register");

			lstProductos
					.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> a, View v,
								int position, long id) {
							// Acciones necesarias al hacer click
							// Intent intent = new Intent(HolaUsuario.this,
							// FrmMensaje.class);
							//
							// Bundle bundle = new Bundle();
							// bundle.putString("NOMBRE", datos[position]);
							// intent.putExtras(bundle);
							// startActivity(intent);
							Toast.makeText(ListaProductos.this,
									"Producto: "+listProductos[position].getNombre(),
									Toast.LENGTH_LONG).show();

						}
					});
		} else {
			Log.d(Constantes.LOG_TAG,"listarProductos() - No existen productos en despensa");
			adaptador = new AdaptadorProductos(this);
			lstProductos.setVisibility(View.INVISIBLE);
			Toast.makeText(ListaProductos.this,
					"No existe ningun \nproducto",
					Toast.LENGTH_LONG).show();
		}

		lstProductos.setAdapter(adaptador);

	}//Fin listarProductos
	
	@SuppressWarnings("rawtypes")
	class AdaptadorProductos extends ArrayAdapter {
		
		Activity context;

		@SuppressWarnings("unchecked")
		AdaptadorProductos(Activity context) {
			super(context, R.layout.listitem_producto, listProductos);
			this.context = context;
		}

		public View getView(final int position, View convertView, ViewGroup parent) {
			Log.d(Constantes.LOG_TAG,"AdaptadorProductos() - getView()");
			LayoutInflater inflater = context.getLayoutInflater();
			View item = inflater.inflate(R.layout.listitem_producto, null);
			
			final long idProducto = listProductos[position].getId();
			
			TextView lblNombreP = (TextView) item.findViewById(R.id.LblItemProductoNombre);
			lblNombreP.setText(listProductos[position].getNombre());
//			TextView lblCantidadP = (TextView) item.findViewById(R.id.LblItemDespensaCantidad);
//			ImageView imgSuma = (ImageView)item.findViewById(R.id.ImgItemDespensaSuma);
//			ImageView imgResta = (ImageView)item.findViewById(R.id.ImgItemDespensaResta);
			ImageView imgEliminar = (ImageView)item.findViewById(R.id.ImgItemProductoEliminar);
			
//			int stock = listProductos[position].getStock();
//			lblCantidadP.setText(Integer.toString(stock));
//			if (listProductos[position].getStock() <= listProductos[position].getStockMin())
//				lblCantidadP.setTextColor(Color.RED);
						
			//Evento al hacer click a un item
			item.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Toast.makeText(ListaProductos.this,
							"Producto: "+listProductos[position].getNombre(),
							Toast.LENGTH_LONG).show();
				}
			});
			
			item.setOnLongClickListener(new View.OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					// TODO Auto-generated method stub
					//Implementar alertDialog para editar el producto.
					registerForContextMenu(lstProductos);
					return false;
				}
			});
			
//			imgSuma.setOnClickListener(new View.OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					conexion.actualizarStockXidProducto(idProducto, Var.despensaSelec.getId(), 1);
//					listarProductos();
//				}
//			});
//			
//			imgResta.setOnClickListener(new View.OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					conexion.actualizarStockXidProducto(idProducto, Var.despensaSelec.getId(), -1);
//					listarProductos();
//				}
//			});
			
			imgEliminar.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
//					conexion.actualizarStockXidProducto(idProducto, Var.despensaSelec.getId(), -1);
					listarProductos();
				}
			});
			
			
			return (item);
		}
	}

}
