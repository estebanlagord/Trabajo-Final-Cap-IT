package com.javier.lagord.trabajofinalcapit.activities;

import java.util.List;

import android.view.Menu;
import android.widget.ListView;

import com.javier.lagord.trabajofinalcapit.R;
import com.javier.lagord.trabajofinalcapit.adapter.CursosAdapter;
import com.javier.lagord.trabajofinalcapit.backend.Backend;
import com.javier.lagord.trabajofinalcapit.database.DatabaseHelper;
import com.javier.lagord.trabajofinalcapit.model.Curso;

public class MisCursosActivity extends CursosActivity {
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenuDisableItem(menu, R.id.menu_mis_cursos);
	}
	

	@Override
	protected void onResume() {
		super.onResume();
		String username = Backend.getInstance().getCurrentUsername(this);
		List<Curso> cursosForUser = DatabaseHelper.getDatabaseInstance(this).getCoursesForUser(username);
		
		ListView list = (ListView)findViewById(R.id.listViewCursos);
		
		CursosAdapter adapter = (CursosAdapter)list.getAdapter();
		adapter.updateCursosList(cursosForUser);
		
		hideOrDisplayEmptyListMessage(cursosForUser, "No estas inscripto en ningún curso");
	}
}
