package com.javier.lagord.trabajofinalcapit.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.javier.lagord.trabajofinalcapit.R;
import com.javier.lagord.trabajofinalcapit.backend.Backend;
import com.javier.lagord.trabajofinalcapit.database.DatabaseHelper;

public class LoginActivity extends Activity {
	private static final int CREAR_CUENTA_DIALOG = 1;
	EditText userNameEditText = null;
	EditText passwordEditText = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		userNameEditText = (EditText)findViewById(R.id.editUserName);
		passwordEditText = (EditText)findViewById(R.id.editPassword);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		userNameEditText.setText("");
		passwordEditText.setText("");
	}
	
	public void onLoginButtonPressed(View v){
		String username = userNameEditText.getText().toString();
		String password = passwordEditText.getText().toString();
		
		try{
			String name = DatabaseHelper.getDatabaseInstance(this).login(username, password);
			Toast.makeText(LoginActivity.this, "Bienvenido " + name, Toast.LENGTH_SHORT).show();
			Backend.getInstance().setCurrentUsername(this, username);

			Intent intent = new Intent(this, CursosActivity.class);
			startActivity(intent);
		} catch(IllegalArgumentException e){
			showMessage(e.getMessage());
		}
	}

	private void showMessage(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
		Log.i("Main Activity", msg);
	}

	public void onCreateAccountButtonPressed(View v){
		showDialog(CREAR_CUENTA_DIALOG);
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		switch (id) {
		case CREAR_CUENTA_DIALOG:
			dialog = new Dialog(LoginActivity.this);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.crear_cuenta_dialog);
			final TextView nameView = (TextView) dialog.findViewById(R.id.editTextName);
			final TextView usernameView = (TextView) dialog.findViewById(R.id.editTextUsername);
			final TextView passwordView = (TextView) dialog.findViewById(R.id.editTextPassword);
			final TextView password2View = (TextView) dialog.findViewById(R.id.editTextPassword2);
			
			Button btnCancel = (Button)dialog.findViewById(R.id.buttonCancelCreateAccount);
			btnCancel.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dismissDialog(CREAR_CUENTA_DIALOG);
				}
			});
			
			Button btnCreate = (Button)dialog.findViewById(R.id.buttonCreateAccount);
			btnCreate.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String name = nameView.getText().toString();
					String username = usernameView.getText().toString();
					String password = passwordView.getText().toString();
					String password2 = password2View.getText().toString();
					
					if (!password.equals(password2)){
						showMessage("Las contraseñas no son iguales.\nTrata de nuevo");
					} else {
						try{
							DatabaseHelper.getDatabaseInstance(LoginActivity.this).createNewAccount(name, username, password);
							Toast.makeText(LoginActivity.this, "La cuenta fue creada con éxito", Toast.LENGTH_SHORT).show();
							userNameEditText.setText(username);
							passwordEditText.setText(password);
							dismissDialog(CREAR_CUENTA_DIALOG);
						} catch(RuntimeException e){
							showMessage(e.getMessage());
						}
					}
				}
			});
			
			dialog.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					nameView.setText("");
					usernameView.setText("");
					passwordView.setText("");
					password2View.setText("");
				}
			});
			
			// adjust dialog width
			LayoutParams params = dialog.getWindow().getAttributes();
			params.width = LayoutParams.MATCH_PARENT;
			dialog.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
			break;

		default:
			break;
		}
		
		return dialog;
	}
}
