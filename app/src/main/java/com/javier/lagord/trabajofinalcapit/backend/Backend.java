package com.javier.lagord.trabajofinalcapit.backend;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.javier.lagord.trabajofinalcapit.database.DatabaseHelper;
import com.javier.lagord.trabajofinalcapit.model.Curso;

	
public class Backend extends Object
{
	private static final String PREF_USERNAME = "username";
	private static Backend instance;
	
	private Backend() {
		super();
	}


	public static Backend getInstance()
	{
		if(instance == null)
		{
			instance = new Backend();
		}
		return instance;		
	}	
	
	
	private InputStreamReader getCursosFromServer()
	{	
		HttpResponse httpResponse;
		
		InputStreamReader response = null;
			
		HttpClient httpclient = new DefaultHttpClient();  
		
        try 
        {  
        	//httpResponse = httpclient.execute(new HttpGet("http://maps.googleapis.com/maps/api/directions/json?origin=Toronto&destination=Montreal&sensor=false"));
        	httpResponse = httpclient.execute(new HttpGet("http://www.black-tobacco.com/lab/android/getCursos"));
            response = new InputStreamReader(httpResponse.getEntity().getContent(), "ISO-8859-1");
        }
        catch (IOException e) 
        {  
			e.printStackTrace();
        }  
        
        return response;
	}
	
	
	/** Loads Cursos from the Internet and stores them in the DB
	 */
	public boolean loadCursosWithGSON(Context context)
	{
		boolean success = true;
		Log.d("Backend", "Iniciando descarga de cursos");
		InputStreamReader response = getCursosFromServer();
		
		if(response != null)
		{	
			Gson gson = new Gson();
			Curso[] cursos = new Curso[0];
			try {
				cursos = gson.fromJson(response, Curso[].class);
			} catch (Exception e) {
				Log.e("Backend", "Error parsing Json response", e);
				return false;
			}

			for (Curso cur : cursos) {
				// Sacar el '.' del final del nombre
				String nombre = cur.getNombre();
				if (nombre != null && nombre.endsWith("."))
					cur.setNombre(nombre.substring(0, nombre.length()-1));
				Log.d("Curso", cur.getNombre());
			}

			DatabaseHelper.getDatabaseInstance(context).updateAllCourses(Arrays.asList(cursos));
		} else {
			success = false;
		}
		
		return success;
	}

	
	public void setCurrentUsername(Context context, String username){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = prefs.edit();
		editor.putString(PREF_USERNAME, username);
		editor.commit();
		Log.d("Backend", "Actualizando usuario: " + username);
	}
	
	public String getCurrentUsername(Context context){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String username = prefs.getString(PREF_USERNAME, null);
		
		if (username == null){
			String msg = "No se pudo acceder al nombre de usuario. Por favor reiniciar la aplicación";
			Log.e("Backend", msg);
			throw new IllegalStateException(msg);
		} else{
			Log.d("Backend", "Usuario actual: " + username);
			return username;
		}
	}
}
