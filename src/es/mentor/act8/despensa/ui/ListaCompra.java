package es.mentor.act8.despensa.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import es.mentor.act8.despensa.App;
import es.mentor.act8.despensa.Constantes;
import es.mentor.act8.despensa.R;
import es.mentor.act8.despensa.model.ProductoDespensa;

public class ListaCompra extends ListActivity {
	
	private ArrayList<ProductoDespensa> listProductosCompra;
	private AdaptadorProductosCompra mAdaptador;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		Log.d(Constantes.LOG_TAG, "ListaCompra.java - onCreate()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lista_compra);
		
		listProductosCompra = new ArrayList<ProductoDespensa>();
		mAdaptador = new AdaptadorProductosCompra(this, listProductosCompra);
		setListAdapter(mAdaptador);	
		registerForContextMenu(getListView());
		
		Button btnFinalizarCompra = (Button)findViewById(R.id.BtnFinCompra);
		
		listarProductosCompra();
		
		btnFinalizarCompra.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				AlertDialog.Builder alert = new AlertDialog.Builder(ListaCompra.this);
				
				alert.setTitle("Finalizar la compra");
				alert.setMessage("Desea eliminar los productos no comprados?");
				
				alert.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    	finCompra();
                    	App.getDatabase().eliminarProductosCompra();
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
			}
		});
	}
	
	// EVITAR QUE NUESTRA ACTIVIDAD COMIENCE DE NUEVO AL ROTAR LA PANTALLA
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
	
	public void finCompra(){
		
		if (App.getDatabase().finalizarCompra(App.despensaSelec.getId())){
			Toast.makeText(ListaCompra.this,"Fin OK",Toast.LENGTH_LONG).show();
			
		}
		else
			Toast.makeText(ListaCompra.this,"Fin ERROR",Toast.LENGTH_LONG).show();
		
	}
	
	public void listarProductosCompra() {
		listProductosCompra.clear();
		listProductosCompra.addAll(App.getDatabase().getProductosCompra(App.despensaSelec.getId()));
		mAdaptador.notifyDataSetChanged();
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Toast.makeText(ListaCompra.this,
				"Producto: "+listProductosCompra.get(position).getNombre(),
				Toast.LENGTH_LONG).show();
		l.updateViewLayout(v, null);
	}
	
	class AdaptadorProductosCompra extends ArrayAdapter<ProductoDespensa> {
		
		Activity context;

		AdaptadorProductosCompra(Activity context, ArrayList<ProductoDespensa> objects) {
			super(context, R.layout.listitem_compra, objects);
			this.context = context;
		}

		public View getView(final int position, View convertView, ViewGroup parent) {
			Log.d(Constantes.LOG_TAG,"AdaptadorProductosCompra() - getView()");
			LayoutInflater inflater = context.getLayoutInflater();
			View item = inflater.inflate(R.layout.listitem_compra, null);
			
			final ProductoDespensa productoItem = getItem(position);
//			final long idProducto = listProductosCompra[position].getIdProducto();
			
			TextView lblNombreP = (TextView) item.findViewById(R.id.LblItemCompraNombre);
			lblNombreP.setText(productoItem.getNombre());
			if (productoItem.getComprado()>0){
				lblNombreP.setTextColor(Color.RED);
			}
			TextView lblCantidadP = (TextView) item.findViewById(R.id.LblItemCompraCantidad);
			ImageView imgSuma = (ImageView)item.findViewById(R.id.ImgItemCompraSuma);
			ImageView imgResta = (ImageView)item.findViewById(R.id.ImgItemCompraResta);
			ImageView imgEliminar = (ImageView)item.findViewById(R.id.ImgItemCompraEliminar);
			
			lblCantidadP.setText(Integer.toString(productoItem.getCantidadComprada()));
						
			//Evento al hacer click a un item
			item.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					if (productoItem.getComprado()==1)
						productoItem.setComprado(0);
					else
						productoItem.setComprado(1);
					
					if (App.getDatabase().productoComprado(productoItem)){
						Toast.makeText(ListaCompra.this,"Producto marcado",Toast.LENGTH_LONG).show();
						listarProductosCompra();
					}
					
				}
			});
			
			imgSuma.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					App.getDatabase().actualizarCantidadCompradaXidProducto(productoItem.getIdProducto(), App.despensaSelec.getId(), 1);
					listarProductosCompra();
				}
			});
			
			imgResta.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					App.getDatabase().actualizarCantidadCompradaXidProducto(productoItem.getIdProducto(), App.despensaSelec.getId(), -1);
					listarProductosCompra();
				}
			});
			
			imgEliminar.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					AlertDialog.Builder alert = new AlertDialog.Builder(context);
					
					alert.setTitle(productoItem.getNombre());
					alert.setMessage("Eliminar?");
					
					alert.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if (App.getDatabase().eliminarProductoCompra(productoItem))
                            	listarProductosCompra();
                        }
                    });

                    alert.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    });

                    alert.show();
					listarProductosCompra();
				}
			});
			
			
			return (item);
		}
	}

}