package com.tsp.bupt.ancestree;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.LinearLayout;

public class LevelLayout extends LinearLayout {
	private float[] points = null;

	public LevelLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint p = new Paint();
		p.setColor(Color.BLACK);
		p.setStyle(Paint.Style.STROKE);
		p.setStrokeWidth(8);
		if (points != null) {
			canvas.drawLines(points, p);
		}
	}

	public float[] getPoints() {
		return points;
	}

	public void setPoints(float[] points) {
		this.points = points;
	}

	public void resetPoints() {
		points = null;
	}
}
