package es.marquesgomez;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListaCompra extends Activity {
	
	private BaseDeDatos conexion;
	private ProductoDespensa[] listProductosCompra;
	private ListView lstProductosCompra;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		Log.d(Constantes.LOG_TAG, "ListaCompra.java - onCreate()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lista_compra);
		
		this.conexion = Var.conexion;
		
		Button btnFinalizarCompra = (Button)findViewById(R.id.BtnFinCompra);
		
		listarProductosCompra();
		
		btnFinalizarCompra.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder alert = new AlertDialog.Builder(ListaCompra.this);
				
				alert.setTitle("Finalizar la compra");
				alert.setMessage("Desea eliminar los productos no comprados?");
				
				alert.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    	finCompra();
                    	conexion.eliminarProductosCompra();
                    	listarProductosCompra();
                    }
                });

                alert.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    	finCompra();
                    	listarProductosCompra();
                    }
                });

                alert.show();
//				if (conexion.finalizarCompra(Var.despensaSelec.getId())){
//					Toast.makeText(ListaCompra.this,"Fin OK",Toast.LENGTH_LONG).show();
//					listarProductosCompra();
//				}
//				else
//					Toast.makeText(ListaCompra.this,"Fin ERROR",Toast.LENGTH_LONG).show();
				
			}
		});
	}
	
	// EVITAR QUE NUESTRA ACTIVIDAD COMIENCE DE NUEVO AL ROTAR LA PANTALLA
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
	
	public void finCompra(){
		
		if (conexion.finalizarCompra(Var.despensaSelec.getId())){
			Toast.makeText(ListaCompra.this,"Fin OK",Toast.LENGTH_LONG).show();
			
		}
		else
			Toast.makeText(ListaCompra.this,"Fin ERROR",Toast.LENGTH_LONG).show();
		
	}
	
	public void listarProductosCompra() {
		Log.d(Constantes.LOG_TAG,"listarProductosCompra() - ");
		
		listProductosCompra = conexion.getProductosCompra(Var.despensaSelec.getId());
		AdaptadorProductosCompra adaptador;
		lstProductosCompra = (ListView) findViewById(R.id.LstProductosCompra);
		TextView txtBackground = (TextView)findViewById(R.id.lista_compraTextBackground);
		
		
		Log.d(Constantes.LOG_TAG,"listarProductosCompra() - Antes del if null");

		if (listProductosCompra[0].getIdDespensa() != 0) {
			Log.d(Constantes.LOG_TAG, "listarProductosCompra() - Existen productos en compra");

			adaptador = new AdaptadorProductosCompra(this);
			Log.d(Constantes.LOG_TAG,"listarProductosCompra() - despues de init adaptador");

			lstProductosCompra.setVisibility(View.VISIBLE);
//			registerForContextMenu(lstProductos);
			txtBackground.setVisibility(ListView.GONE);
			Log.d(Constantes.LOG_TAG,"listarProductos() - Despues de register");

			lstProductosCompra
					.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> a, View v,
								int position, long id) {
							Toast.makeText(ListaCompra.this,
									"Producto: "+listProductosCompra[position].getNombre(),
									Toast.LENGTH_LONG).show();
							a.updateViewLayout(v, null);

						}
					});
		} else {
			Log.d(Constantes.LOG_TAG,"listarProductosCompra() - No existen productos en despensa");
			adaptador = new AdaptadorProductosCompra(this);
			lstProductosCompra.setVisibility(View.INVISIBLE);
			txtBackground.setVisibility(ListView.VISIBLE);
		}

		lstProductosCompra.setAdapter(adaptador);

	}//Fin listarProductos
	
	@SuppressWarnings("rawtypes")
	class AdaptadorProductosCompra extends ArrayAdapter {
		
		Activity context;

		@SuppressWarnings("unchecked")
		AdaptadorProductosCompra(Activity context) {
			super(context, R.layout.listitem_compra, listProductosCompra);
			this.context = context;
		}

		public View getView(final int position, View convertView, ViewGroup parent) {
			Log.d(Constantes.LOG_TAG,"AdaptadorProductosCompra() - getView()");
			LayoutInflater inflater = context.getLayoutInflater();
			View item = inflater.inflate(R.layout.listitem_compra, null);
			
			final long idProducto = listProductosCompra[position].getIdProducto();
			
			TextView lblNombreP = (TextView) item.findViewById(R.id.LblItemCompraNombre);
			lblNombreP.setText(listProductosCompra[position].getNombre());
			Log.i(Constantes.LOG_TAG,"antes del if - formato producto comprado "+listProductosCompra[position].getComprado());
			if (listProductosCompra[position].getComprado()>0){
				lblNombreP.setTextColor(Color.RED);
				Log.i(Constantes.LOG_TAG,"formato producto comprado "+listProductosCompra[position].getComprado());
			}
			TextView lblCantidadP = (TextView) item.findViewById(R.id.LblItemCompraCantidad);
			ImageView imgSuma = (ImageView)item.findViewById(R.id.ImgItemCompraSuma);
			ImageView imgResta = (ImageView)item.findViewById(R.id.ImgItemCompraResta);
			ImageView imgEliminar = (ImageView)item.findViewById(R.id.ImgItemCompraEliminar);
			
			int cantidad = listProductosCompra[position].getCantidadComprada();
			lblCantidadP.setText(Integer.toString(cantidad));
//			if (listProductosCompra[position].get() <= listProductosCompra[position].getStockMin())
//				lblCantidadP.setTextColor(Color.RED);
						
			//Evento al hacer click a un item
			item.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (listProductosCompra[position].getComprado()==1)
						listProductosCompra[position].setComprado(0);
					else
						listProductosCompra[position].setComprado(1);
					
					if (conexion.productoComprado(listProductosCompra[position])){
						Toast.makeText(ListaCompra.this,"Producto marcado",Toast.LENGTH_LONG).show();
						listarProductosCompra();
					}
					
				}
			});
			
			item.setOnLongClickListener(new View.OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					// TODO Implementar alertDialog para editar el producto.
					
					registerForContextMenu(lstProductosCompra);
					return false;
				}
			});
			
			imgSuma.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					conexion.actualizarCantidadCompradaXidProducto(idProducto, Var.despensaSelec.getId(), 1);
					listarProductosCompra();
				}
			});
			
			imgResta.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					conexion.actualizarCantidadCompradaXidProducto(idProducto, Var.despensaSelec.getId(), -1);
					listarProductosCompra();
				}
			});
			
			imgEliminar.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					AlertDialog.Builder alert = new AlertDialog.Builder(context);
					
					alert.setTitle(listProductosCompra[position].getNombre());
					alert.setMessage("Eliminar?");
					
					alert.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        	ProductoDespensa itemToEdit = listProductosCompra[position];
                            if (conexion.eliminarProductoCompra(itemToEdit))
                            	listarProductosCompra();
                        }
                    });

                    alert.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    });

                    alert.show();
//					conexion.actualizarStockXidProducto(idProducto, Var.despensaSelec.getId(), -1);
					listarProductosCompra();
				}
			});
			
			
			return (item);
		}
	}

}