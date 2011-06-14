package es.marquesgomez;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.Button;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class ContenidoDespensa extends Activity {

	private BaseDeDatos conexion;
	private int resultEstadoCodBarras;
	private Button btnScan;
	private String barCodeFinal;
	private boolean teclaMenu = false;
	private ProductoDespensa[] listProductosDespensa;
	private ListView lstProductosDespensa;
//	private AdaptadorProductosDespensa adaptador;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(Constantes.LOG_TAG, "ContenidoDespensa.java - onCreate()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.productos_despensa);

		Log.d(Constantes.LOG_TAG, "ContenidoDespensa.java - onCreate()2");
		//Conexión base de datos
		this.conexion = Var.conexion;
		Log.d(Constantes.LOG_TAG, "ContenidoDespensa.java - onCreate()3");
		btnScan = (Button) findViewById(R.id.BtnScan);
		Log.d(Constantes.LOG_TAG, "ContenidoDespensa.java - onCreate()4");
		Button btnProductos = (Button) findViewById(R.id.BtnProductos);
		Log.d(Constantes.LOG_TAG, "ContenidoDespensa.java - onCreate()5");
		Button btnListaCompra = (Button) findViewById(R.id.BtnLista);
		

		TextView txtMensaje = (TextView) findViewById(R.id.TxtMensaje);
		Log.d(Constantes.LOG_TAG, "ContenidoDespensa.java - onCreate()7");
		listarProductosDespensa();
		
				
		//Establece el mensaje principal
		txtMensaje.setText("Contenido despensa " + Var.despensaSelec.getNombre() + " ("
				+ Var.despensaSelec.getId() + ")");

		Log.d(Constantes.LOG_TAG, "ContenidoDespensa.java - onCreate() - despensa: "
				+ Var.despensaSelec + " idDespensa: " + Var.despensaSelec.getId());

		btnProductos.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				 Intent intent = new Intent(ContenidoDespensa.this, ListaProductos.class);
				 startActivity(intent);

			}
		});
		
		btnListaCompra.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				 Intent intent = new Intent(ContenidoDespensa.this, ListaCompra.class);
				 startActivity(intent);

			}
		});

		btnScan.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				Log.d(Constantes.LOG_TAG, "ContenidoDespensa.java - btmScan.onClik()");

				scanCode();

			}
		});
		Log.d(Constantes.LOG_TAG,"ContenidoDespensa.java - Fin onCreate()");
	}//Fin onCreate()

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

		if (v.getId() == R.id.LstProductosDespensa) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
//			menu.setHeaderTitle(lstProductosDespensa.getAdapter().getItem(info.position).toString());
			menu.setHeaderTitle(listProductosDespensa[info.position].getNombre());

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
			// lblMensaje.setText("Etiqueta: Opcion 1 pulsada!");
			return true;
		case R.id.CtxProductoResta1:
			// if
			// (conexion.eliminarDespensa(lstProductosDespensa.getAdapter().getItem(info.position).toString()))
			// listarProductosDespensa(conexion);
			// lblMensaje.setText("Etiqueta: Opcion 1 pulsada!");
			return true;
		case R.id.CtxProductoEditar:
			// if
			// (conexion.eliminarDespensa(lstProductosDespensa.getAdapter().getItem(info.position).toString()))
			// listarProductosDespensa(conexion);
			// lblMensaje.setText("Etiqueta: Opcion 1 pulsada!");
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
			menu.setGroupVisible(R.id.OpcGrpAñadirAD, false);
			menu.setGroupVisible(R.id.OpcGrpAñadirAP, false);

			teclaMenu = false;
		} else {

			menu.setGroupVisible(R.id.OpcGrpPrincipal, false);

			switch (resultEstadoCodBarras) {
			case Constantes.ESTADO_BARCODE_DESPENSA:
				menu.setGroupVisible(R.id.OpcGrpProducto, true);
				menu.setGroupVisible(R.id.OpcGrpAñadirAD, false);
				menu.setGroupVisible(R.id.OpcGrpAñadirAP, false);
				break;
			case Constantes.ESTADO_BARCODE_PRODUCTOS:
				menu.setGroupVisible(R.id.OpcGrpProducto, false);
				menu.setGroupVisible(R.id.OpcGrpAñadirAD, true);
				menu.setGroupVisible(R.id.OpcGrpAñadirAP, false);
				break;
			case Constantes.ESTADO_BARCODE_NO_EXISTE:
				menu.setGroupVisible(R.id.OpcGrpProducto, false);
				menu.setGroupVisible(R.id.OpcGrpAñadirAD, false);
				menu.setGroupVisible(R.id.OpcGrpAñadirAP, true);
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
			conexion.actualizarStockXCode(barCodeFinal, Var.despensaSelec.getId(), 1);
			listarProductosDespensa();
			// lblMensaje.setText("Etiqueta: Opcion 1 pulsada!");
			return true;
		case R.id.OpcResta1:
			conexion.actualizarStockXCode(barCodeFinal, Var.despensaSelec.getId(), -1);
			listarProductosDespensa();
			// lblMensaje.setText("Etiqueta: Opcion 1 pulsada!");
			return true;
		case R.id.OpcEliminar:
			// lblMensaje.setText("Etiqueta: Opcion 1 pulsada!");
			return true;
		case R.id.OpcEditar:
			// lblMensaje.setText("Etiqueta: Opcion 1 pulsada!");
			return true;
		case R.id.OpcAñadirAD:
			añadirProductoADespensa(this.barCodeFinal);
			// lblMensaje.setText("Etiqueta: Opcion 1 pulsada!");
			return true;
		case R.id.OpcAñadirAP:
			añadirProducto(this.barCodeFinal);
			// lblMensaje.setText("Etiqueta: Opcion 1 pulsada!");
			return true;
		case R.id.OpcScan:
			scanCode();
			return true;
		case R.id.OpcListaCompra:
			conexion.generarListaCompra(Var.despensaSelec.getId());
			return true;
		case R.id.OpcNuevoProducto:
			añadirProducto("");
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	private void añadirProductoADespensa(String barCode) {
		// TODO Auto-generated method stub
		Log.d(Constantes.LOG_TAG, "añadirProductoADespensa() - barCode: "
				+ barCode);

		String toastMensaje = "";
		long idProducto = conexion.getIdProductoXBarCode(barCode);
		Log.d(Constantes.LOG_TAG, "añadirProductoADespensa() - idProducto: "
				+ idProducto);
		if (idProducto > 0)
			if (conexion.insertarProductoADespensa(idProducto, Var.despensaSelec.getId(),1,0))
				toastMensaje= "Producto en despensa";
			else
				toastMensaje = "Error al añadirlo";
		else
			toastMensaje = "No existe \nproducto";
	
		Toast.makeText(ContenidoDespensa.this, toastMensaje,Toast.LENGTH_SHORT).show();
	}

	private void añadirProducto(String barCode) {
		Log.d(Constantes.LOG_TAG, "añadirProducto() - barCode: " + barCode);
		// TODO Auto-generated method stub
		Intent intent = new Intent(ContenidoDespensa.this, EditProducto.class);

		Bundle bundle = new Bundle();
		bundle.putString(Constantes.BARCODE, barCode);
		bundle.putInt(Constantes.ACCION, Constantes.ACCION_ADD);
		intent.putExtras(bundle);
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

		resultEstadoCodBarras = conexion.buscarCodBarras(codBarras,
				Var.despensaSelec.getId());
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
		Log.d(Constantes.LOG_TAG, "scanCode()");

		IntentIntegrator.initiateScan(ContenidoDespensa.this);

	}

	/* Here is where we come back after the Barcode Scanner is done */
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		Log.d(Constantes.LOG_TAG, "ContenidoDespensa.java - onActivityResult()");

		IntentResult scanResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, intent);
		if (scanResult != null) {

			// handle scan result
			String contents = intent.getStringExtra("SCAN_RESULT");
			// String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
			// Handle successful scan
			buscarProducto(contents);
		} else {
			// else continue with any other code you need in the method
			Toast.makeText(ContenidoDespensa.this, "Cancelado", Toast.LENGTH_SHORT)
					.show();

		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.d(Constantes.LOG_TAG, "onKeyDown() - keyCode: " + keyCode
				+ " Event" + event);

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

	public void listarProductosDespensa() {
		Log.d(Constantes.LOG_TAG,"listarProductosDespensas() - ");
		
		listProductosDespensa = conexion.getProductosDespensa(Var.despensaSelec.getId());
		AdaptadorProductosDespensa adaptador;
		lstProductosDespensa = (ListView) findViewById(R.id.LstProductosDespensa);
		
		Log.d(Constantes.LOG_TAG,"listarProductosDespensa() - Antes del if null");

		if (listProductosDespensa[0].getNombre() != null) {
			Log.d(Constantes.LOG_TAG, "listarProductosDespensas() - Existen productos en despensa");

			adaptador = new AdaptadorProductosDespensa(this);
			Log.d(Constantes.LOG_TAG,"listarProductosdespensa() - despues de init adaptador");

			lstProductosDespensa.setVisibility(View.VISIBLE);
			registerForContextMenu(lstProductosDespensa);

			lstProductosDespensa
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
							Toast.makeText(ContenidoDespensa.this,
									"Producto: "+listProductosDespensa[position].getNombre(),
									Toast.LENGTH_LONG).show();

						}
					});
		} else {
			Log.d(Constantes.LOG_TAG,"listarProductosDespensas() - No existen productos en despensa");
			adaptador = new AdaptadorProductosDespensa(this);
			lstProductosDespensa.setVisibility(View.INVISIBLE);
			Toast.makeText(ContenidoDespensa.this,
					"No existe ningun \nproducto en despensa",
					Toast.LENGTH_LONG).show();
		}

		lstProductosDespensa.setAdapter(adaptador);

	}//Fin listarProductosDespensa

	@SuppressWarnings("rawtypes")
	class AdaptadorProductosDespensa extends ArrayAdapter {
		
		Activity context;

		@SuppressWarnings("unchecked")
		AdaptadorProductosDespensa(Activity context) {
			super(context, R.layout.listitem_despensa, listProductosDespensa);
			this.context = context;
		}

		public View getView(final int position, View convertView, ViewGroup parent) {
			Log.d(Constantes.LOG_TAG,"AdaptadorProductosDespensa() - getView()");
			LayoutInflater inflater = context.getLayoutInflater();
			View item = inflater.inflate(R.layout.listitem_despensa, null);
			
			final long idProducto = listProductosDespensa[position].getIdProducto();
			
			TextView lblNombreP = (TextView) item.findViewById(R.id.LblItemDespensaNombre);
			lblNombreP.setText(listProductosDespensa[position].getNombre());
			TextView lblCantidadP = (TextView) item.findViewById(R.id.LblItemDespensaCantidad);
			ImageView imgSuma = (ImageView)item.findViewById(R.id.ImgItemDespensaSuma);
			ImageView imgResta = (ImageView)item.findViewById(R.id.ImgItemDespensaResta);
			ImageView imgEliminar = (ImageView)item.findViewById(R.id.ImgItemDespensaEliminar);
			
			int stock = listProductosDespensa[position].getStock();
			lblCantidadP.setText(Integer.toString(stock));
			if (listProductosDespensa[position].getStock() <= listProductosDespensa[position].getStockMin())
				lblCantidadP.setTextColor(Color.RED);
						
			//Evento al hacer click a un item
			item.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Toast.makeText(ContenidoDespensa.this,
							"Producto: "+listProductosDespensa[position].getNombre(),
							Toast.LENGTH_LONG).show();
				}
			});
			
			item.setOnLongClickListener(new View.OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					// TODO Auto-generated method stub
					//Implementar alertDialog para editar el producto.
					registerForContextMenu(lstProductosDespensa);
					return false;
				}
			});
			
			imgSuma.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					conexion.actualizarStockXidProducto(idProducto, Var.despensaSelec.getId(), 1);
					listarProductosDespensa();
				}
			});
			
			imgResta.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					conexion.actualizarStockXidProducto(idProducto, Var.despensaSelec.getId(), -1);
					listarProductosDespensa();
				}
			});
			
			imgEliminar.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
//					conexion.actualizarStockXidProducto(idProducto, Var.despensaSelec.getId(), -1);
					listarProductosDespensa();
				}
			});
			
			
			return (item);
		}
	}

}
