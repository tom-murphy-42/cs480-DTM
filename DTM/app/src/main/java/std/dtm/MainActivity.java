/*
Author: Sam Alston, Tom Murphy, Jack (Daniel) Kinne [STD]
Last Modified: 4/30/2018
Purpose: MainActivity is the main menu for the DTM application
    DTM is the Don't Tell Me game where you pose a question about a movie, the app finds it for you and presents clues
    Reclaim your outsourced memory, ask, don't tell me.

    'Movie' button launches AskQuestion
 */
package std.dtm;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //launch Ask question when you click a play button (movies)
    private ImageButton playButtonMovies;
    private ImageButton playButtonTV;
    private ImageButton playButtonVG;
    private ImageButton playButtonBooks;
    private ImageButton howToButton;
    private ImageButton leaderboardsButton;
    private ImageButton settingsButton;
    private TextView displayTextView;
    public static GameSettings settings;
    public static User user;

    //permissions
    private static final int PERMISSION_REQUEST_ALL = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings = new GameSettings();
        user = new User("L33T69GAMER69");

        displayTextView = (TextView) findViewById(R.id.displaytextview);
        displayTextView.setText(user.getDisplayString());

        playButtonMovies = (ImageButton) findViewById(R.id.playButtonMovies);
        playButtonTV = (ImageButton) findViewById(R.id.playButtonTV);
        playButtonVG = (ImageButton) findViewById(R.id.playButtonVG);
        playButtonBooks = (ImageButton) findViewById(R.id.playButtonBooks);
        howToButton = (ImageButton) findViewById(R.id.howtoButton);
        leaderboardsButton = (ImageButton) findViewById(R.id.leaderboardButton);
        settingsButton = (ImageButton) findViewById(R.id.settingsButton);

        playButtonMovies.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                checkPermission();
            }
        });

        //highlight effects
        playButtonMovies.setOnTouchListener(new HighlightOnTouchListener(playButtonMovies));
        playButtonTV.setOnTouchListener(new HighlightOnTouchListener(playButtonTV));
        playButtonVG.setOnTouchListener(new HighlightOnTouchListener(playButtonVG));
        playButtonBooks.setOnTouchListener(new HighlightOnTouchListener(playButtonBooks));
        howToButton.setOnTouchListener(new HighlightOnTouchListener(howToButton));
        leaderboardsButton.setOnTouchListener(new HighlightOnTouchListener(leaderboardsButton));
        settingsButton.setOnTouchListener(new HighlightOnTouchListener(settingsButton));

        howToButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToHowTo();
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSettings();
            }
        });
    }

    private void goToSettings(){
        Intent settingsIntent = new Intent(this, Settings.class);
        startActivity(settingsIntent);
    }

    private void goToAskQuestion(){
        Intent askQuestionIntent = new Intent(this, AskQuestion.class);
        startActivity(askQuestionIntent);

    }

    private void goToHowTo(){
        Intent settingsIntent = new Intent(this, HowToPlay.class);
        startActivity(settingsIntent);
    }

    //before we launch, check we have permissions.
    private void checkPermission() {
        // Check if the AUDIO and INTERNET permission has been granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission is already available, start DTM!
            goToAskQuestion();
        }
        else {
            // Permission is missing and must be requested.
            requestPermission();
        }
    }

    //Make new Permission request
    private void requestPermission() {
        // if permission not granted, show error.
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO) &&
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET) )
        {
            Toast.makeText(getApplicationContext(), "permission not granted!", Toast.LENGTH_LONG).show();

        }
        else {
            // Request the permission. Result received in onRequestPermissionsResult().
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.INTERNET}, PERMISSION_REQUEST_ALL);
        }
    }

    //handle the request for permission: start or deny.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_ALL) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start.
                goToAskQuestion();
            } else {
                // Permission request was denied.
                Toast.makeText(getApplicationContext(), "permission for camera was denied.", Toast.LENGTH_LONG).show();
            }
        }
    }
}//end mainactivity

//store settings information for the play session
class GameSettings {
    private boolean timer;
    private boolean freePlay;

    GameSettings() {
        timer = true;
        freePlay = false;
    }

    public boolean isTimerSet() {
        return timer;
    }

    public boolean isFreePlaySet() {
        return freePlay;
    }

    public void setTimer(boolean timer){
        this.timer = timer;
    }

    public void setFreePlay(boolean freePlay) {
        this.freePlay = freePlay;
    }
}

//store user information for the play session
class User{
    private int bankBalance;
    private String username;

    User(String username){
        this.bankBalance = 500;
        this.username = username;
    }

    public int getBankBalance() {
        return bankBalance;
    }

    public String getUsername() {
        return username;
    }

    public void setBankBalance(int newBalance) {
        this.bankBalance = newBalance;
    }

    public void setUsername(String newUsername) {
        this.username = newUsername;
    }

    public String getDisplayString() {
        return "Username: "+username+"   Bank Balance: $"+bankBalance;
    }

    public void subtractBalance(int spent){
        bankBalance-=spent;
    }

    public void addBalance(int earned) {
        bankBalance+= earned;
    }
}

//highlight imagebuttons on touch, as per https://stackoverflow.com/users/891479/l-g
//more @: https://stackoverflow.com/questions/4131656/how-to-make-button-highlight/14278790#14278790
class HighlightOnTouchListener implements View.OnTouchListener {

    private static final int TRANSPARENT_GREY = Color.argb(0, 111, 111, 111);
    private static final int FILTERED_GREY = Color.argb(155, 111, 111, 111);

    ImageView imageView;
    TextView textView;

    public HighlightOnTouchListener(final ImageView imageView) {
        super();
        this.imageView = imageView;
    }

    public HighlightOnTouchListener(final TextView textView) {
        super();
        this.textView = textView;
    }

    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        if (imageView != null) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                imageView.setColorFilter(FILTERED_GREY);
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                imageView.setColorFilter(TRANSPARENT_GREY); // or null
            }
        } else {
            for (final Drawable compoundDrawable : textView.getCompoundDrawables()) {
                if (compoundDrawable != null) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        // we use PorterDuff.Mode. SRC_ATOP as our filter color is already transparent
                        // we should have use PorterDuff.Mode.LIGHTEN with a non transparent color
                        compoundDrawable.setColorFilter(FILTERED_GREY, PorterDuff.Mode.SRC_ATOP);
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        compoundDrawable.setColorFilter(TRANSPARENT_GREY, PorterDuff.Mode.SRC_ATOP); // or null
                    }
                }
            }
        }
        return false;
    }

}