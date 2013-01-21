package es.mentor.act8.despensa.ui;

import es.marquesgomez.R;
import es.mentor.act8.despensa.Constantes;
import es.mentor.act8.despensa.Var;
import es.mentor.act8.despensa.database.BaseDeDatos;
import es.mentor.act8.despensa.model.Despensa;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
//import android.os.Vibrator;
import android.text.Editable;
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
	
	private BaseDeDatos conexion;
	private ListView lstOpciones;
	private Despensa[] listDespensas;

	
    @Override
    public void onCreate(Bundle savedInstanceState) {    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Log.d(Constantes.LOG_TAG,ListaDespensas.class.getName()+" onCreate()");

        Var.conexion = new BaseDeDatos(this, "DBDespensa", null, 1);
        this.conexion = Var.conexion;
        
        listarDespensas();
        
        final Button btmAdd = (Button)findViewById(R.id.BtnAdd);
        
        btmAdd.setOnClickListener(new View.OnClickListener(){
        	public void onClick(View arg0){
        		addDespensa();
        	}
        });
        
    } //Fin onCreate
    
    /**
     * Menú Principal: Opción de añadir nueva despensa.
     * 
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	
    	MenuInflater inflater = getMenuInflater();

		inflater.inflate(R.menu.menu_opc_main, menu);

	    return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
		    case R.id.OpcMainConfig:
			    // TODO : Implementar los parámetros de configuración de la aplicación
		        
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
    	
    	//TODO: Implementar la opción de duplicar una despensa
	    AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	    switch (item.getItemId()) {
	    case R.id.CtxDuplicarDespensa:
	    	duplicarDespensa(listDespensas[info.position]);
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
	    
	    
        if (listDespensas[0].getId() != 0){
        	Log.d(Constantes.LOG_TAG,"listarDespensas() - Existen despensas");
        	
        	adaptador = new AdaptadorListaDespensas(this);
        	registerForContextMenu(lstOpciones);
        	lstOpciones.setVisibility(View.VISIBLE);
        	txtBackground.setVisibility(ListView.GONE);
        	
	        lstOpciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	        	public void onItemClick(AdapterView<?> a, View v, int position, long id) {
	        		//Acciones necesarias al hacer click
	        		Intent intent = new Intent(ListaDespensas.this, ContenidoDespensa.class);
	        		Var.despensaSelec = listDespensas[position];
	        		startActivity(intent);
	
	        	}
	        	});
	        
        } else {
        	Log.d(Constantes.LOG_TAG,"listarDespensas() - No existen despensas");
        	adaptador = new AdaptadorListaDespensas(this);
        	lstOpciones.setVisibility(View.INVISIBLE);
        	txtBackground.setVisibility(ListView.VISIBLE);
        }
        
        lstOpciones.setAdapter(adaptador);
    } //Final listarDespensas
    
    private void addDespensa(){
    	//AlertDialog para insertar una nueva despensa 
        final EditText input = new EditText(this);
        input.setSingleLine(true);
        input.setHint(R.string.hint_add);

    	new AlertDialog.Builder(ListaDespensas.this)
         .setTitle(R.string.tittle_add)
         .setPositiveButton(R.string.add,
          new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int which) {
        	  if (conexion.addDespensa(input.getText().toString().trim())>0){
        		  Toast.makeText(ListaDespensas.this, "Despensa añadida", Toast.LENGTH_SHORT).show();
        		  listarDespensas();
        	  } else {
        		  Toast.makeText(ListaDespensas.this, "Error al añadir \nla despensa", Toast.LENGTH_SHORT).show();
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
				
				public void onClick(View v) {
					// TODO: Implementar la edicion de la despensa
					//AlertDialog para editar una nueva despensa 
//			        final EditText input = new EditText(context);
//			        input.setSingleLine(true);
//			        input.setHint(R.string.hint_añadir);
//
//			    	new AlertDialog.Builder(ListaDespensas.this)
//			         .setTitle(R.string.tittle_añadir)
//			         .setPositiveButton(R.string.añadir,
//			          new DialogInterface.OnClickListener() {
//			          public void onClick(DialogInterface dialog, int which) {
//			        	  if (conexion.añadirDespensa(input.getText().toString().trim())>0){
//			        		  Toast.makeText(ListaDespensas.this, "Despensa añadida", Toast.LENGTH_SHORT).show();
//			        		  listarDespensas();
//			        	  } else {
//			        		  Toast.makeText(ListaDespensas.this, "Error al añadir \nla despensa", Toast.LENGTH_SHORT).show();
//			        	  }
//			           }
//			           }).setView(input).setNegativeButton(R.string.cancelar, null).show();
			    	//
			    	AlertDialog.Builder alert = new AlertDialog.Builder(context);

                    alert.setTitle("Editar "+listDespensas[position].getNombre());

                    // Set an EditText view to get user input
                    final EditText input = new EditText(context);
                    input.setText(listDespensas[position].getNombre());
                    alert.setView(input);

                    alert.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                            Editable value = input.getText();
                            if (value.toString().equals(""))
                                return;

                            Despensa itemToEdit = listDespensas[position];
                            itemToEdit.setNombre(value.toString().trim());
//                            update(itemToEdit);
                            if (conexion.updateDespensa(itemToEdit))
                            	listarDespensas();
                        }
                    });

                    alert.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    });

                    alert.show();
				}
			});
			imgEliminar.setOnClickListener(new View.OnClickListener() {
				
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
					listarDespensas();
				}
			});
			return item;
		}
    	
    }

    private void duplicarDespensa(final Despensa despensaActual){
    	
    	final Despensa despensaNueva = new Despensa();
    	//AlertDialog para insertar una nueva despensa 
        final EditText input = new EditText(this);
        input.setSingleLine(true);
        input.setHint("Nueva despensa");

    	new AlertDialog.Builder(ListaDespensas.this)
         .setTitle("Duplicar despensa")
         .setPositiveButton(R.string.add,
          new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int which) {
        	  
        	  despensaNueva.setNombre(input.getText().toString().trim());
        	  if (conexion.duplicarDespensa(despensaActual,despensaNueva)){
        		  Toast.makeText(ListaDespensas.this, "Despensa añadida", Toast.LENGTH_SHORT).show();
        		  listarDespensas();
        	  } else {
        		  Toast.makeText(ListaDespensas.this, "Error al duplicar la despensa", Toast.LENGTH_SHORT).show();
        	  }
           }
           }).setView(input).setNegativeButton(R.string.cancelar, null).show();
    	
    }
} //Final class ListaDespensas