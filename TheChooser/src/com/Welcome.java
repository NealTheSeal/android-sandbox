package com;

import com.code.chooser.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Welcome extends Activity {
	
	/**
	 * The value given to results_activity for a food choice
	 */
	private final int FOOD_CHOICE_VALUE = 1;
	
	/**
	 * The value given to results_activity for a activity choice
	 */
	private final int ACTIVITY_CHOICE_VALUE = 2;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
	}
	
	/**
	 * food button functionality
	 * @param v
	 */
	public void food(View v) {
		Intent intent = new Intent(this, ResultActivity.class);
		intent.putExtra("choice", FOOD_CHOICE_VALUE);
		startActivity(intent);
	}
	
	/**
	 * activity button functionality
	 * @param v
	 */
	public void activity(View v) {
		Intent intent = new Intent(this, ResultActivity.class);
		intent.putExtra("choice", ACTIVITY_CHOICE_VALUE);
		startActivity(intent);
	}
}