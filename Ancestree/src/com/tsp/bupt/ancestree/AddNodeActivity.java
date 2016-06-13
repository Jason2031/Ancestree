package com.tsp.bupt.ancestree;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import classes.Person;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

public class AddNodeActivity extends Activity {

	private EditText etDeathday = null;
	private EditText etBirthday = null;
	private EditText etLastname = null;
	private EditText etFirstname = null;
	private Button btConfirm = null;
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_node);
		etDeathday = (EditText) this.findViewById(R.id.addNodeDeathday);
		etBirthday = (EditText) this.findViewById(R.id.addNodeBirthday);
		etLastname = (EditText) this.findViewById(R.id.addNodeLastname);
		etFirstname = (EditText) this.findViewById(R.id.addNodeFirstname);
		btConfirm = (Button) this.findViewById(R.id.addNodeConfirm);
		etLastname.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence text, int start, int before,
					int count) {
				// text 输入框中改变后的字符串信息
				// start 输入框中改变后的字符串的起始位置
				// before 输入框中改变前的字符串的位置 默认为0
				// count 输入框中改变后的一共输入字符串的数量
				if (etLastname.getText().length() >= 1
						&& etFirstname.getText().length() >= 1) {
					btConfirm.setEnabled(true);
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}
		});
		etFirstname.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence text, int start, int before,
					int count) {
				// text 输入框中改变后的字符串信息
				// start 输入框中改变后的字符串的起始位置
				// before 输入框中改变前的字符串的位置 默认为0
				// count 输入框中改变后的一共输入字符串的数量
				if (etLastname.getText().length() >= 1
						&& etFirstname.getText().length() >= 1) {
					btConfirm.setEnabled(true);
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}
		});
		Switch s = (Switch) this.findViewById(R.id.addNodeIsAliveSwitch);
		s.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					etDeathday.setVisibility(View.GONE);
				} else {
					etDeathday.setVisibility(View.VISIBLE);
				}
			}
		});
		etDeathday.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View arg0, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					Calendar calendar = Calendar.getInstance();
					DatePickerDialog dpd = new DatePickerDialog(
							AddNodeActivity.this, deathdayListener, calendar
									.get(Calendar.YEAR), calendar
									.get(Calendar.MONTH), calendar
									.get(Calendar.DAY_OF_MONTH));
					dpd.show();
				}
			}
		});
		etDeathday.setInputType(InputType.TYPE_NULL);
		etBirthday = (EditText) this.findViewById(R.id.addNodeBirthday);
		etBirthday.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View arg0, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					Calendar calendar = Calendar.getInstance();
					DatePickerDialog dpd = new DatePickerDialog(
							AddNodeActivity.this, birthdayListener, calendar
									.get(Calendar.YEAR), calendar
									.get(Calendar.MONTH), calendar
									.get(Calendar.DAY_OF_MONTH));
					dpd.show();
				}
			}
		});
		etBirthday.setInputType(InputType.TYPE_NULL);
		if (StaticInfos.nodeCount == 0) {
			LinearLayout l = (LinearLayout) this
					.findViewById(R.id.addNodeRelationshipLayout);
			l.setVisibility(View.GONE);
		}
		if (StaticInfos.personList.size() > 0) {
			Spinner spinner = (Spinner) findViewById(R.id.addNodePersonsSpinner);
			
			List<String> persons = new ArrayList<String>();
			for(Entry<String, Person> entry:StaticInfos.personList.entrySet()){
				if(entry.getValue().isMale()){
					persons.add(entry.getKey());
				}
			}
			// 适配器
			//Set<String> keys = StaticInfos.personList.keySet();
			//for (String k : keys) {
				//persons.add(k);
			// }
			ArrayAdapter<String> arr_adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, persons);
			// 设置样式
			arr_adapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			// 加载适配器
			spinner.setAdapter(arr_adapter);
		}
		Spinner spinner = (Spinner) findViewById(R.id.addNodeRelationshipSpinner);
		List<String> data = new ArrayList<String>();
		data.add("儿子");
		data.add("父亲");
		// 适配器
		ArrayAdapter<String> arr_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, data);
		// 设置样式
		arr_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 加载适配器
		spinner.setAdapter(arr_adapter);
	}

	@Override
	public void onBackPressed() {
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_node, menu);
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

	public void openBirthdayPicker(View view) {
		Calendar calendar = Calendar.getInstance();
		DatePickerDialog dpd = new DatePickerDialog(this, birthdayListener,
				calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH));
		dpd.show();
	}

	public void openDeathdayPicker(View view) {
		Calendar calendar = Calendar.getInstance();
		DatePickerDialog dpd = new DatePickerDialog(this, deathdayListener,
				calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH));
		dpd.show();
	}

	private DatePickerDialog.OnDateSetListener birthdayListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			etBirthday.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
		}
	};

	private DatePickerDialog.OnDateSetListener deathdayListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			etDeathday.setText(year + "/" + (monthOfYear + 1) + "/"
					+ dayOfMonth);
		}
	};

	public void onClickAddNodeMaleRadio(View view) {
		Spinner spinner = (Spinner) findViewById(R.id.addNodeRelationshipSpinner);
		List<String> data = new ArrayList<String>();
		data.add("儿子");
		data.add("父亲");
		// 适配器
		ArrayAdapter<String> arr_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, data);
		// 设置样式
		arr_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 加载适配器
		spinner.setAdapter(arr_adapter);
	}

	public void onClickAddNodeFemaleRadio(View view) {
		Spinner spinner = (Spinner) findViewById(R.id.addNodeRelationshipSpinner);
		List<String> data = new ArrayList<String>();
		data.add("女儿");
		data.add("妻子");
		// 适配器
		ArrayAdapter<String> arr_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, data);
		// 设置样式
		arr_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 加载适配器
		spinner.setAdapter(arr_adapter);
	}

	public void onClickAddNodeConfirm(View view) {
		progressDialog = new ProgressDialog(AddNodeActivity.this);
		progressDialog.setTitle(getString(R.string.logining));
		progressDialog.setMessage(getString(R.string.pleasewait));
		progressDialog.setIndeterminate(false);
		progressDialog.setCancelable(false);
		progressDialog.show();
		Person p = new Person(etFirstname.getText().toString().trim(),
				etLastname.getText().toString().trim());
		if (((RadioButton) this.findViewById(R.id.addNodeMaleRadio))
				.isChecked()) {
			p.setMale(true);
		} else {
			p.setMale(false);
		}
		p.setIndex(StaticInfos.nodeCount++);
		if (StaticInfos.personList.size() == 0) {
			StaticInfos.Ancestor = p;
		} else {
			Spinner spinnerPerson = (Spinner) this
					.findViewById(R.id.addNodePersonsSpinner);
			Person relatedPerson = StaticInfos.personList.get(String
					.valueOf(spinnerPerson.getSelectedItem()));
			Spinner spinnerRelationship = (Spinner) this
					.findViewById(R.id.addNodeRelationshipSpinner);
			int index = spinnerRelationship.getSelectedItemPosition();
			if (index == 0) {
				p.setFatherPerson(relatedPerson);
				relatedPerson.setChildPerson(p);
			} else {
				if (p.isMale()) {
					if (StaticInfos.Ancestor.equals(relatedPerson)) {
						StaticInfos.Ancestor = relatedPerson;
					}
					relatedPerson.setFatherPerson(p);
					p.setChildPerson(relatedPerson);
				} else {
					relatedPerson.setSpousePerson(p);
					p.setSpousePerson(relatedPerson);
				}
			}
		}
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");
		if(etBirthday.getText().length()>0){
			try {
				p.setBirthDate(sdf.parse(etBirthday.getText().toString()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(etDeathday.getText().length()>0){
			try {
				p.setDeathDate(sdf.parse(etDeathday.getText().toString()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		StaticInfos.personList.put(p.getFullName(), p);
		Toast.makeText(getApplicationContext(), R.string.addsuccessful,
				Toast.LENGTH_SHORT).show();
		progressDialog.dismiss();
		this.finish();
	}

}
