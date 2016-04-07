package com.javier.lagord.trabajofinalcapit.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.javier.lagord.trabajofinalcapit.R;
import com.javier.lagord.trabajofinalcapit.model.Curso;

public class CursosAdapter extends BaseAdapter {

	Context context;
	List<Curso> courses;
	LayoutInflater inflater;
	
	public CursosAdapter(Context context, List<Curso> courses) {
		this.context = context;
		this.courses = courses;
		this.inflater = ((Activity)context).getLayoutInflater();
	}
	
	@Override
	public int getCount() {
		return courses.size();
	}

	@Override
	public Object getItem(int index) {
		return courses.get(index);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int index, View convertView, ViewGroup groupView) {
		Curso course = (Curso)getItem(index);
		
		convertView = inflater.inflate(R.layout.curso_row, null);
		TextView titulo = (TextView) convertView.findViewById(R.id.titulo_curso);
		TextView subtitulo = (TextView) convertView.findViewById(R.id.subtitulo_curso);
		
		titulo.setText(course.getNombre());
		subtitulo.setText(course.getModalidad() + " - " + course.getCarga_horaria() + " hs.");
		return convertView;
	}
	
	
	/**
	 * Used to refresh the list of courses in the adapter
	 */
	public void updateCursosList(List<Curso> courses){
		this.courses = courses;
		notifyDataSetChanged();
	}
}
