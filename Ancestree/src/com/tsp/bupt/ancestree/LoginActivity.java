package com.tsp.bupt.ancestree;

import httputil.HttpConnection;
import httputil.MD5;
import httputil.HttpConnection.CallbackListener;

import java.security.NoSuchAlgorithmException;
import java.util.Hashtable;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private ProgressDialog progressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void onClickLoginNewUser(View view){
		Intent it=new Intent(LoginActivity.this,RegisterActivity.class);
		startActivity(it);
	}
	
	public void onClickLoginResetPSW(View view){
		Intent it=new Intent(LoginActivity.this,ResetPswActivity.class);
		startActivity(it);
	}
	
	public void onClickLogin(View view){
		progressDialog = new ProgressDialog(LoginActivity.this);
		progressDialog.setTitle(getString(R.string.logining));
		progressDialog.setMessage(getString(R.string.pleasewait));
		progressDialog.setIndeterminate(false); 
		progressDialog.setCancelable(false);
		progressDialog.show();
		String email = ((EditText) this.findViewById(R.id.loginEMail))
				.getText().toString().trim();
		String psw = ((EditText) this.findViewById(R.id.loginPSW)).getText()
				.toString().trim();
		Hashtable<String,String> data = new Hashtable<String,String>();
		data.put("email", email);
		try {
			String pswhash=MD5.getMD5(psw);
			data.put("password", MD5.getMD5(email+pswhash));
		} catch (NoSuchAlgorithmException e) {
			Toast.makeText(getApplicationContext(),
					R.string.registererrormd5error, Toast.LENGTH_SHORT).show();
			return;
		}
		
		new HttpConnection().post("login", data, loginCallbackListener);
	}
	
	private CallbackListener loginCallbackListener = new CallbackListener() {
		@Override
		public void callBack(String v) {
			progressDialog.dismiss();
			if (v.equals("fail")) {
				Toast.makeText(getApplicationContext(),
						R.string.networktransfererror, Toast.LENGTH_SHORT).show();
			} else {
				JSONObject jsonObj = new JSONObject(v);
				int retcode = Integer.parseInt(jsonObj.getString("retcode"));
				switch (retcode) {
				case 200:
					Toast.makeText(getApplicationContext(),
							R.string.loginsucceed, Toast.LENGTH_SHORT).show();
					Intent it=new Intent(LoginActivity.this,MainActivity.class);
					startActivity(it);
					finish();
					break;
				case 603:
					Toast.makeText(getApplicationContext(),
							R.string.loginwrongpsw, Toast.LENGTH_SHORT).show();
					break;
				case 604:
					Toast.makeText(getApplicationContext(),
							R.string.nouser, Toast.LENGTH_SHORT).show();
					break;
				default:
					Toast.makeText(getApplicationContext(),
							R.string.dberror, Toast.LENGTH_SHORT).show();
					break;
				}
			}
		}
	};
	
}
