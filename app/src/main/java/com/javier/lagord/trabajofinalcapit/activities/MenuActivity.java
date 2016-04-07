package com.javier.lagord.trabajofinalcapit.activities;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import com.javier.lagord.trabajofinalcapit.R;

public class MenuActivity extends Activity {

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;

		switch (item.getItemId()) {
		case R.id.menu_cursos:
			intent = new Intent(this, CursosActivity.class);
			break;
		case R.id.menu_mis_cursos:
			intent = new Intent(this, MisCursosActivity.class);
			break;
		default:
			intent = new Intent(this, MapaITActivity.class);
			break;
		}

		startActivity(intent);
		finish();
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * To be called by subclasses to disable the menu option to switch to the current Activity
	 */
	protected boolean onPrepareOptionsMenuDisableItem(Menu menu, int id) {
		MenuItem item = menu.findItem(id);
		item.setEnabled(false);
		return super.onPrepareOptionsMenu(menu);
	}
}
