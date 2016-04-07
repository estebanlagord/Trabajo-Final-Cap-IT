package com.javier.lagord.trabajofinalcapit.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.javier.lagord.trabajofinalcapit.R;
import com.javier.lagord.trabajofinalcapit.adapter.CursosAdapter;
import com.javier.lagord.trabajofinalcapit.database.DatabaseHelper;
import com.javier.lagord.trabajofinalcapit.model.Curso;

public class CursosActivity extends MenuActivity {
	private ListView list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cursos_activity);
		
		list = (ListView)findViewById(R.id.listViewCursos);
		
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Curso curso = (Curso)parent.getAdapter().getItem(position);
				Intent intent  = new Intent(CursosActivity.this, DetalleActivity.class);
				intent.putExtra("course", curso);
				startActivity(intent);
			}
		});
		
		list.setAdapter(new CursosAdapter(CursosActivity.this, new ArrayList<Curso>()));
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		List<Curso> allCourses = DatabaseHelper.getDatabaseInstance(this).getAllCourses();
		
		CursosAdapter adapter = (CursosAdapter)list.getAdapter();
		adapter.updateCursosList(allCourses);
		hideOrDisplayEmptyListMessage(allCourses, null);
	}
	
	/**
	 * When the list parameter is empty, the message is displayed to the user.
	 * @param list
	 * @param message optional. When null, the default will be used.
	 */
	protected void hideOrDisplayEmptyListMessage(List<?> list, String message){
		TextView messageView = (TextView)findViewById(R.id.textViewListIsEmptyMessage);

		if (list.isEmpty()){
			if (message != null)
				messageView.setText(message);
			
			messageView.setVisibility(View.VISIBLE);
		} else{
			messageView.setVisibility(View.GONE);
		}
	}
	
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenuDisableItem(menu, R.id.menu_cursos);
	}
}
