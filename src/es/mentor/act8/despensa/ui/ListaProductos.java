package es.mentor.act8.despensa.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import es.mentor.act8.despensa.App;
import es.mentor.act8.despensa.Constantes;
import es.mentor.act8.despensa.R;
import es.mentor.act8.despensa.model.Producto;
import es.mentor.act8.despensa.model.ProductoDespensa;

public class ListaProductos extends ListActivity {
	
	private ArrayList<Producto> listProductos;
	private AdaptadorProductos mAdaptador;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.productos);
		
		listProductos = new ArrayList<Producto>();
		mAdaptador = new AdaptadorProductos(this, listProductos);
		setListAdapter(mAdaptador);
		
		listarProductos();
	}
	
	// EVITAR QUE NUESTRA ACTIVIDAD COMIENCE DE NUEVO AL ROTAR LA PANTALLA
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
	
	public void listarProductos() {
		listProductos.clear();
		listProductos.addAll(App.getDatabase().getProductos());
		mAdaptador.notifyDataSetChanged();
	}
	
	class AdaptadorProductos extends ArrayAdapter<Producto> {
		
		Activity context;

		AdaptadorProductos(Activity context, ArrayList<Producto> objects) {
			super(context, R.layout.listitem_producto, objects);
			this.context = context;
		}

		public View getView(final int position, View convertView, ViewGroup parent) {
			Log.d(Constantes.LOG_TAG,"AdaptadorProductos() - getView()");
			LayoutInflater inflater = context.getLayoutInflater();
			View item = inflater.inflate(R.layout.listitem_producto, null);
			
			final Producto productoItem = getItem(position);
			
			TextView lblNombreP = (TextView) item.findViewById(R.id.LblItemProductoNombre);
			lblNombreP.setText(productoItem.getNombre());
			TextView lblCodigoBarras = (TextView) item.findViewById(R.id.LblItemProductoCodigoBarras);
			lblCodigoBarras.setText(productoItem.getCodigoBarras());
			ImageView imgAdd = (ImageView)item.findViewById(R.id.ImgItemProductoAddADespensa);
			ImageView imgEliminar = (ImageView)item.findViewById(R.id.ImgItemProductoEliminar);
						
			item.setOnLongClickListener(new View.OnLongClickListener() {
				
				public boolean onLongClick(View v) {
					//Implementar alertDialog para editar el producto.
					AlertDialog.Builder alert = new AlertDialog.Builder(context);

                    alert.setTitle("Editar "+productoItem.getNombre());

                    // Set an EditText view to get user input
                    final EditText input = new EditText(context);
                    input.setText(productoItem.getNombre());
                    alert.setView(input);

                    alert.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                            Editable value = input.getText();
                            if (value.toString().equals(""))
                                return;

                            productoItem.setNombre(value.toString());
                            listarProductos();
                        }
                    });

                    alert.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    });

                    alert.show();
					
					return false;
				}
			});
			
			imgAdd.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					// Create an input dialog
					String msg;
					ProductoDespensa producto = new ProductoDespensa();
					producto.setIdDespensa(App.despensaSelec.getId());
					producto.setIdProducto(productoItem.getId());
					producto.setStock(1);
					producto.setStockMin(0);
					producto.setCantidadAComprar(1);

					if (App.getDatabase().insertarProductoADespensa(producto))
						msg="Producto a�adido";
					else
						msg="Ya estaba en \nla despensa";
					
					Toast.makeText(ListaProductos.this,msg,Toast.LENGTH_SHORT).show();
				}
			});
			
			imgEliminar.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					// Create an input dialog

					AlertDialog.Builder alert = new AlertDialog.Builder(context);
					
					alert.setTitle("Eliminar "+productoItem.getNombre());
					
					alert.setMessage("Eliminando este producto, se eliminará de las despensas y listas de la compra. ¿Desea continuar?");
					
					alert.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                            if (App.getDatabase().eliminarProducto(productoItem))
                            	listarProductos();
                        }
                    });

                    alert.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    });

                    alert.show();
					listarProductos();
				}
			});
			
			
			return (item);
		}
	}

}
