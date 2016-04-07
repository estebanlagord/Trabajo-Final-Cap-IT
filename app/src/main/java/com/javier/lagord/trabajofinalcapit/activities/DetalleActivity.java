package com.javier.lagord.trabajofinalcapit.activities;

import java.io.InputStream;
import java.net.URL;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.javier.lagord.trabajofinalcapit.R;
import com.javier.lagord.trabajofinalcapit.backend.Backend;
import com.javier.lagord.trabajofinalcapit.database.DatabaseHelper;
import com.javier.lagord.trabajofinalcapit.model.Curso;

public class DetalleActivity extends Activity {
	Curso course;
	String username;
	ProgressBar progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detalle_activity);
		
		course = (Curso)getIntent().getSerializableExtra("course");
		username = Backend.getInstance().getCurrentUsername(this);
		
		progress = (ProgressBar)findViewById(R.id.progressBarDetail);
		
		TextView nameView = (TextView)findViewById(R.id.textDetailName);
		nameView.setText(course.getNombre());
		
		TextView descriptionView = (TextView)findViewById(R.id.textDetailDescription);
		descriptionView.setText(course.getDescripcion());
		
		TextView numberOfHoursView = (TextView)findViewById(R.id.textNumberOfHours);
		numberOfHoursView.setText(course.getCarga_horaria());
		
		TextView modalidadView = (TextView)findViewById(R.id.textModalidad);
		modalidadView.setText(course.getModalidad());
		
		TextView startDateView = (TextView)findViewById(R.id.textStartDate);
		startDateView.setText(course.getComienzo());
		
		Spinner spinner = (Spinner)findViewById(R.id.spinnerTimeChoice);
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{course.getDias_horarios()}); 
		spinner.setAdapter(spinnerAdapter);
		
		updateEnrollmentButton();
		
		// download image
		new DownloadImageTask().execute(course.getImagen());
	}
	
	
	
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> 
    {   
    	@Override
        protected void onPostExecute(final Bitmap result) 
        {
    		// Make the image twice as big
    		Bitmap resizedBitmap = Bitmap.createScaledBitmap(result, result.getWidth() * 2, result.getHeight() * 2, true);
    		
    		ImageView downloadedImage = (ImageView)findViewById(R.id.imageViewDetail);
        	downloadedImage.setImageBitmap(resizedBitmap);
        	
        	progress.setVisibility(View.GONE);
        	downloadedImage.setVisibility(View.VISIBLE);
			
			Animation animAlphaIn = AnimationUtils.loadAnimation(DetalleActivity.this, R.anim.alpha_in);
	    	downloadedImage.startAnimation(animAlphaIn);
        }

    	@Override
		protected void onPreExecute() 
		{	
			progress.setVisibility(View.VISIBLE);
		}

    	@Override
		protected Bitmap doInBackground(String... urlStr) 
		{		
    		
    		Bitmap result = null;
    		try {
    			InputStream content = (InputStream)new URL(urlStr[0]).getContent();
				result = BitmapFactory.decodeStream(content);
    		} catch(Exception e) {
    			Log.w("Detalle", "No se pudo descargar la imagen para el curso desde la URL: " + urlStr[0]);
    			e.printStackTrace();
    			// some images fail to download from the URL provided by the web service 
    			// default to Capacitacion IT logo in that case
    			result = BitmapFactory.decodeResource(getResources(), R.drawable.capacitacion_it_logo);
    		}
			return result;
		}
    }
	
	private void updateEnrollmentButton(){
		Button button = (Button)findViewById(R.id.buttonEnrollment);
		if (DatabaseHelper.getDatabaseInstance(this).isUserEnrolledInCourse(username, course.getNombre())){
			button.setText("Eliminar Inscripcion");
		} else {
			button.setText("Inscribirse");
		}
	}
	
	public void onEnrollmentButtonClicked(View v){
		DatabaseHelper db = DatabaseHelper.getDatabaseInstance(this);
		
		if (db.isUserEnrolledInCourse(username, course.getNombre())){
			db.unenrollUserToCourse(username, course.getNombre());
		} else {
			db.enrollUserToCourse(username, course.getNombre());
		}
		updateEnrollmentButton();
	}
}
