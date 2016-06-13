package com.tsp.bupt.ancestree;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import classes.ParentRelationship;
import classes.Person;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class NodeDetailActivity extends Activity {

	Person person = null;
	private EditText etLastname = null;
	private EditText etFirstname = null;
	private RadioButton rbMale = null;
	private RadioButton rbFemale = null;
	private EditText etDeathday = null;
	private EditText etBirthday = null;
	private Switch s = null;
	private Button btConfirm = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_node_detail);

		Intent it = getIntent();
		String personName = it.getStringExtra("personFullName");
		person = StaticInfos.personList.get(personName);

		btConfirm = (Button) this.findViewById(R.id.nodeDetailConfirm);
		etLastname = (EditText) this.findViewById(R.id.nodeDetailLastname);
		etLastname.setText(person.getLastName());
		etLastname.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				btConfirm.setEnabled(true);
			}

		});

		etFirstname = (EditText) this.findViewById(R.id.nodeDetailFirstname);
		etFirstname.setText(person.getFirstName());
		etFirstname.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				btConfirm.setEnabled(true);
			}

		});

		rbMale = (RadioButton) this.findViewById(R.id.nodeDetailMaleRadio);
		rbFemale = (RadioButton) this.findViewById(R.id.nodeDetailFemaleRadio);
		if (person.isMale()) {
			rbMale.setChecked(true);
			rbFemale.setChecked(false);
		} else {
			rbFemale.setChecked(true);
			rbMale.setChecked(false);
		}
		etBirthday = (EditText) this.findViewById(R.id.nodeDetailBirthday);
		etBirthday.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				btConfirm.setEnabled(true);
			}

		});
		etDeathday = (EditText) this.findViewById(R.id.nodeDetailDeathday);
		etDeathday.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				btConfirm.setEnabled(true);
			}

		});
		s = (Switch) this.findViewById(R.id.nodeDetailIsAliveSwitch);
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
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		if (person.getBirthDate() != null) {
			etBirthday.setText(sdf.format(person.getBirthDate()));
		}
		if (person.getDeathDate() != null) {
			etDeathday.setText(sdf.format(person.getDeathDate()));
			s.setChecked(false);
		}

		LinearLayout relationshipDisplay = (LinearLayout) this
				.findViewById(R.id.nodeDetailHiddenRelationshipLayout);
		if (person.isMale()) {
			List<Person> children = person.getChildPersons();
			for (int i = 0; i < children.size(); ++i) {
				LinearLayout newLayout = generateRelationshipDisplayLayout(
						children.get(i), "¸¸Ç×");
				relationshipDisplay.addView(newLayout);
			}
			if (person.getFather() != null) {
				LinearLayout newLayout = generateRelationshipDisplayLayout(
						person.getFatherPerson(), "¶ù×Ó");
				relationshipDisplay.addView(newLayout);
			}
			if (person.getSpouseof() != null) {
				LinearLayout newLayout = generateRelationshipDisplayLayout(
						person.getSpousePerseon(), "ÕÉ·ò");
				relationshipDisplay.addView(newLayout);
			}
		} else {
			if (person.getFather() != null) {
				LinearLayout newLayout = generateRelationshipDisplayLayout(
						person.getFatherPerson(), "Å®¶ù");
				relationshipDisplay.addView(newLayout);
			}
			if (person.getSpouseof() != null) {
				LinearLayout newLayout = generateRelationshipDisplayLayout(
						person.getSpousePerseon(), "ÆÞ×Ó");
				relationshipDisplay.addView(newLayout);
				List<Person> children = person.getSpousePerseon().getChildPersons();
				for (int i = 0; i < children.size(); ++i) {
					LinearLayout newLayout1 = generateRelationshipDisplayLayout(
							children.get(i), "Ä¸Ç×");
					relationshipDisplay.addView(newLayout1);
				}
			}
		}
	}

	public void onClickDeleteNode(View view) {
		new AlertDialog.Builder(this)
		.setTitle(getResources().getString(R.string.areyousuretodeletenode))
		.setMessage(getResources().getString(R.string.deletewarinng))
		.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if(person.isMale()){
					if(StaticInfos.Ancestor.equals(person)){
						StaticInfos.Ancestor=null;
						StaticInfos.personList.clear();
						StaticInfos.nodeCount=0;
					}else if(person.getFatherPerson()!=null){
						person.getFatherPerson().deleteChild(person);
						deleteNodeRecursivly(person);
					}else{
						StaticInfos.personList.remove(person.getFullName());
						--StaticInfos.nodeCount;
					}
				}else{
					if(person.getFatherPerson()!=null){
						person.getFatherPerson().getChildPersons().remove(person);
						StaticInfos.personList.remove(person.getSpousePerseon().getFullName());
						StaticInfos.personList.remove(person.getFullName());
						StaticInfos.nodeCount-=2;
					}else{
						person.getSpousePerseon().setSpousePerson(null);
						StaticInfos.personList.remove(person.getFullName());
						--StaticInfos.nodeCount;
					}
				}
				finish();
				
			}
		})
		.setNegativeButton(getResources().getString(R.string.no), null)
		.show();
	}
	
	private void deleteNodeRecursivly(Person person){
		for(Person child:person.getChildPersons()){
			deleteNodeRecursivly(child);
		}
		if(person.isMale()&&person.getSpousePerseon()!=null){
			StaticInfos.personList.remove(person.getSpousePerseon().getFullName());
		}
		--StaticInfos.nodeCount;
		StaticInfos.personList.remove(person.getFullName());
	}

	public void onClickConfirmChange(View view) {

	}

	private LinearLayout generateRelationshipDisplayLayout(Person person,
			String relationshipString) {
		LinearLayout newLayout = new LinearLayout(this);
		newLayout.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		newLayout.setOrientation(LinearLayout.HORIZONTAL);

		TextView is = new TextView(this);
		is.setText(getResources().getString(R.string.is));
		is.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		newLayout.addView(is);

		TextView relatedPerson = new TextView(this);
		relatedPerson.setMinWidth(dpToPx(100));
		relatedPerson.setText(person.getFullName());
		relatedPerson.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		newLayout.addView(relatedPerson);

		TextView ones = new TextView(this);
		ones.setText(getResources().getString(R.string.ones));
		ones.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		newLayout.addView(ones);

		TextView relationship = new TextView(this);
		relationship.setMinWidth(dpToPx(100));
		relationship.setText(relationshipString);
		relationship.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		newLayout.addView(relationship);
		return newLayout;
	}

	private int dpToPx(int dp) {
		return (int) (dp * getResources().getDisplayMetrics().density + 0.5f);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.node_detail, menu);
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
			etBirthday.setText(year + "/" + (monthOfYear + 1) + "/"
					+ dayOfMonth);
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
}
