package classes;

import com.example.farkle_scoreboard.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	/**
	 * winning score
	 */
	protected int winningScore;
	
	/**
	 * turn counter
	 */
	private int turnCounter = 0;
	
	/**
	 * team one's score
	 */
	private int teamOneScore = 0;
	
	/**
	 * team two's score
	 */
	private int teamTwoScore = 0;
	
	/**
	 * team one's name
	 */
	private String teamOne = "Team 1";
	
	/**
	 * team two's name
	 */
	private String teamTwo = "Team 2";
	
	/**
	 * team one's farkles
	 */
	private int teamOneFarkles = 0;
	
	/**
	 * team two's farkles
	 */
	private int teamTwoFarkles = 0;
	
	/**
	 * Input view
	 */
	private EditText input;
	
	/**
	 * View for displaying the current turn
	 */
	private TextView turnView;
	
	/**
	 * View for diplaying team one's score
	 */
	private TextView teamOneScoreView;
	
	/**
	 * View for displaying team two's score
	 */
	private TextView teamTwoScoreView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

    	alert.setTitle(R.string.winning_score);
    	alert.setMessage(R.string.win_description);
    	final EditText winInput = new EditText(this);
    	winInput.setInputType(InputType.TYPE_CLASS_NUMBER);
    	alert.setView(winInput);
    
    	(new Handler()).postDelayed(new Runnable() {

			@Override
			public void run() {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.showSoftInput(winInput, 0);
			}
			
		}, 250);
    	
    	alert.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialog, int whichButton) {
        		  winningScore = Integer.parseInt(winInput.getText().toString());
    		  }
    		});
    	
    	alert.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
    		  public void onClick(DialogInterface dialog, int whichButton) {
    		    // Canceled.
    		  }
    		});

    	alert.show();
    	
    	turnView = (TextView) findViewById(R.id.turn);
    	turnView.setText(teamOne + getString(R.string.turn));
    	
    	teamOneScoreView = (TextView) findViewById(R.id.team_one);
    	teamTwoScoreView = (TextView) findViewById(R.id.team_two);
    	
		input = (EditText) findViewById(R.id.text);
	}
	
	/**
	 * functionality for the add button
	 * 
	 * @param v
	 */
	public void add(View v) {
		if(!input.getText().toString().equals("")) {
			if(turnCounter % 2 == 0) {
				teamOneScore += Integer.parseInt(input.getText().toString());
				teamOneScoreView.setText("" + teamOneScore);
	 		} else {
				teamTwoScore += Integer.parseInt(input.getText().toString());
				teamTwoScoreView.setText("" + teamTwoScore);
	 		}
			turnCounter++;
			setTurnView();
			checkWin();
		}
	}
	
	/**
	 * functionality for the subtract button
	 * 
	 * @param v
	 */
	public void subtract(View v) {
		if(!input.getText().toString().equals("")) {
			if(turnCounter % 2 == 0) {
				teamOneScore -= Integer.parseInt(input.getText().toString());
				teamOneScoreView.setText("" + teamOneScore);
	 		} else {
				teamTwoScore -= Integer.parseInt(input.getText().toString());
				teamTwoScoreView.setText("" + teamTwoScore);
	 		}
		}
	}
	
	/**
	 * functionality for the farkle button
	 * 
	 * @param v
	 */
	public void farkle(View v) {
		if(turnCounter % 2 == 0) {
			teamOneScore -= 1000;
			teamOneFarkles++;
			TextView t1Farkles = (TextView) findViewById(R.id.one_farkles);
			t1Farkles.setText("" + teamOneFarkles); 
			teamOneScoreView.setText("" + teamOneScore);
		} else { 
			teamTwoScore -= 1000;
			teamTwoFarkles++;
			TextView t2Farkles = (TextView) findViewById(R.id.two_farkles);
			t2Farkles.setText("" + teamTwoFarkles);
			teamTwoScoreView.setText("" + teamTwoScore);
		}
		turnCounter++;
		setTurnView();
	}
	
	/**
	 * functionality for the left arrow
	 * 
	 * @param v
	 */
	public void decrementTurn(View v) {
		turnCounter--;
		setTurnView();
	}
	
	/**
	 * functionality for the right arrow
	 * 
	 * @param v
	 */
	public void incrementTurn(View v) {
		turnCounter++;
		setTurnView();
	}
	
	/**
	 * controls the view for the current turn
	 */
	public void setTurnView() {
		turnView = (TextView) findViewById(R.id.turn);
		if(turnCounter%2==0) {
			turnView.setText(teamOne + "'s turn");
		} else {
			turnView.setText(teamTwo + "'s turn");
		}
 	}
	
	/**
	 * checks to see if someone won
	 */
	public void checkWin() {
		if(teamOneScore >= winningScore) {
			Toast.makeText(this, teamOne + " wins!", Toast.LENGTH_LONG).show();
			//TODO add winning activity
		}
		
		if(teamTwoScore>=winningScore) {
			Toast.makeText(this, teamTwo + " wins!", Toast.LENGTH_LONG).show();
			//TODO add wining activity
		}
	}
}
