package com.tsp.bupt.ancestree;

import httputil.HttpConnection;
import httputil.HttpConnection.CallbackListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import classes.Person;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class DownloadActivity extends Activity {

	private ProgressDialog progressDialog;
	private ListView lv;
	private ArrayList<HashMap<String, Object>> fileList;
	private String selectedID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_download);
		lv = (ListView) this.findViewById(R.id.downloadListView);
		fileList=new ArrayList<HashMap<String, Object>>();
		getDownloadList();
	}

	private void getDownloadList() {
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage(getString(R.string.pleasewait));
		progressDialog.setIndeterminate(false);
		progressDialog.setCancelable(false);
		progressDialog.show();

		new HttpConnection().post("getancestreefiles", null, downloadfilesbackListener);
	}

	private CallbackListener downloadfilesbackListener = new CallbackListener() {

		@Override
		public void callBack(String v) {
			// TODO Auto-generated method stub
			progressDialog.dismiss();
			if (v.equals("fail")) {
				Toast.makeText(getApplicationContext(),
						R.string.networktransfererror, Toast.LENGTH_SHORT)
						.show();
			} else {
				JSONObject jsonObj = new JSONObject(v);
				int retcode = Integer.parseInt(jsonObj.getString("retcode"));
				if (retcode == 200) {
					JSONArray graphFiles = new JSONArray(
							jsonObj.getString("retdata"));
					for (int i = 0; i < graphFiles.length(); ++i) {
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("Title", graphFiles.getJSONArray(i)
								.getString(1));
						map.put("FileID",
								graphFiles.getJSONArray(i).getString(0));
						fileList.add(map);
					}
					SimpleAdapter sa = new SimpleAdapter(DownloadActivity.this,
							fileList, R.layout.graphline, new String[] {
									"Title", "ID" }, new int[] { R.id.Title,
									R.id.FileID });
					lv.setAdapter(sa);
					lv.setOnItemClickListener(new OnItemClickListener(){
						
						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							// TODO Auto-generated method stub
							selectedID=(String) fileList.get(arg2).get("FileID");
							new AlertDialog.Builder(DownloadActivity.this)
							.setTitle(getResources().getString(R.string.pleaseconfirm))
							.setMessage(getResources().getString(R.string.downloadornot)+"\n"+getResources().getString(R.string.unsavedwillbelost))
							.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									progressDialog = new ProgressDialog(DownloadActivity.this);
									progressDialog.setMessage(getString(R.string.pleasewait));
									progressDialog.setIndeterminate(false);
									progressDialog.setCancelable(false);
									progressDialog.show();
									Hashtable<String,String> data = new Hashtable<String,String>();
									data.put("graphid", selectedID);
									new HttpConnection().post("downloadancestree", null, downloadgrapgbackListener);
								}
							})
							.setNegativeButton(getResources().getString(R.string.no), null)
							.show();
						}
						
					});
				}
			}
		}

		private CallbackListener downloadgrapgbackListener = new CallbackListener(){

			@Override
			public void callBack(String v) {
				// TODO Auto-generated method stub
				progressDialog.dismiss();
				if (v.equals("fail")) {
					Toast.makeText(getApplicationContext(),
							R.string.networktransfererror, Toast.LENGTH_SHORT)
							.show();
				}else{
					JSONObject jsonObj = new JSONObject(v);
					int retcode = Integer.parseInt(jsonObj.getString("retcode"));
					switch(retcode){
					case 200:
						StaticInfos.Ancestor=null;
						StaticInfos.personList.clear();
						SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");
						JSONArray data=new JSONArray(jsonObj.getString("retdata"));
						JSONArray nodes=data.getJSONArray(0);
						for(int i=0;i<nodes.length();++i){
							JSONObject n=nodes.getJSONObject(i);
							Person p=new Person(n.getString("Firstname"),n.getString("Lastname"));
							p.setIndex(n.getInt("PersonNodeID"));
							StaticInfos.personList.put(p.getFullName(), p);
							p.setMale(n.getBoolean("IsMale"));
							try {
								p.setBirthDate(sdf.parse(n.getString("Birthday")));
								p.setDeathDate(sdf.parse(n.getString("Deathday")));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						StaticInfos.nodeCount=StaticInfos.personList.size();
						JSONArray relationships=data.getJSONArray(1);
						for(int i=0;i<relationships.length();++i){
							JSONObject r=relationships.getJSONObject(i);
							Person p1=StaticInfos.personList.get(r.get("ID1"));
							Person p2=StaticInfos.personList.get(r.get("ID2"));
							int relationshipType=r.getInt("RelationshipType");
							switch(relationshipType){
							case 1:
								p1.setChildPerson(p2);
								p2.setFatherPerson(p1);
								break;
							case 2:
								p1.setSpousePerson(p2);
								p2.setSpousePerson(p1);
								break;
							}
						}
						break;
					}
				}
				Toast.makeText(getApplicationContext(),
						R.string.downloadcomplete, Toast.LENGTH_SHORT).show();
				finish();
			}
			
		};
		
	};

}
