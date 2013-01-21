package es.mentor.act8.despensa.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import es.mentor.act8.despensa.App;
import es.mentor.act8.despensa.Constantes;
import es.mentor.act8.despensa.R;
import es.mentor.act8.despensa.model.ProductoDespensa;
import es.mentor.act8.despensa.utils.barcode.IntentIntegrator;
import es.mentor.act8.despensa.utils.barcode.IntentResult;

public class ContenidoDespensa extends ListActivity {

	private int resultEstadoCodBarras;
	private String barCodeFinal;
	private boolean teclaMenu = false;
	private ArrayList<ProductoDespensa> listProductosDespensa;
	private AdaptadorProductosDespensa mAdaptador;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(Constantes.LOG_TAG, "ContenidoDespensa.java - onCreate()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.productos_despensa);
		
		listProductosDespensa = new ArrayList<ProductoDespensa>();
		mAdaptador = new AdaptadorProductosDespensa(this, listProductosDespensa);

		registerForContextMenu(getListView());
		setListAdapter(mAdaptador);

		Button btnProductos = (Button) findViewById(R.id.BtnProductos);
		Button btnListaCompra = (Button) findViewById(R.id.BtnLista);
		Button btnNuevoProducto = (Button) findViewById(R.id.BtnNuevoProducto);

		TextView txtMensaje = (TextView) findViewById(R.id.TxtMensaje);
				
		//Establece el mensaje principal
		txtMensaje.setText("Contenido despensa " + App.despensaSelec.getNombre() + " ("
				+ App.despensaSelec.getId() + ")");

		Log.d(Constantes.LOG_TAG, "ContenidoDespensa.java - onCreate() - despensa: "
				+ App.despensaSelec + " idDespensa: " + App.despensaSelec.getId());

		btnProductos.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				 Intent intent = new Intent(ContenidoDespensa.this, ListaProductos.class);
				 startActivity(intent);

			}
		});
		
		btnListaCompra.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				 Intent intent = new Intent(ContenidoDespensa.this, ListaCompra.class);
				 startActivity(intent);

			}
		});
		
		btnNuevoProducto.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				addProducto("");
			}
		});
		
		Log.d(Constantes.LOG_TAG,"ContenidoDespensa.java - Fin onCreate()");
	}//Fin onCreate()
	
	@Override
	protected void onStart(){
		Log.i(Constantes.LOG_TAG,"ContenidoDespensas.java - onStart()");
		super.onStart();
		listarProductosDespensa();
	}

	// EVITAR QUE NUESTRA ACTIVIDAD COMIENCE DE NUEVO AL ROTAR LA PANTALLA
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	/**
	 * Menú Contextual
	 * 
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		Log.d(Constantes.LOG_TAG,"onCreateContextMenu()");

		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();

		if (v.getId() == android.R.id.list) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			menu.setHeaderTitle(listProductosDespensa.get(info.position).getNombre());

			inflater.inflate(R.menu.menu_ctx_producto_despensa, menu);
		}

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.CtxProductoSumar1:
			// if
			// (conexion.eliminarDespensa(lstProductosDespensa.getAdapter().getItem(info.position).toString()))
			// listarProductosDespensa(conexion);
			return true;
		case R.id.CtxProductoResta1:
			// if
			// (conexion.eliminarDespensa(lstProductosDespensa.getAdapter().getItem(info.position).toString()))
			// listarProductosDespensa(conexion);
			return true;
		case R.id.CtxProductoEditar:

			Intent intent = new Intent(ContenidoDespensa.this, EditProducto.class);

			Bundle bundle = new Bundle();
			bundle.putLong(Constantes.ID_PRODUCTO_EDIT, mAdaptador.getItemId(info.position));
			bundle.putInt(Constantes.ACCION, Constantes.ACCION_EDIT_DESPENSA);
			intent.putExtras(bundle);
			startActivity(intent);
			return true;
		case R.id.CtxProductoAddACompra:
			insertarProductoACompra(mAdaptador.getItem(info.position));
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	/**
	 * Menús de opciones
	 * 
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.i(Constantes.LOG_TAG, "ContenidoDespensa.java - onCreateOptionsMenu() - "
				+ resultEstadoCodBarras);
		// Alternativa 1
		MenuInflater inflater = getMenuInflater();

		inflater.inflate(R.menu.menu_opc_despensa, menu);
		
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		// MenuItem item;
		// item = menu.findItem(R.id.Ctx1Editar);
		// item.setEnabled(true);
		if (teclaMenu) {

			menu.setGroupVisible(R.id.OpcGrpPrincipal, true);
			menu.setGroupVisible(R.id.OpcGrpProducto, false);
			menu.setGroupVisible(R.id.OpcGrpAddAD, false);
			menu.setGroupVisible(R.id.OpcGrpAddAP, false);

			teclaMenu = false;
		} else {

			menu.setGroupVisible(R.id.OpcGrpPrincipal, false);

			switch (resultEstadoCodBarras) {
			case Constantes.ESTADO_BARCODE_DESPENSA:
				menu.setGroupVisible(R.id.OpcGrpProducto, true);
				menu.setGroupVisible(R.id.OpcGrpAddAD, false);
				menu.setGroupVisible(R.id.OpcGrpAddAP, false);
				break;
			case Constantes.ESTADO_BARCODE_PRODUCTOS:
				menu.setGroupVisible(R.id.OpcGrpProducto, false);
				menu.setGroupVisible(R.id.OpcGrpAddAD, true);
				menu.setGroupVisible(R.id.OpcGrpAddAP, false);
				break;
			case Constantes.ESTADO_BARCODE_NO_EXISTE:
				menu.setGroupVisible(R.id.OpcGrpProducto, false);
				menu.setGroupVisible(R.id.OpcGrpAddAD, false);
				menu.setGroupVisible(R.id.OpcGrpAddAP, true);
				break;
			default:
				break;
			}
		}

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d(Constantes.LOG_TAG, "ContenidoDespensa.java - onOptionsItemSelected()");

		switch (item.getItemId()) {
		case R.id.OpcSuma1:
			App.getDatabase().actualizarStockXCode(barCodeFinal, App.despensaSelec.getId(), 1);
			listarProductosDespensa();
			return true;
		case R.id.OpcResta1:
			App.getDatabase().actualizarStockXCode(barCodeFinal, App.despensaSelec.getId(), -1);
			listarProductosDespensa();
			return true;
		case R.id.OpcEliminar:
			return true;
		case R.id.OpcEditar:
			return true;
		case R.id.OpcAddAD:
			addProductoADespensa(this.barCodeFinal);
			return true;
		case R.id.OpcAddAP:
			addProducto(this.barCodeFinal);
			return true;
		case R.id.OpcScan:
			scanCode();
			return true;
		case R.id.OpcListaCompra:
			generarCompra();
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	private void addProductoADespensa(String barCode) {
		Log.d(Constantes.LOG_TAG, "añadirProductoADespensa() - barCode: "
				+ barCode);
		ProductoDespensa producto;

		String toastMensaje = "";
		long idProducto = App.getDatabase().getIdProductoXBarCode(barCode);
		Log.d(Constantes.LOG_TAG, "añadirProductoADespensa() - idProducto: "+ idProducto);
		if (idProducto > 0){
			
			producto = new ProductoDespensa();
			producto.setIdDespensa(App.despensaSelec.getId());
			producto.setIdProducto(idProducto);
			producto.setStock(1);
			producto.setStockMin(0);
			producto.setCantidadAComprar(1);
			
			if (App.getDatabase().insertarProductoADespensa(producto))
				toastMensaje= "Producto en despensa";
			else
				toastMensaje = "Error al a�adirlo";
		} else
			toastMensaje = "No existe \nproducto";
	
		Toast.makeText(ContenidoDespensa.this, toastMensaje,Toast.LENGTH_SHORT).show();
	}

	private void addProducto(String barCode) {
		Log.d(Constantes.LOG_TAG, "añadirProducto() - barCode: " + barCode);
		Intent intent = new Intent(ContenidoDespensa.this, EditProducto.class);

		intent.putExtra(Constantes.BARCODE, barCode);
		intent.putExtra(Constantes.ACCION, Constantes.ACCION_ADD_DESPENSA);
		startActivity(intent);

	}

	/**
	 * buscarProducto: Busca la existencia del producto en la BD. Depende de
	 * donde se encuentra el producto, si está, aparece un menú u otro de
	 * opciones.
	 * 
	 * @param codBarras
	 */
	public void buscarProducto(String codBarras) {
		Log.d(Constantes.LOG_TAG, "buscarProducto() - codBarras: " + codBarras);

		resultEstadoCodBarras = App.getDatabase().buscarCodBarras(codBarras,
				App.despensaSelec.getId());
		this.barCodeFinal = codBarras;
		Log.d(Constantes.LOG_TAG,
				"ContenidoDespensa.java - buscarProducto() - Antes de openOptionsMenu()");
		openOptionsMenu();
		Log.d(Constantes.LOG_TAG,
				"ContenidoDespensa.java - buscarProducto() - Despues de openOptionsMenu()");

	}

	/**
	 * scanCode: Llama a Barcode Scanner desde IntentIntegrator.
	 */
	public void scanCode() {
		IntentIntegrator.initiateScan(ContenidoDespensa.this);
	}

	/* Here is where we come back after the Barcode Scanner is done */
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		
		if (scanResult.getContents() != null) {

			// handle scan result
			String contents = intent.getStringExtra("SCAN_RESULT");
			// Handle successful scan
			buscarProducto(contents);
		} else {
			// else continue with any other code you need in the method
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case (KeyEvent.KEYCODE_CAMERA):
			scanCode();
			return true;
		case (KeyEvent.KEYCODE_MENU):
			teclaMenu = true;
			openOptionsMenu();
			return true;
		default:
			return super.onKeyDown(keyCode, event);
		}
	}

	private void listarProductosDespensa() {
		listProductosDespensa.clear();
		listProductosDespensa.addAll(App.getDatabase().getProductosDespensa(App.despensaSelec.getId()));
		mAdaptador.notifyDataSetChanged();
	}

	class AdaptadorProductosDespensa extends ArrayAdapter<ProductoDespensa> {
		
		Activity context;

		AdaptadorProductosDespensa(Activity context, ArrayList<ProductoDespensa> objects) {
			super(context, R.layout.listitem_despensa, objects);
			this.context = context;
		}

		public View getView(final int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = context.getLayoutInflater();
			View item = inflater.inflate(R.layout.listitem_despensa, null);
			
			final ProductoDespensa productoItem = getItem(position);
			
			TextView lblNombreP = (TextView) item.findViewById(R.id.LblItemDespensaNombre);
			lblNombreP.setText(productoItem.getNombre());
			TextView lblCantidadP = (TextView) item.findViewById(R.id.LblItemDespensaCantidad);
			ImageView imgSuma = (ImageView)item.findViewById(R.id.ImgItemDespensaSuma);
			ImageView imgResta = (ImageView)item.findViewById(R.id.ImgItemDespensaResta);
			ImageView imgEliminar = (ImageView)item.findViewById(R.id.ImgItemDespensaEliminar);
			
			int stock = productoItem.getStock();
			lblCantidadP.setText(Integer.toString(stock));
			if (productoItem.isEndStock())
				lblCantidadP.setTextColor(Color.RED);

			imgSuma.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					App.getDatabase().actualizarStockXidProducto(productoItem.getIdProducto(), App.despensaSelec.getId(), 1);
					listarProductosDespensa();
				}
			});
			
			imgResta.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					App.getDatabase().actualizarStockXidProducto(productoItem.getIdProducto(), App.despensaSelec.getId(), -1);
					listarProductosDespensa();
				}
			});
			
			imgEliminar.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					AlertDialog.Builder alert = new AlertDialog.Builder(context);
					
					alert.setTitle(productoItem.getNombre());
					alert.setMessage("Eliminar?");
					
					alert.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                            if (App.getDatabase().eliminarProductoDespensa(productoItem))
                            	listarProductosDespensa();
                        }
                    });

                    alert.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    });

                    alert.show();
				}
			});
			
			
			return (item);
		}
	}
	
	private void generarCompra(){
		String msg;
		
		if (App.getDatabase().generarListaCompra(App.despensaSelec.getId()))
			msg = "Lista generada";
		else
			msg = "Error en lista";
		
		Toast.makeText(ContenidoDespensa.this,msg,Toast.LENGTH_LONG).show();
	}
	
	private void insertarProductoACompra(ProductoDespensa produc){
		String msg;
		
		if (App.getDatabase().insertarProductoACompra(produc))
			msg="Producto a�adido";
		else
			msg="Ya estaba en \nla lista";
		
		Toast.makeText(ContenidoDespensa.this,msg,Toast.LENGTH_LONG).show();
	}

}
