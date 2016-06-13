package com.tsp.bupt.ancestree;

import httputil.HttpConnection;
import httputil.HttpConnection.CallbackListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;

import classes.ParentRelationship;
import classes.Person;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private final int NODEWIDTH = 70;
	private final int NODEHEIGHT = 70;
	private final int NODEMARGIN = 15;

	private ZoomView zoomView;
	private LinearLayout ancestree;
	private LevelLayout ll;
	private List<LinearLayout> generationLayout = new ArrayList<LinearLayout>();
	private List<Float> lineCoordinates;
	private ProgressDialog progressDialog;

	private class Coordinate {
		private int x;
		private int y;

		public Coordinate(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public void setX(int x) {
			this.x = x;
		}

		public void setY(int y) {
			this.y = y;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		lineCoordinates = new ArrayList<Float>(); // startx, starty, endx, endy

		ll = new LevelLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);

		ll.setBackgroundColor(Color.WHITE);
		ancestree = (LinearLayout) ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
				.inflate(R.layout.activity_main, null, false);
		ancestree.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		ancestree.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						// TODO Auto-generated method stub
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
							ancestree.getViewTreeObserver()
									.removeOnGlobalLayoutListener(this);
						} else {
							ancestree.getViewTreeObserver()
									.removeGlobalOnLayoutListener(this);
						}
						// drawAncestree();
					}
				});
		ll.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						// TODO Auto-generated method stub
						addLineCoordinatesRecursivly(StaticInfos.Ancestor,
								new Coordinate(0, 0));
						float[] points = new float[lineCoordinates.size()];
						for (int i = 0; i < lineCoordinates.size(); ++i) {
							points[i] = lineCoordinates.get(i);
						}
						ll.setPoints(points);
						ll.invalidate();
					}
				});

		zoomView = new ZoomView(this);
		zoomView.setBackgroundColor(Color.WHITE);
		zoomView.addView(ancestree);

		ancestree.setBackgroundColor(Color.BLACK);
		ancestree.addView(ll);

		LinearLayout rootLayout = (LinearLayout) this
				.findViewById(R.id.mainPageRootLayout);
		rootLayout.setBackgroundColor(Color.WHITE);
		rootLayout.addView(zoomView);

	}

	@Override
	protected void onStart() {
		super.onStart();

		drawAncestree();
	}

	private void addLineCoordinatesRecursivly(Person person, Coordinate c) {
		if (person == null)
			return;
		for (int i = 0; i < person.getChildPersons().size(); ++i) {
			Coordinate childCoordinate = new Coordinate(c.getX() + i,
					c.getY() + 1);
			addLine(c, childCoordinate);
			addLineCoordinatesRecursivly(person.getChildPersons().get(i),
					childCoordinate);
		}
		if (person.getSpousePerseon() != null) {
			addLine(c, new Coordinate(c.getX() + 1, c.getY()));
		}
	}

	private void addLine(Coordinate starting, Coordinate ending) {
		try {
			Button startPoint = (Button) generationLayout.get(starting.getY())
					.getChildAt(starting.getX());
			Button endPoint = (Button) generationLayout.get(ending.getY())
					.getChildAt(ending.getX());
			lineCoordinates.add(getButtonXPosition(startPoint));
			lineCoordinates.add(getButtonYPosition(startPoint));
			lineCoordinates.add(getButtonXPosition(endPoint));
			lineCoordinates.add(getButtonYPosition(endPoint));
		} catch (Exception e) {
		}
	}

	private void drawAncestree() {
		// TODO: draw the tree inside the view "ancestree"
		ll.removeAllViews();
		generationLayout = new ArrayList<LinearLayout>();
		lineCoordinates = new ArrayList<Float>();
		if (StaticInfos.Ancestor != null) {
			Button ancestor = createButtonNode(StaticInfos.Ancestor);
			LinearLayout firstLevel = new LinearLayout(this);
			firstLevel.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT));
			ll.addView(firstLevel);
			generationLayout.add(firstLevel);

			firstLevel.addView(ancestor);

			List<Person> children = StaticInfos.Ancestor.getChildPersons();
			for (int i = 0; i < children.size(); ++i) {
				drawNodesRecursivly(children.get(i), ancestor, new Coordinate(
						i, 1));
			}
			if (StaticInfos.Ancestor.getSpouseof() != null) {
				drawNodesRecursivly(StaticInfos.Ancestor.getSpouseof()
						.getRelatedTo(), ancestor, new Coordinate(1, 0));
			}
		}
	}

	private int getStatusBarHeight() {
		int result = 0;
		int resourceId = getResources().getIdentifier("status_bar_height",
				"dimen", "android");
		if (resourceId > 0) {
			result = getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	private Button createButtonNode(final Person person) {
		Button output = new Button(MainActivity.this);
		if (person.isMale()) {
			output.setBackground(getResources()
					.getDrawable(R.drawable.malenode));
		} else {
			output.setBackground(getResources().getDrawable(
					R.drawable.femalenode));
		}
		output.setTextColor(Color.WHITE);
		output.setText(person.getFullName());
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				dpToPx(NODEWIDTH), dpToPx(NODEHEIGHT));
		params.setMargins(dpToPx(NODEMARGIN), dpToPx(NODEMARGIN),
				dpToPx(NODEMARGIN), dpToPx(NODEMARGIN));
		output.setLayoutParams(params);

		output.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it = new Intent();
				it.putExtra("personFullName", person.getFullName());
				it.setClass(MainActivity.this, NodeDetailActivity.class);
				startActivity(it);
			}

		});

		return output;
	}

	private int dpToPx(int dp) {
		return (int) (dp * getResources().getDisplayMetrics().density + 0.5f);
	}

	private float getButtonXPosition(Button button) {
		int[] loc = new int[2];
		button.getLocationOnScreen(loc);
		return (float) (loc[0] + dpToPx(NODEWIDTH / 2));
	}

	private float getButtonYPosition(Button button) {
		int[] loc = new int[2];
		button.getLocationOnScreen(loc);
		return (float) (loc[1] - getStatusBarHeight());
	}

	private Coordinate drawNodesRecursivly(Person person, Button startingPoint,
			Coordinate coordinate) {
		LinearLayout thisLevel = null;
		if (generationLayout.size() <= coordinate.getY()) {
			thisLevel = new LinearLayout(this);
			thisLevel.setOrientation(LinearLayout.HORIZONTAL);
			thisLevel.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT));
			ll.addView(thisLevel);
			generationLayout.add(thisLevel);
		} else {
			thisLevel = generationLayout.get(coordinate.getY());
		}

		int levelNodeCount = thisLevel.getChildCount();
		for (int i = levelNodeCount; i < coordinate.getX(); ++i) { // padding
			Button bt = new Button(this);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					dpToPx(NODEWIDTH), dpToPx(NODEHEIGHT));
			params.setMargins(dpToPx(NODEMARGIN), dpToPx(NODEMARGIN),
					dpToPx(NODEMARGIN), dpToPx(NODEMARGIN));
			bt.setLayoutParams(params);
			bt.setEnabled(false);
			bt.setVisibility(View.INVISIBLE);
			thisLevel.addView(bt);
		}

		Button node = createButtonNode(person);

		thisLevel.addView(node);

		Coordinate output = new Coordinate(coordinate.getX(), coordinate.getY());

		if (person.getParentof() != null && person.getParentof().size() != 0) {
			output.setY((output.getY() + 1));
			for (int i = 0; i < person.getParentof().size(); ++i) {
				Coordinate thisCoordinate = drawNodesRecursivly(person
						.getParentof().get(i).getRelatedTo(), node, output);
				if (output.getX() < thisCoordinate.getX()) {
					output.setX(thisCoordinate.getX());
				}
			}
		}
		if (person.isMale() && person.getSpouseof() != null) {
			drawNodesRecursivly(person.getSpouseof().getRelatedTo(), node,
					new Coordinate(coordinate.getX() + 1, coordinate.getY()));
			if (output.getX() < coordinate.getX() + 1) {
				output.setX(coordinate.getX() + 1);
			}
		}
		return output;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
		case R.id.menuaddnode:
			Intent it1 = new Intent(MainActivity.this, AddNodeActivity.class);
			startActivity(it1);
			break;
		case R.id.menudownloadancestree:
			Intent it2 = new Intent(MainActivity.this, DownloadActivity.class);
			startActivity(it2);
			break;
		case R.id.menuuploadancestree:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(getResources().getString(
					R.string.inputancestreetitle));
			final EditText input = new EditText(this);
			// input.setInputType(InputType.TYPE_CLASS_TEXT);
			builder.setView(input);
			builder.setPositiveButton(getResources().getString(R.string.yes),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							uploadAncestree(input.getText().toString()
									.toString());
						}
					});
			builder.setNegativeButton(getResources().getString(R.string.no),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});

			builder.show();

			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void uploadAncestree(String title) {
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage(getString(R.string.pleasewait));
		progressDialog.setIndeterminate(false);
		progressDialog.setCancelable(false);
		progressDialog.show();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		JSONArray nodes = new JSONArray();
		JSONArray relationships = new JSONArray();
		for (Entry<String, Person> entry : StaticInfos.personList.entrySet()) {
			Person p = entry.getValue();
			JSONObject json = new JSONObject();
			json.put("PersonNodeID", p.getIndex());
			json.put("Firstname", p.getFirstName());
			json.put("Lastname", p.getLastName());
			if (p.isMale()) {
				json.put("IsMale", 1);
			} else {
				json.put("IsMale", 0);
			}
			if (p.getBirthDate() != null) {
				json.put("Birthday", sdf.format(p.getBirthDate()));
			} else {
				json.put("Birthday", "");
			}
			if (p.getDeathDate() != null) {
				json.put("Deathday", sdf.format(p.getDeathDate()));
			} else {
				json.put("Deathday", "");
			}
			nodes.put(json);
			for (int i = 0; i < p.getChildPersons().size(); ++i) {
				JSONObject r = new JSONObject();
				r.put("PersonNodeID1", p.getIndex());
				r.put("PersonNodeID2", p.getChildPersons().get(i).getIndex());
				r.put("RelationshipType", 1);
				relationships.put(r);
			}
			if (p.getSpousePerseon() != null) {
				JSONObject r = new JSONObject();
				r.put("PersonNodeID1", p.getIndex());
				r.put("PersonNodeID2", p.getSpousePerseon().getIndex());
				r.put("RelationshipType", 2);
				relationships.put(r);
			}
		}

		Hashtable<String, String> data = new Hashtable<String, String>();
		data.put("title", title);
		data.put("nodes", nodes.toString());
		data.put("relationships", relationships.toString());

		new HttpConnection().post("uploadancestree", data, uploadbackListener);
	}

	private CallbackListener uploadbackListener = new CallbackListener() {

		@Override
		public void callBack(String v) {
			// TODO Auto-generated method stub
			progressDialog.dismiss();
			if (v.equals("fail")) {
				Toast.makeText(getApplicationContext(),
						R.string.networktransfererror, Toast.LENGTH_SHORT)
						.show();
			} else {

			}
		}

	};

}
