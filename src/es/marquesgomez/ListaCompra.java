package es.marquesgomez;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

public class ListaCompra extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		Log.d(Constantes.LOG_TAG, "ListaCompra.java - onCreate()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lista_compra);
	}
	
	// EVITAR QUE NUESTRA ACTIVIDAD COMIENCE DE NUEVO AL ROTAR LA PANTALLA
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

}