package com.onurkolofficial.spsgame.classes;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.onurkolofficial.spsgame.R;
import com.onurkolofficial.spsgame.activity.TwoPlayerActivity;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import pl.droidsonroids.gif.GifImageView;

import static com.onurkolofficial.spsgame.activity.MainActivity.getActivity;
import static com.onurkolofficial.spsgame.activity.MainActivity.isSignedIn;
import static com.onurkolofficial.spsgame.activity.TwoPlayerActivity.session_player1_draw;
import static com.onurkolofficial.spsgame.activity.TwoPlayerActivity.session_player1_lose;
import static com.onurkolofficial.spsgame.activity.TwoPlayerActivity.session_player1_win;
import static com.onurkolofficial.spsgame.activity.TwoPlayerActivity.session_player2_draw;
import static com.onurkolofficial.spsgame.activity.TwoPlayerActivity.session_player2_lose;
import static com.onurkolofficial.spsgame.activity.TwoPlayerActivity.session_player2_win;
import static com.onurkolofficial.spsgame.classes.GameAnimations.JumpAnimation;
import static com.onurkolofficial.spsgame.classes.GameAnimations.ScaleAnimation;
import static com.onurkolofficial.spsgame.classes.GameAnimations.ShakeAnimation;
import static com.onurkolofficial.spsgame.classes.GameSound.startDrawSound;
import static com.onurkolofficial.spsgame.classes.GameVibration.startGameVibration;
import static com.onurkolofficial.spsgame.classes.SinglePlayerGame.unlockAchievement;

public class TwoPlayerGame {
    /// !WARNING
    // Two Player Game mode hasn't Data.
    // Data save is an activity session.
    // If player go back menu and re-open '2 Player' game mode is all data reset.
    // **************************************************************************

    // Define Game Item ids.
    public static final int NO_ITEM=-1,
            ITEM_STONE=getActivity().getResources().getInteger(R.integer.ITEM_STONE),
            ITEM_PAPER=getActivity().getResources().getInteger(R.integer.ITEM_PAPER),
            ITEM_SCISSORS=getActivity().getResources().getInteger(R.integer.ITEM_SCISSORS),
            ITEM_IRON=getActivity().getResources().getInteger(R.integer.ITEM_IRON);

    private static final int PLAYER_1_WIN_GAME=100,
            PLAYER_1_DRAW_GAME=101,
            PLAYER_1_LOSE_GAME=102,
            ERROR_CODE=-100;

    private static final int GAME_VIBRATE_DURATION=40;

    // Define Player Ids
    public static final int PLAYER_1=10,
            PLAYER_2=11;
    public static int PLAYER_1_ITEM_ID=NO_ITEM,
            PLAYER_2_ITEM_ID=NO_ITEM;

    // Game Elements : TwoPlayerActivity
    static ImageView player1Select, player2Select, player1Status, player2Status;
    static TextView player1Name,player1Win,player1Draw,player1Lose,player2Name,player2Win,player2Draw,player2Lose,
            player1StoneBtn,player1PaperBtn,player1ScissorsBtn,player2StoneBtn,player2PaperBtn,player2ScissorsBtn;;
    static GifImageView player1GifView,player2GifView;

    // Define Timeout
    // If 1 Player select and another player is not select, !Win the select player.
    private static final int GAME_DELAY=900; // Millisecond
    private static boolean STOP_TASK;
    private static Timer timer;
    private static TimerTask task;
    private static Runnable showGameStatus;

    public static void gameStart(Activity context, int PLAYER_ID, int ITEM_ID){
        // Get Elements in View (Context)
        player1Name=(TextView)context.findViewById(R.id.player1NameText);
        player1Win=(TextView)context.findViewById(R.id.player1WinScoreText);
        player1Draw=(TextView)context.findViewById(R.id.player1DrawScoreText);
        player1Lose=(TextView)context.findViewById(R.id.player1LoseScoreText);
        player2Name=(TextView)context.findViewById(R.id.player2NameText);
        player2Win=(TextView)context.findViewById(R.id.player2WinScoreText);
        player2Draw=(TextView)context.findViewById(R.id.player2DrawScoreText);
        player2Lose=(TextView)context.findViewById(R.id.player2LoseScoreText);
        player1StoneBtn=(TextView)context.findViewById(R.id.player1StoneButton);
        player1PaperBtn=(TextView)context.findViewById(R.id.player1PaperButton);
        player1ScissorsBtn=(TextView)context.findViewById(R.id.player1ScissorButton);
        player2StoneBtn=(TextView)context.findViewById(R.id.player2StoneButton);
        player2PaperBtn=(TextView)context.findViewById(R.id.player2PaperButton);
        player2ScissorsBtn=(TextView)context.findViewById(R.id.player2ScissorButton);
        player1Select=(ImageView)context.findViewById(R.id.player1SelectOut);
        player2Select=(ImageView)context.findViewById(R.id.player2SelectOut);
        player1Status=(ImageView)context.findViewById(R.id.player1GameStatus);
        player2Status=(ImageView)context.findViewById(R.id.player2GameStatus);
        player1GifView=(GifImageView)context.findViewById(R.id.player1GifView);
        player2GifView=(GifImageView)context.findViewById(R.id.player2GifView);

        // Create Runnable Task
        showGameStatus=new Runnable() {
            @Override
            public void run() {
                // Check Items
                if(PLAYER_1_ITEM_ID==NO_ITEM){
                    winGame(context,PLAYER_2);
                }
                else if(PLAYER_2_ITEM_ID==NO_ITEM){
                    winGame(context,PLAYER_1);
                }
                else{
                    // Playing Game
                    int gameStatus=gamePlay(PLAYER_1_ITEM_ID,PLAYER_2_ITEM_ID);
                    // Exec Game Status
                    if(gameStatus==PLAYER_1_WIN_GAME)
                        winGame(context,PLAYER_1);
                    else if(gameStatus==PLAYER_1_DRAW_GAME)
                        drawGame(context);
                    else if(gameStatus==PLAYER_1_LOSE_GAME)
                        winGame(context,PLAYER_2);
                    else if(gameStatus==ERROR_CODE)
                        Toast.makeText(context, context.getString(R.string.unknown_error_text), Toast.LENGTH_LONG).show();

                    // Hide Loading Gif and Show ALL
                    player2GifView.setVisibility(View.GONE);
                    player2Select.setVisibility(View.VISIBLE);
                    player1GifView.setVisibility(View.GONE);
                    player1Select.setVisibility(View.VISIBLE);
                }

                // Check Sign-in Account
                // If sign-in account to save scores and achievements.
                if(isSignedIn()){
                    unlockAchievement(context.getString(R.string.entertainment_achievement));
                }

                // Enabled All Buttons
                player1StoneBtn.setEnabled(true);
                player1PaperBtn.setEnabled(true);
                player1ScissorsBtn.setEnabled(true);
                player2StoneBtn.setEnabled(true);
                player2PaperBtn.setEnabled(true);
                player2ScissorsBtn.setEnabled(true);

                // Reset Player Items
                // Because retry task process.
                PLAYER_1_ITEM_ID=NO_ITEM;
                PLAYER_2_ITEM_ID=NO_ITEM;

                STOP_TASK=true;
                timer=null;
                task=null;
            }
        };
        // Timer
        timer=new Timer();
        // Timer Task
        task=new TimerTask(){
            @Override
            public void run() {
                if(STOP_TASK) {
                    this.cancel();
                }
                else
                    context.runOnUiThread(showGameStatus);
            }
        };

        if(PLAYER_ID==PLAYER_1){
            // Set Item
            PLAYER_1_ITEM_ID=ITEM_ID;
            // Show Selected Item
            if(ITEM_ID==ITEM_STONE){
                // Show Select Image
                player1Select.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.stone_32));
                player1Select.startAnimation(ShakeAnimation());
            }
            else if(ITEM_ID==ITEM_PAPER){
                // Show Select Image
                player1Select.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.paper_32));
                player1Select.startAnimation(ShakeAnimation());
            }
            else if(ITEM_ID==ITEM_SCISSORS){
                // Show Select Image
                player1Select.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.scissors_32));
                player1Select.startAnimation(ShakeAnimation());
            }
            // Hide Gif View
            player1GifView.setVisibility(View.GONE);

            // Disable Buttons Player 1
            player1StoneBtn.setEnabled(false);
            player1PaperBtn.setEnabled(false);
            player1ScissorsBtn.setEnabled(false);

            // Check Player 2 Select.
            // If Player 2 isn't select to start timeout task.
            if(PLAYER_2_ITEM_ID==NO_ITEM){
                STOP_TASK=false;
                // Draw Loading Gif
                player2GifView.setVisibility(View.VISIBLE);
                player2Select.setVisibility(View.GONE);
                // Waiting Player 2 Select
                timer.schedule(task,GAME_DELAY);
            }
            else{
                // Play Game.
                task.run();
            }
        }
        else if(PLAYER_ID==PLAYER_2){
            // Set Item
            PLAYER_2_ITEM_ID=ITEM_ID;
            // Show Selected Item
            if(ITEM_ID==ITEM_STONE){
                // Show Select Image
                player2Select.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.stone_32));
                player2Select.startAnimation(ShakeAnimation());
            }
            else if(ITEM_ID==ITEM_PAPER){
                // Show Select Image
                player2Select.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.paper_32));
                player2Select.startAnimation(ShakeAnimation());
            }
            else if(ITEM_ID==ITEM_SCISSORS){
                // Show Select Image
                player2Select.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.scissors_32));
                player2Select.startAnimation(ShakeAnimation());
            }
            // Hide Gif View
            player1GifView.setVisibility(View.GONE);

            // Disable Buttons Player 2
            player2StoneBtn.setEnabled(false);
            player2PaperBtn.setEnabled(false);
            player2ScissorsBtn.setEnabled(false);

            // Check Player 1 Select.
            // If Player 1 isn't select to start timeout task.
            if(PLAYER_1_ITEM_ID==NO_ITEM){
                STOP_TASK=false;
                // Draw Loading Gif
                player1GifView.setVisibility(View.VISIBLE);
                player1Select.setVisibility(View.GONE);
                // Waiting Player 1 Select
                timer.schedule(task,GAME_DELAY);
            }
            else{
                // Play Game.
                task.run();
            }
        }
        Log.e("TwoPlayerGame","Click Player ("+PLAYER_ID+") Player 1: ("+PLAYER_1_ITEM_ID+")  Player 2: ("+PLAYER_2_ITEM_ID+")");
    }

    private static int gamePlay(int PLAYER_1_ITEM_ID,int PLAYER_2_ITEM_ID){
        int RETURN=ERROR_CODE;
        if(PLAYER_1_ITEM_ID==ITEM_STONE){
            if(PLAYER_2_ITEM_ID==ITEM_STONE)
                RETURN=PLAYER_1_DRAW_GAME;
            else if(PLAYER_2_ITEM_ID==ITEM_PAPER)
                RETURN=PLAYER_1_LOSE_GAME;
            else if(PLAYER_2_ITEM_ID==ITEM_SCISSORS)
                RETURN=PLAYER_1_WIN_GAME;
        }
        else if(PLAYER_1_ITEM_ID==ITEM_PAPER){
            if(PLAYER_2_ITEM_ID==ITEM_STONE)
                RETURN=PLAYER_1_WIN_GAME;
            else if(PLAYER_2_ITEM_ID==ITEM_PAPER)
                RETURN=PLAYER_1_DRAW_GAME;
            else if(PLAYER_2_ITEM_ID==ITEM_SCISSORS)
                RETURN=PLAYER_1_LOSE_GAME;
        }
        else if(PLAYER_1_ITEM_ID==ITEM_SCISSORS){
            if(PLAYER_2_ITEM_ID==ITEM_STONE)
                RETURN=PLAYER_1_LOSE_GAME;
            else if(PLAYER_2_ITEM_ID==ITEM_PAPER)
                RETURN=PLAYER_1_WIN_GAME;
            else if(PLAYER_2_ITEM_ID==ITEM_SCISSORS)
                RETURN=PLAYER_1_DRAW_GAME;
        }
        return RETURN;
    }

    public static void winGame(Activity context, int PLAYER_ID){
        // Sound Effect
        startDrawSound();
        // Vibration
        startGameVibration(GAME_VIBRATE_DURATION);
        // Check Win Reason (Timeout or Normal)
        if(PLAYER_1_ITEM_ID==NO_ITEM){
            // Player 1 Timeout
            player1Select.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.time_32));
            player1Select.startAnimation(ShakeAnimation());
            player1Select.setVisibility(View.VISIBLE);
            player1GifView.setVisibility(View.GONE);
            winPlayer2Event(context);
        }
        else if(PLAYER_2_ITEM_ID==NO_ITEM){
            // Player 2 Timeout
            player2Select.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.time_32));
            player2Select.startAnimation(ShakeAnimation());
            player2Select.setVisibility(View.VISIBLE);
            player2GifView.setVisibility(View.GONE);
            winPlayer1Event(context);
        }
        else{
            if(PLAYER_ID==PLAYER_1)
                winPlayer1Event(context);
            else if(PLAYER_ID==PLAYER_2)
                winPlayer2Event(context);
        }
    }
    private static void winPlayer1Event(Activity context){
        // Count Data
        session_player1_win+=1;
        session_player2_lose+=1;
        // Status Text
        player1Status.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.win_image));
        player1Status.startAnimation(ScaleAnimation());
        player2Status.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.lose_image));
        player2Status.startAnimation(ScaleAnimation());
        // Player Win Animation
        JumpAnimation(player1Name).start();
        // Print Data
        player1Win.setText(String.valueOf(session_player1_win));
        player2Lose.setText(String.valueOf(session_player2_lose));
    }
    private static void winPlayer2Event(Activity context){
        // Count Data
        session_player1_lose+=1;
        session_player2_win+=1;
        // Status Text
        player1Status.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.lose_image));
        player1Status.startAnimation(ScaleAnimation());
        player2Status.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.win_image));
        player2Status.startAnimation(ScaleAnimation());
        // Player Win Animation
        JumpAnimation(player2Name).start();
        // Print Data
        player1Lose.setText(String.valueOf(session_player1_lose));
        player2Win.setText(String.valueOf(session_player2_win));
    }

    public static void drawGame(Activity context){
        // Sound Effect
        startDrawSound();
        // Vibration
        startGameVibration(GAME_VIBRATE_DURATION);
        // Count Data
        session_player1_draw+=1;
        session_player2_draw+=1;
        // Status Text
        player1Status.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.draw_image));
        player1Status.startAnimation(ScaleAnimation());
        player2Status.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.draw_image));
        player2Status.startAnimation(ScaleAnimation());
        // Print Data
        player1Draw.setText(String.valueOf(session_player1_draw));
        player2Draw.setText(String.valueOf(session_player2_draw));
    }
}
