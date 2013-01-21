package es.mentor.act8.despensa;

import android.app.Application;
import es.mentor.act8.despensa.database.BaseDeDatos;
import es.mentor.act8.despensa.model.Despensa;

public final class App extends Application{
	
	private static BaseDeDatos database;
	public static Despensa despensaSelec;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		database = new BaseDeDatos(this);
		
	}

	public static BaseDeDatos getDatabase() {
		return database;
	}

}
