package com.tsp.bupt.ancestree;

import java.security.NoSuchAlgorithmException;
import java.util.Hashtable;

import org.json.JSONObject;

import httputil.HttpConnection;
import httputil.HttpConnection.CallbackListener;
import httputil.MD5;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity {

	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
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

	public void onClickRegister(View view) {
		progressDialog = new ProgressDialog(RegisterActivity.this);
		progressDialog.setTitle(getString(R.string.registering));
		progressDialog.setMessage(getString(R.string.pleasewait));
		progressDialog.setIndeterminate(false); 
		progressDialog.setCancelable(false);
		progressDialog.show();
		/*progressDialog = ProgressDialog.show(getApplicationContext(), getString(R.string.registering),
				getString(R.string.pleasewait));*/
		String email = ((EditText) this.findViewById(R.id.registerEMail))
				.getText().toString().trim();
		String psw = ((EditText) this.findViewById(R.id.registerPSW)).getText()
				.toString().trim();
		String confirmPsw = ((EditText) this
				.findViewById(R.id.registerConfirmPSW)).getText().toString()
				.trim();

		if (psw == null || psw.length() < 4) {
			Toast.makeText(getApplicationContext(),
					R.string.registererrorpswlength, Toast.LENGTH_SHORT);
			((EditText) this.findViewById(R.id.registerPSW)).requestFocus();
			return;
		}
		if (!psw.equals(confirmPsw)) {
			Toast.makeText(getApplicationContext(),
					R.string.registererrorpswconfirm, Toast.LENGTH_SHORT);
			((EditText) this.findViewById(R.id.registerConfirmPSW))
					.requestFocus();
			return;
		}
 
		Hashtable<String,String> data = new Hashtable<String,String>();
		data.put("email", email);
		try {
			String pswhash=MD5.getMD5(psw);
			data.put("password", MD5.getMD5(email+pswhash));
		} catch (NoSuchAlgorithmException e) {
			Toast.makeText(getApplicationContext(),
					R.string.registererrormd5error, Toast.LENGTH_SHORT);
			return;
		}
		new HttpConnection().post("register", data, registerCallbackListener);

	}

	private CallbackListener registerCallbackListener = new CallbackListener() {
		@Override
		public void callBack(String v) {
			progressDialog.dismiss();
			if (v.equals("fail")||v.equals("error")) {
				Toast.makeText(getApplicationContext(),
						R.string.networktransfererror, Toast.LENGTH_SHORT);
			} else {
				JSONObject jsonObj = new JSONObject(v);
				int retcode = Integer.parseInt(jsonObj.getString("retcode"));
				switch (retcode) {
				case 200:
					Toast.makeText(getApplicationContext(),
							R.string.registersucceed, Toast.LENGTH_SHORT).show();
					Intent it=new Intent(RegisterActivity.this,LoginActivity.class);
					startActivity(it);
					finish();
					break;
				case 601:
					Toast.makeText(getApplicationContext(),
							R.string.registererrordup, Toast.LENGTH_SHORT).show();
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
