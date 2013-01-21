package es.mentor.act8.despensa.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import es.mentor.act8.despensa.App;
import es.mentor.act8.despensa.Constantes;
import es.mentor.act8.despensa.R;
import es.mentor.act8.despensa.model.Despensa;

public class ListaDespensas extends ListActivity {
	
	private ArrayList<Despensa> listDespensas;
	private AdaptadorListaDespensas mAdaptador;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Log.d(Constantes.LOG_TAG,ListaDespensas.class.getName()+" onCreate()");

        listDespensas = new ArrayList<Despensa>();
        mAdaptador = new AdaptadorListaDespensas(this,listDespensas);
	    
        registerForContextMenu(getListView());
        setListAdapter(mAdaptador);
        
        listarDespensas();
        
        final Button btmAdd = (Button)findViewById(R.id.BtnAdd);
        
        btmAdd.setOnClickListener(new View.OnClickListener(){
        	public void onClick(View arg0){
        		addDespensa();
        	}
        });
        
    } //Fin onCreate
    
    /**
     * Menœ Principal: Opci—n de a–adir nueva despensa.
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
			    // TODO : Implementar los parÃ¡metros de configuraciÃ³n de la aplicaciÃ³n
		        
			    return true;
		    default:
		    	return super.onOptionsItemSelected(item);
	    }
    }
    
    /**
     * MenÃº Contextual
     * 
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo){
	    	
	    super.onCreateContextMenu(menu, v, menuInfo);
	    MenuInflater inflater = getMenuInflater();
	    
	    if (v.getId()==android.R.id.list){
	    	AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
	    	menu.setHeaderTitle(getListAdapter().getItem(info.position).toString());

	    	inflater.inflate(R.menu.menu_ctx_principal, menu);
	    }
	    
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	
	    AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	    switch (item.getItemId()) {
	    case R.id.CtxDuplicarDespensa:
	    	duplicarDespensa(listDespensas.get(info.position));
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
    	
    	listDespensas.clear();
    	listDespensas.addAll(App.getDatabase().getDespensas());
    	mAdaptador.notifyDataSetChanged();
    } //Final listarDespensas
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	Intent intent = new Intent(ListaDespensas.this, ContenidoDespensa.class);
		App.despensaSelec = listDespensas.get(position);
		startActivity(intent);
    }
    
    private void addDespensa(){
    	//AlertDialog para insertar una nueva despensa 
        final EditText input = new EditText(this);
        input.setSingleLine(true);
        input.setHint(R.string.hint_name);

    	new AlertDialog.Builder(ListaDespensas.this)
         .setTitle(R.string.dialog_title_new_despensa)
         .setPositiveButton(R.string.add,
          new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int which) {
        	  if (App.getDatabase().addDespensa(input.getText().toString().trim())>0){
        		  listarDespensas();
        	  } else {
        		  Toast.makeText(ListaDespensas.this, R.string.msg_error_add_despensa, Toast.LENGTH_SHORT).show();
        	  }
           }
           }).setView(input).setNegativeButton(R.string.cancelar, null).show();
    }
    
	class AdaptadorListaDespensas extends ArrayAdapter<Despensa> {

    	Activity context;
    	
		public AdaptadorListaDespensas(Activity context, ArrayList<Despensa> objects) {
			super(context,R.layout.listitem_despensa,objects);
			this.context=context;
			
		}
		
		public View getView(final int position, View convertView, ViewGroup parent) {
			Log.d(Constantes.LOG_TAG,"AdaptadorListaDespensas() - getView()");
			LayoutInflater inflater = context.getLayoutInflater();
			View item = inflater.inflate(R.layout.despensa_list_item, null);
			
			final Despensa despensaItem = getItem(position);
			
			TextView lblNombreDespensa = (TextView)item.findViewById(R.id.LblItemMainNombre);
			lblNombreDespensa.setText(despensaItem.getNombre());
			ImageView imgEditar = (ImageView)item.findViewById(R.id.ImgItemMainEditar);
			ImageView imgEliminar = (ImageView)item.findViewById(R.id.ImgItemMainEliminar);
			
			imgEditar.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
			    	AlertDialog.Builder alert = new AlertDialog.Builder(context);

                    alert.setTitle("Editar "+listDespensas.get(position).getNombre());

                    // Set an EditText view to get user input
                    final EditText input = new EditText(context);
                    input.setText(despensaItem.getNombre());
                    alert.setView(input);

                    alert.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                            Editable value = input.getText();
                            if (value.toString().equals(""))
                                return;

                            despensaItem.setNombre(value.toString().trim());
                            if (App.getDatabase().updateDespensa(despensaItem))
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
					AlertDialog.Builder alert = new AlertDialog.Builder(context);
					
					alert.setTitle(despensaItem.getNombre());
					alert.setMessage("Eliminar?");
					
					alert.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                            if (App.getDatabase().eliminarDespensa(despensaItem))
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
        input.setHint(R.string.hint_name);

    	new AlertDialog.Builder(ListaDespensas.this)
         .setTitle(R.string.dialog_title_clone_despensa)
         .setPositiveButton(R.string.add,
          new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int which) {
        	  
        	  despensaNueva.setNombre(input.getText().toString().trim());
        	  if (App.getDatabase().duplicarDespensa(despensaActual,despensaNueva)){
        		  listarDespensas();
        	  } else {
        		  Toast.makeText(ListaDespensas.this, R.string.msg_error_clone_despensa, Toast.LENGTH_SHORT).show();
        	  }
           }
           }).setView(input).setNegativeButton(R.string.cancelar, null).show();
    	
    }
} //Final class ListaDespensas