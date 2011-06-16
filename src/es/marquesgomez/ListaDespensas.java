package es.marquesgomez;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
//import android.os.Vibrator;
import android.util.Log;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListaDespensas extends Activity {
    /** Called when the activity is first created. */
	
	private static final int MnuAñadir = 1;
	private BaseDeDatos conexion;
	private ListView lstOpciones;
//	private EditText txtNombre;
	private Despensa[] listDespensas;

	
    @Override
    public void onCreate(Bundle savedInstanceState) {    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Log.d(Constantes.LOG_TAG,ListaDespensas.class.getName()+" onCreate()");

        //final BaseDeDatos conexion = new BaseDeDatos(this, "DBDespensa", null, 1);
        Var.conexion = new BaseDeDatos(this, "DBDespensa", null, 1);
        this.conexion = Var.conexion;
        
//        vibrator = (Vibrator) getSystemService(HolaUsuario.VIBRATOR_SERVICE);
//        lstOpciones = (ListView)findViewById(R.id.LstOpciones);
        listarDespensas();
        
        //final EditText txtNombre = (EditText)findViewById(R.id.TxtNombre);
//        txtNombre = (EditText)findViewById(R.id.TxtNombre);
        final Button btmAñadir = (Button)findViewById(R.id.BtnAñadir);
        
        btmAñadir.setOnClickListener(new View.OnClickListener(){
        	public void onClick(View arg0){
        		añadirDespensa();
        	}
        });
//        
//        btnEliminar.setOnClickListener(new View.OnClickListener(){
//        	public void onClick(View arg0){
//        		
//        		if (txtNombre.getText().toString().trim().length() > 0){
//        			
//        			if (conexion.eliminarDespensa(txtNombre.getText().toString().trim())){
//        				
//        				listarDespensas();
//        				txtNombre.setText("");
//        				
//        			} else {
//        				Toast.makeText(ListaDespensas.this,"No se ha podido eliminar",Toast.LENGTH_SHORT).show();
//        			}
//        		} else {
//        			Toast.makeText(ListaDespensas.this,"Introduce o selecciona el nombre de la despensa",Toast.LENGTH_SHORT).show();        		
//        		}
//        	}
//        });
        
        

     
        

    } //Fin onCreate
    
    /**
     * Menú Principal: Opción de añadir nueva despensa.
     * 
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	    //Activa el menu
	    //MenuInflater inflater = getMenuInflater();
	    //inflater.inflate(R.menu.menu_principal, menu);
    	menu.add(Menu.NONE, MnuAñadir, Menu.NONE, "Añadir");

	    return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
		    case MnuAñadir:
			    
		    	añadirDespensa();
		        
			    return true;
		    default:
		    	return super.onOptionsItemSelected(item);
	    }
    }
    
    /**
     * Menú Contextual
     * 
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo){
	    	
	    super.onCreateContextMenu(menu, v, menuInfo);
	    MenuInflater inflater = getMenuInflater();
	    
	    if (v.getId()==R.id.LstOpciones){
	    	AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
	    	menu.setHeaderTitle(lstOpciones.getAdapter().getItem(info.position).toString());

	    	inflater.inflate(R.menu.menu_ctx_principal, menu);
	    }
	    
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	
	    AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	    switch (item.getItemId()) {
	    case R.id.CtxEliminar:
//	    	if (conexion.eliminarDespensa(lstOpciones.getAdapter().getItem(info.position).toString()))
//	    		listarDespensas();
		    //lblMensaje.setText("Etiqueta: Opcion 1 pulsada!");
		    return true;
    	default:
    		return super.onContextItemSelected(item);
    	}
    }




    /**
     * listarDespensas
     * 
     */
    public void listarDespensas(){
    	Log.d(Constantes.LOG_TAG,"listarDespensas() - ");
    	
    	listDespensas = conexion.getDespensas();
        AdaptadorListaDespensas adaptador;
        lstOpciones = (ListView)findViewById(R.id.LstOpciones);
        TextView txtBackground = (TextView)findViewById(R.id.mainListViewBackgroundText);
        //ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datos);
//	    ListView lstOpciones = (ListView)findViewById(R.id.LstOpciones);
//	    lstOpciones.setAdapter(adaptador);
	    
	    
        if (listDespensas[0].getId() != 0){
        	Log.d(Constantes.LOG_TAG,"listarDespensas() - Existen despensas");
        	
//        	adaptador = new ArrayAdapter<Despensa>(this, android.R.layout.simple_list_item_1, listDespensas);
        	adaptador = new AdaptadorListaDespensas(this);
        	registerForContextMenu(lstOpciones);
        	lstOpciones.setVisibility(View.VISIBLE);
        	txtBackground.setVisibility(ListView.GONE);
        	
        	

	    	
	    	//final EditText txtNombre = (EditText)findViewById(R.id.TxtNombre);
	    	//ListView lstOpciones = (ListView)findViewById(R.id.LstOpciones);
	        lstOpciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	        	@Override
	        	public void onItemClick(AdapterView<?> a, View v, int position, long id) {
	        		//Acciones necesarias al hacer click
	        		Intent intent = new Intent(ListaDespensas.this, ContenidoDespensa.class);
	        		
//	        		Bundle bundle = new Bundle();
//	        		bundle.putString("NOMBRE", datos[position]);
//	        		intent.putExtras(bundle);
//	        		Var.idDespensa = listDespensas[position].getId();
	        		Var.despensaSelec = listDespensas[position];
	        		startActivity(intent);
	
	        	}
	        	});
	    	//Comentado el evento de la pulsación larga porque lo gestiona el onCreateContexMenu()
//	        lstOpciones.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//			
//	        	public boolean onItemLongClick (AdapterView<?> a, View v, int position, long id){
//	        		//Acciones al hacer un click largo
//	        		//boolean result=false;
//	        		vibrator.vibrate(100);
//	        		onCreateOptionsMenu(Menu);
//	        		onCreateContextMenu();
//	        		//txtNombre.setText(datos[position]);
//	        		
//	        		
//	        		return true;
//	        	}
//	        });
        } else {
        	Log.d(Constantes.LOG_TAG,"listarDespensas() - No existen despensas");
        	adaptador = new AdaptadorListaDespensas(this);
        	lstOpciones.setVisibility(View.INVISIBLE);
        	txtBackground.setVisibility(ListView.VISIBLE);
        	Toast.makeText(ListaDespensas.this,"No existe ninguna despensa",Toast.LENGTH_LONG).show();
        }
        
        lstOpciones.setAdapter(adaptador);
    } //Final listarDespensas
    
    private void añadirDespensa(){
    	//AlertDialog para insertar una nueva despensa 
        final EditText input = new EditText(this);
        input.setSingleLine(true);
        input.setHint(R.string.hint_añadir);

    	new AlertDialog.Builder(ListaDespensas.this)
         .setTitle(R.string.tittle_añadir) //.setMessage("Hello!")
         .setPositiveButton(R.string.añadir,
          new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int which) {
        	  if (conexion.añadirDespensa(input.getText().toString().trim())){
        		  Toast.makeText(ListaDespensas.this, "Despensa añadida", Toast.LENGTH_SHORT).show();
        		  listarDespensas();
        	  } else {
        		  Toast.makeText(ListaDespensas.this, "Error al añadir la despensa", Toast.LENGTH_SHORT).show();
        	  }
           }
           }).setView(input).setNegativeButton(R.string.cancelar, null).show();
    }
    
    @SuppressWarnings("rawtypes")
	class AdaptadorListaDespensas extends ArrayAdapter {

    	Activity context;
    	
		@SuppressWarnings("unchecked")
		public AdaptadorListaDespensas(Activity context) {
			// TODO Auto-generated constructor stub
			super(context,R.layout.listitem_despensa,listDespensas);
			this.context=context;
		}
		
		public View getView(final int position, View convertView, ViewGroup parent) {
			Log.d(Constantes.LOG_TAG,"AdaptadorListaDespensas() - getView()");
			LayoutInflater inflater = context.getLayoutInflater();
			View item = inflater.inflate(R.layout.despensa_list_item, null);
			
			TextView lblNombreDespensa = (TextView)item.findViewById(R.id.LblItemMainNombre);
			lblNombreDespensa.setText(listDespensas[position].getNombre());
			ImageView imgEditar = (ImageView)item.findViewById(R.id.ImgItemMainEditar);
			ImageView imgEliminar = (ImageView)item.findViewById(R.id.ImgItemMainEliminar);
			
			imgEditar.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO: Implementar la edicion de la despensa
				}
			});
			imgEliminar.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					AlertDialog.Builder alert = new AlertDialog.Builder(context);
					
					alert.setTitle(listDespensas[position].getNombre());
					alert.setMessage("Eliminar?");
					
					alert.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        	Despensa itemToEdit = listDespensas[position];
//                            if (conexion.eliminarProductoCompra(itemToEdit))
                            if (conexion.eliminarDespensa(itemToEdit))
                            	listarDespensas();
                        }
                    });

                    alert.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    });

                    alert.show();
//					conexion.actualizarStockXidProducto(idProducto, Var.despensaSelec.getId(), -1);
					listarDespensas();
				}
			});
			return item;
		}
    	
    }

} //Final class ListaDespensas