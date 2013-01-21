package es.mentor.act8.despensa.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import es.mentor.act8.despensa.App;
import es.mentor.act8.despensa.Constantes;
import es.mentor.act8.despensa.R;
import es.mentor.act8.despensa.database.BaseDeDatos;
import es.mentor.act8.despensa.model.Producto;
import es.mentor.act8.despensa.model.ProductoDespensa;
import es.mentor.act8.despensa.utils.barcode.IntentIntegrator;
import es.mentor.act8.despensa.utils.barcode.IntentResult;

public class EditProducto extends Activity {
	
	//Declaración variables Globales.
	private BaseDeDatos conexion;
	private View lytStock;
	private EditText txtBarCode;
	private CheckBox chbAddEnDespensa;
	private EditText txtNombre;
	private EditText txtNotas;
	private ImageView imgScan;
	private EditText txtCantidad;
	private EditText txtCantidadMinima;
	private EditText txtCantidadAComprar;
	private ProductoDespensa productoEdit;
	private int accion;

	
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.producto);
        
        Log.d(Constantes.LOG_TAG, "Producto.java - onCreate()");
        
        this.conexion = App.getDatabase();
        
        final Button btnGuardar = (Button)findViewById(R.id.BtnGuardar);
        final Button btnCancelar = (Button)findViewById(R.id.BtnCancelar);
        lytStock = findViewById(R.id.LytStock);
        txtBarCode = (EditText)findViewById(R.id.txtBarCode);
        txtNombre= (EditText)findViewById(R.id.txtNombreP);
        txtNotas= (EditText)findViewById(R.id.txtNotas);
        chbAddEnDespensa = (CheckBox)findViewById(R.id.chbAddEnDespensa); 
        imgScan = (ImageView)findViewById(R.id.imgScanP);
        txtCantidad = (EditText)findViewById(R.id.txtCantidad);
        txtCantidadMinima = (EditText)findViewById(R.id.txtCantidadMinima);
        txtCantidadAComprar = (EditText)findViewById(R.id.txtCantidadAComprar);
        
        Bundle bundle = getIntent().getExtras();
        accion = bundle.getInt(Constantes.ACCION);
        
        switch (accion){
        case (Constantes.ACCION_ADD_DESPENSA):
        	Log.d(Constantes.LOG_TAG, "onCreate() - ACCION ADD DESDE DESPENSA");
        	txtBarCode.setText(bundle.getString(Constantes.BARCODE));
        	lytStock.setVisibility(View.INVISIBLE);
        	break;
        case (Constantes.ACCION_EDIT_DESPENSA):
        	Log.d(Constantes.LOG_TAG, "onCreate() - ACCION EDIT DESDE DESPENSA");
        	long idProducto =  bundle.getLong(Constantes.ID_PRODUCTO_EDIT);
        	
        	productoEdit = conexion.getProductoDespensa(idProducto);
        	
        	txtNombre.setText(productoEdit.getNombre());
        	txtBarCode.setText(productoEdit.getCodigoBarras());
        	txtCantidad.setText(String.valueOf(productoEdit.getStock()));
        	txtCantidadMinima.setText(String.valueOf(productoEdit.getStockMin()));
        	txtCantidadAComprar.setText(String.valueOf(productoEdit.getCantidadAComprar()));
        	chbAddEnDespensa.setChecked(true);
        	findViewById(R.id.chbAddEnDespensa).setEnabled(false);
        	chbAddEnDespensa.setText("Producto en despensa");
        	lytStock.setVisibility(View.VISIBLE);
        	break;
        default:
        		lytStock.setVisibility(View.VISIBLE);
        }
        
        chbAddEnDespensa.setOnCheckedChangeListener(
    			new CheckBox.OnCheckedChangeListener() {
    				
	    			public void onCheckedChanged(CompoundButton buttonView,
	    			boolean isChecked) {
		    			if (isChecked) {
		    				//chbAñadirEnDespensa.setText("Checkbox marcado!");
		    				lytStock.setVisibility(View.VISIBLE);
		    			}
		    			else {
		    				//chbAñadirEnDespensa.setText("Checkbox desmarcado!");
		    				lytStock.setVisibility(View.INVISIBLE);
		    			}
	    			}
    			});
        
        btnGuardar.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (comprobarCampos()){
					switch (accion){
			        case (Constantes.ACCION_ADD_DESPENSA):
			        	Log.d(Constantes.LOG_TAG, "EditProducto.java - Click guardar - ACCION ADD DESDE DESPENSA");
			        	guardarProductoNuevo();
			        	break;
			        case (Constantes.ACCION_EDIT_DESPENSA):
			        	Log.d(Constantes.LOG_TAG, "EditProducto.java - Click guardar - ACCION EDIT DESDE DESPENSA");
			        	guardarProductoEditado();
			        	break;
					}
					
				}
				
			}
		});
        
        btnCancelar.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				finish();
			}
		});
        
        imgScan.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				scanCode();
			}
		});
        
//        txtBarCode.setOnLongClickListener(new View.OnLongClickListener() {
//			
//			@Override
//			public boolean onLongClick(View v) {
//				// TODO Auto-generated method stub
//				scanCode();
//				return true;
//			}
//		});
              
	}
	
	



	//EVITAR QUE NUESTRA ACTIVIDAD COMIENCE DE NUEVO AL ROTAR LA PANTALLA
	@Override
    public void onConfigurationChanged(Configuration newConfig) {
    	super.onConfigurationChanged(newConfig);
    }
	
	protected boolean comprobarCampos() {
		// TODO Auto-generated method stub
		Log.d(Constantes.LOG_TAG, "comprovarCampos()");
		boolean result = false;
		
		if (txtNombre.getText().toString().trim().length() > 0){
			Log.i(Constantes.LOG_TAG, "comprobarCampos() - txtNombre: "+txtNombre.getText()+" length: "+txtNombre.getText().length());
			result = true;
		}
		Log.i(Constantes.LOG_TAG, "comprobarCampos() - return: "+result);
		return result;
		
	}


	protected void guardarProductoNuevo() {
		// TODO Auto-generated method stub
		Log.d(Constantes.LOG_TAG, "guardarProducto()");
		final Producto nuevoProducto = new Producto();
		
		nuevoProducto.setNombre(txtNombre.getText().toString().trim());
		nuevoProducto.setCodigoBarras(txtBarCode.getText().toString().trim());
		nuevoProducto.setNotas(txtNotas.getText().toString().trim());
		
		long idProducto = conexion.insertarProducto(nuevoProducto);
		
		if (idProducto>0){
			if (chbAddEnDespensa.isChecked()){
				
				productoEdit = conexion.getProductoDespensa(idProducto);
				
				productoEdit.setIdDespensa(App.despensaSelec.getId());
				productoEdit.setIdProducto(idProducto);
				productoEdit.setStock(Integer.parseInt(txtCantidad.getText().toString()));
				productoEdit.setStockMin(Integer.parseInt(txtCantidadMinima.getText().toString()));
				productoEdit.setCantidadAComprar(Integer.parseInt(txtCantidadAComprar.getText().toString()));
				
				conexion.insertarProductoADespensa(productoEdit);
			}
			Log.d(Constantes.LOG_TAG, "guardarProducto() - OK");
			Toast.makeText(EditProducto.this,"Producto introducido",Toast.LENGTH_SHORT).show();
			finish();
		} else {
			Log.d(Constantes.LOG_TAG, "guardarProducto() - NO OK");
			Toast.makeText(EditProducto.this,"Producto no \nintroducido",Toast.LENGTH_SHORT).show();
		}
		
		
	}
	
	protected void guardarProductoEditado() {
		Log.d(Constantes.LOG_TAG, "guardarProductoEditado()");
		final Producto productoPEdit = new Producto();
		
		productoEdit.setNombre(txtNombre.getText().toString().trim());
		productoEdit.setCodigoBarras(txtBarCode.getText().toString().trim());
		productoEdit.setStock(Integer.parseInt(txtCantidad.getText().toString()));
		productoEdit.setStockMin(Integer.parseInt(txtCantidadMinima.getText().toString()));
		productoEdit.setCantidadAComprar(Integer.parseInt(txtCantidadAComprar.getText().toString()));
//		productoEdit.setNotas(txtNotas.getText().toString().trim());
		productoPEdit.setId(productoEdit.getIdProducto());
		productoPEdit.setNombre(productoEdit.getNombre());
		if (productoEdit.getCodigoBarras().length()>0)
			productoPEdit.setCodigoBarras(productoEdit.getCodigoBarras());
		else 
			productoPEdit.setCodigoBarras(null);
		
		
		
		String msg;
		if (conexion.updateProducto(productoPEdit)){
			if (conexion.updateProductoDespensa(productoEdit)){
//				int stock = Integer.parseInt(txtCantidad.getText().toString());
//				int stockMin = Integer.parseInt(txtCantidadMinima.getText().toString());
//				conexion.insertarProductoADespensa(idProducto, Var.despensaSelec.getId(), stock, stockMin);
			}
			Log.d(Constantes.LOG_TAG, "guardarProductoEditado() - OK");
			msg="Producto \nactualizado";
			finish();
			
		} else {
			Log.d(Constantes.LOG_TAG, "guardarProductoEditado() - NO OK");
			msg="Producto no \nactualizado";
		}
		
		Toast.makeText(EditProducto.this,msg,Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * scanCode: Llama a Barcode Scanner desde IntentIntegrator.
	 */
	public void scanCode() {
		Log.d(Constantes.LOG_TAG, "scanCode()");

		IntentIntegrator.initiateScan(this);

	}

	/* Here is where we come back after the Barcode Scanner is done */
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		Log.d(Constantes.LOG_TAG, "EditProducto.java - onActivityResult()");

		IntentResult scanResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, intent);
		if (scanResult.getContents() != null) {

			// handle scan result
			String contents = intent.getStringExtra("SCAN_RESULT");
			// String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
			// Handle successful scan
			txtBarCode.setText(contents);
		} else {
			// else continue with any other code you need in the method
			Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT)
					.show();

		}
	}
		
}
