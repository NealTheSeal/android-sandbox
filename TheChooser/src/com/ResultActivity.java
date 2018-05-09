package com;

import com.code.chooser.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultActivity extends Activity {
	
	/**
	 * used to hold the choice type
	 * 
	 * 1 -> food
	 * 2 -> activity
	 */
	private int choiceType;
	
	/**
	 * view for the title
	 */
	private TextView title;
	/**
	 * view for image
	 */
	private ImageView image;
	/**
	 * view for the description
	 */
	private TextView description;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		description = (TextView) findViewById(R.id.description);
		
		Bundle b = getIntent().getExtras();
		if(b != null) {
			choiceType = b.getInt("choice");
		}
		
		setDisplay(choiceType);
	}
	
	/**
	 * sets the display based on the type of choice
	 * @param choice_type
	 */
	public void setDisplay(int choice_type) {
		int choice;
		if(choice_type == 1) {
			title.setText("You will be eating at...");
			choice = choose(choice_type);
		}
		else if(choice_type == 2) {
			title.setText("You are going...");
		}
		else {
			//TODO add defaul functionality
		}
	}
	
	public void 
}
