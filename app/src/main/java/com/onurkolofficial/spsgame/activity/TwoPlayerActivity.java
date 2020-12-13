package com.onurkolofficial.spsgame.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.onurkolofficial.spsgame.R;

import static com.onurkolofficial.spsgame.activity.MainActivity.isSignedIn;
import static com.onurkolofficial.spsgame.activity.MainActivity.playerAccount;
import static com.onurkolofficial.spsgame.classes.GameSound.startButtonClickSound;
import static com.onurkolofficial.spsgame.classes.TwoPlayerGame.ITEM_PAPER;
import static com.onurkolofficial.spsgame.classes.TwoPlayerGame.ITEM_SCISSORS;
import static com.onurkolofficial.spsgame.classes.TwoPlayerGame.ITEM_STONE;
import static com.onurkolofficial.spsgame.classes.TwoPlayerGame.PLAYER_1;
import static com.onurkolofficial.spsgame.classes.TwoPlayerGame.PLAYER_2;
import static com.onurkolofficial.spsgame.classes.TwoPlayerGame.gameStart;

public class TwoPlayerActivity extends AppCompatActivity {

    TextView menuBtn,player1Name,player1Win,player1Draw,player1Lose,player2Win,player2Draw,player2Lose,
            player1StoneBtn,player1PaperBtn,player1ScissorsBtn,player2StoneBtn,player2PaperBtn,player2ScissorsBtn;

    // Only Start and Show Scores.
    String player1_win="0",
            player1_draw="0",
            player1_lose="0",
            player2_win="0",
            player2_draw="0",
            player2_lose="0";

    // Define Session Scores (Player 1 and Player 2)
    public static int session_player1_win=0,
            session_player1_draw=0,
            session_player1_lose=0,
            session_player2_win=0,
            session_player2_draw=0,
            session_player2_lose=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_player);

        // Reset All Data
        session_player1_win=0;
        session_player1_draw=0;
        session_player1_lose=0;
        session_player2_win=0;
        session_player2_draw=0;
        session_player2_lose=0;

        // This Activity
        Activity context=this;

        // Get View Elements
        menuBtn=findViewById(R.id.menuButton);
        player1Name=findViewById(R.id.player1NameText);
        player1Win=findViewById(R.id.player1WinScoreText);
        player1Draw=findViewById(R.id.player1DrawScoreText);
        player1Lose=findViewById(R.id.player1LoseScoreText);
        player2Win=findViewById(R.id.player2WinScoreText);
        player2Draw=findViewById(R.id.player2DrawScoreText);
        player2Lose=findViewById(R.id.player2LoseScoreText);
        player1StoneBtn=findViewById(R.id.player1StoneButton);
        player1PaperBtn=findViewById(R.id.player1PaperButton);
        player1ScissorsBtn=findViewById(R.id.player1ScissorButton);
        player2StoneBtn=findViewById(R.id.player2StoneButton);
        player2PaperBtn=findViewById(R.id.player2PaperButton);
        player2ScissorsBtn=findViewById(R.id.player2ScissorButton);

        // Button Events
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sound Effect
                startButtonClickSound();
                // Close This Activity
                finish();
            }
        });
        // Gameplay Buttons
        player1StoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sound Effect
                startButtonClickSound();
                // Start Game
                gameStart(context,PLAYER_1,ITEM_STONE);
            }
        });
        player1PaperBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sound Effect
                startButtonClickSound();
                // Start Game
                gameStart(context,PLAYER_1,ITEM_PAPER);
            }
        });
        player1ScissorsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sound Effect
                startButtonClickSound();
                // Start Game
                gameStart(context,PLAYER_1,ITEM_SCISSORS);
            }
        });
        player2StoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sound Effect
                startButtonClickSound();
                // Start Game
                gameStart(context,PLAYER_2,ITEM_STONE);
            }
        });
        player2PaperBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sound Effect
                startButtonClickSound();
                // Start Game
                gameStart(context,PLAYER_2,ITEM_PAPER);
            }
        });
        player2ScissorsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sound Effect
                startButtonClickSound();
                // Start Game
                gameStart(context,PLAYER_2,ITEM_SCISSORS);
            }
        });

        // Check Sign-in and Print Player 1 Name
        if(isSignedIn())
            player1Name.setText(playerAccount.getDisplayName());
        else
            player1Name.setText(getString(R.string.player_text));

        // Print Player Data
        player1Win.setText(player1_win);
        player1Draw.setText(player1_draw);
        player1Lose.setText(player1_lose);
        player2Win.setText(player2_win);
        player2Draw.setText(player2_draw);
        player2Lose.setText(player2_lose);

    }
}