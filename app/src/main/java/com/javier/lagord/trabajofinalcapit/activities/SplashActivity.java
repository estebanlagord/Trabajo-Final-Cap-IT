package com.javier.lagord.trabajofinalcapit.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.javier.lagord.trabajofinalcapit.R;
import com.javier.lagord.trabajofinalcapit.backend.Backend;

public class SplashActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_activity);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		new DownloadCursosTask().execute();
	}
	
	private class DownloadCursosTask extends AsyncTask<Void, Boolean, Boolean> 
    {   
    	@Override
		protected Boolean doInBackground(Void... params) 
		{		
    		boolean success = Backend.getInstance().loadCursosWithGSON(SplashActivity.this);
			return success;
		}

		@Override
        protected void onPostExecute(final Boolean wasUpdateSuccessful) 
        {
        	if (!wasUpdateSuccessful){
        		String msg = "No se pudo descargar la lista de cursos de internet";
        		Log.w("Splash", msg);
        		Toast.makeText(SplashActivity.this, msg, Toast.LENGTH_SHORT).show();
        	}
			
			Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
    		startActivity(intent);
    		finish();
        }
    }
}
