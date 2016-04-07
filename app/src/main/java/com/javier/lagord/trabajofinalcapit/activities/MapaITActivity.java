package com.javier.lagord.trabajofinalcapit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.javier.lagord.trabajofinalcapit.R;

public class MapaITActivity extends android.support.v4.app.FragmentActivity {
	private static final LatLng CAP_IT_LOCATION = new LatLng(-31.40947, -64.19537);
	private GoogleMap theMap;
	private int vista = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapa_it_activity);
		
		if(theMap == null)
		{
			theMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

			if(theMap != null)
			{
				theMap.setMyLocationEnabled(true);
				
				Marker marker = theMap.addMarker(new MarkerOptions()
					.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))	
					.snippet("Nicolás Avellaneda 240 - Córdoba, Argentina")
					.position(CAP_IT_LOCATION)
		        	.title("Capacitación IT"));
		        	
				marker.showInfoWindow();
				
				goToCapIt(false);
			}
		}
	}

	private void goToCapIt(boolean animate) {
		CameraUpdate camUpd = CameraUpdateFactory.newLatLngZoom(CAP_IT_LOCATION, 16F);
		
		if (animate)
			theMap.animateCamera(camUpd);
		else
			theMap.moveCamera(camUpd);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;

		switch (item.getItemId()) {
		case R.id.menu_cursos:
			intent = new Intent(this, CursosActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.menu_mis_cursos:
			intent = new Intent(this, MisCursosActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.menu_cap_it:
			goToCapIt(true);
			break;
		case R.id.menu_vista:
			alternarVista();
			break;
		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}
	
	private void alternarVista()
	{
		vista = (vista + 1) % 3;
		
		switch(vista)
		{
			case 0:
				theMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				break;
			case 1:
				theMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
				break;
			case 2:
				theMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
				break;
		}
	}
}
