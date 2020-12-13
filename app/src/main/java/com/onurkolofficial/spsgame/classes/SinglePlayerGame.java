package com.onurkolofficial.spsgame.classes;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.games.Games;
import com.onurkolofficial.spsgame.R;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import pl.droidsonroids.gif.GifImageView;

import static com.onurkolofficial.spsgame.activity.MainActivity.getActivity;
import static com.onurkolofficial.spsgame.activity.MainActivity.isSignedIn;
import static com.onurkolofficial.spsgame.classes.GameAnimations.JumpAnimation;
import static com.onurkolofficial.spsgame.classes.GameAnimations.ScaleAnimation;
import static com.onurkolofficial.spsgame.classes.GameAnimations.ShakeAnimation;
import static com.onurkolofficial.spsgame.classes.GameSound.startDrawSound;
import static com.onurkolofficial.spsgame.classes.GameSound.startLoseSound;
import static com.onurkolofficial.spsgame.classes.GameSound.startWinSound;
import static com.onurkolofficial.spsgame.classes.GameVibration.startGameVibration;
import static com.onurkolofficial.spsgame.data.GamePlayerData.getPlayerData;
import static com.onurkolofficial.spsgame.data.GamePlayerData.getPlayerDataPreferences;
import static com.onurkolofficial.spsgame.data.GamePlayerData.setPlayerData;

public class SinglePlayerGame {
    // Define Game Item ids.
    public static final int ITEM_STONE=getActivity().getResources().getInteger(R.integer.ITEM_STONE),
            ITEM_PAPER=getActivity().getResources().getInteger(R.integer.ITEM_PAPER),
            ITEM_SCISSORS=getActivity().getResources().getInteger(R.integer.ITEM_SCISSORS),
            ITEM_IRON=getActivity().getResources().getInteger(R.integer.ITEM_IRON);

    private static final int WIN_GAME=100,
            DRAW_GAME=101,
            LOSE_GAME=102,
            ERROR_CODE=-100;

    private static final int VIBRATE_WIN_DURATION=45,
            VIBRATE_DRAW_DURATION=35,
            VIBRATE_LOSE_DURATION=50;

    private static final ArrayList<Integer> WIN_X_SCORE=new ArrayList<>();

    // Game Elements : SinglePlayerActivity
    static ImageView playerSelect, comSelect, gameStatus, gameStatusScore;
    static TextView stoneBtn,paperBtn,scissorsBtn,ironBtn,playerName,computerName,playerMoney,playerScore,playerWin,playerDraw,playerLose;
    static GifImageView gameWaitingGifView;

    static int temp_win,temp_lose,temp_draw,temp_score,temp_money;

    // Define Computer Level. Computer Level is select item count.
    // If COMPUTER_LEVEL set to 4, computer now selected ITEM_IRON.
    private static final int COMPUTER_LEVEL=3; // ITEM ID
    // Define Timeout
    private static final int GAME_DELAY=1500; // Millisecond

    // Computer Select Item
    private static int COMPUTER_ITEM_ID;

    public static int computerSelect(){
        // Algorithm: Random select item.
        Random rand=new Random();
        // Item Code is "1000"+[0,1,2,3]
        String selectString="1000"+rand.nextInt(COMPUTER_LEVEL);
        // Convert and Return
        return Integer.parseInt(selectString);
    }

    public static void gameStart(Activity context, int ITEM_ID){
        // Get Elements in View (Context)
        playerSelect=(ImageView)context.findViewById(R.id.playerSelectOut);
        comSelect=(ImageView)context.findViewById(R.id.comSelectOut);
        gameWaitingGifView=(GifImageView)context.findViewById(R.id.gameWaitingGifView);
        stoneBtn=(TextView)context.findViewById(R.id.stoneButton);
        paperBtn=(TextView)context.findViewById(R.id.paperButton);
        scissorsBtn=(TextView)context.findViewById(R.id.scissorButton);
        ironBtn=(TextView)context.findViewById(R.id.ironButton);
        gameStatus=(ImageView)context.findViewById(R.id.gameStatusImage);
        gameStatusScore=(ImageView)context.findViewById(R.id.gameStatusXScore);
        playerName=(TextView)context.findViewById(R.id.playerNameText);
        computerName=(TextView)context.findViewById(R.id.computerNameText);
        playerMoney=(TextView)context.findViewById(R.id.gamePlayerMoney);
        playerScore=(TextView)context.findViewById(R.id.gamePlayerScore);
        playerWin=(TextView)context.findViewById(R.id.winScoreText);
        playerDraw=(TextView)context.findViewById(R.id.drawScoreText);
        playerLose=(TextView)context.findViewById(R.id.loseScoreText);

        // Disable All Buttons (Waiting Next Round.)
        stoneBtn.setEnabled(false);
        paperBtn.setEnabled(false);
        scissorsBtn.setEnabled(false);
        ironBtn.setEnabled(false);

        if(ITEM_ID==ITEM_STONE){
            // Show Select Image
            playerSelect.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.stone_32));
            playerSelect.startAnimation(ShakeAnimation());
        }
        else if(ITEM_ID==ITEM_PAPER){
            // Show Select Image
            playerSelect.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.paper_32));
            playerSelect.startAnimation(ShakeAnimation());
        }
        else if(ITEM_ID==ITEM_SCISSORS){
            // Show Select Image
            playerSelect.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.scissors_32));
            playerSelect.startAnimation(ShakeAnimation());
        }
        else if(ITEM_ID==ITEM_IRON){
            // Show Select Image
            playerSelect.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.iron_32));
            playerSelect.startAnimation(ShakeAnimation());
        }

        // Timeout Computer Select
        // Draw Loading Gif
        gameWaitingGifView.setVisibility(View.VISIBLE);
        comSelect.setVisibility(View.GONE);

        // Create Runnable Task
        final Runnable showGameStatus = new Runnable() {
            @Override
            public void run() {
                // Get Computer Select.
                COMPUTER_ITEM_ID=computerSelect();
                // Playing Game
                int gameStatus=gamePlay(ITEM_ID,COMPUTER_ITEM_ID);
                // Exec Game Status
                if(gameStatus==WIN_GAME)
                    winGame(context);
                else if(gameStatus==DRAW_GAME)
                    drawGame(context);
                else if(gameStatus==LOSE_GAME)
                    loseGame(context);
                else if(gameStatus==ERROR_CODE)
                    Toast.makeText(context, context.getString(R.string.unknown_error_text), Toast.LENGTH_LONG).show();

                // Hide Loading Gif and Show Computer Select
                gameWaitingGifView.setVisibility(View.GONE);
                comSelect.setVisibility(View.VISIBLE);
                if(COMPUTER_ITEM_ID==ITEM_STONE)
                    comSelect.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.stone_32));
                else if(COMPUTER_ITEM_ID==ITEM_PAPER)
                    comSelect.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.paper_32));
                else if(COMPUTER_ITEM_ID==ITEM_SCISSORS)
                    comSelect.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.scissors_32));
                else if(COMPUTER_ITEM_ID==ITEM_IRON)
                    comSelect.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.iron_32));
                // Computer Select Animation
                comSelect.startAnimation(ShakeAnimation());

                // Check Sign-in Account
                // If sign-in account to save scores and achievements.
                if(isSignedIn()){
                    saveLeaderBoard(temp_score);
                    saveAchievementScore(getActivity().getString(R.string.rich_1k_achievement),temp_money);
                    saveAchievementScore(getActivity().getString(R.string.protectscore_achievement),temp_score);
                }

                // Enabled All Buttons
                stoneBtn.setEnabled(true);
                paperBtn.setEnabled(true);
                scissorsBtn.setEnabled(true);
                ironBtn.setEnabled(true);
            }
        };
        // Run Task '1500' Millisecond.
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                context.runOnUiThread(showGameStatus);
            }
        }, GAME_DELAY);
        
    }

    private static int gamePlay(int PLAYER_ITEM_ID,int COMPUTER_ITEM_ID){
        int RETURN=ERROR_CODE;
        if(PLAYER_ITEM_ID==ITEM_STONE){
            if(COMPUTER_ITEM_ID==ITEM_STONE)
                RETURN=DRAW_GAME;
            else if(COMPUTER_ITEM_ID==ITEM_PAPER)
                RETURN=LOSE_GAME;
            else if(COMPUTER_ITEM_ID==ITEM_SCISSORS)
                RETURN=WIN_GAME;
            else if(COMPUTER_ITEM_ID==ITEM_IRON)
                RETURN=LOSE_GAME;
        }
        else if(PLAYER_ITEM_ID==ITEM_PAPER){
            if(COMPUTER_ITEM_ID==ITEM_STONE)
                RETURN=WIN_GAME;
            else if(COMPUTER_ITEM_ID==ITEM_PAPER)
                RETURN=DRAW_GAME;
            else if(COMPUTER_ITEM_ID==ITEM_SCISSORS)
                RETURN=LOSE_GAME;
            else if(COMPUTER_ITEM_ID==ITEM_IRON)
                RETURN=WIN_GAME;
        }
        else if(PLAYER_ITEM_ID==ITEM_SCISSORS){
            if(COMPUTER_ITEM_ID==ITEM_STONE)
                RETURN=LOSE_GAME;
            else if(COMPUTER_ITEM_ID==ITEM_PAPER)
                RETURN=WIN_GAME;
            else if(COMPUTER_ITEM_ID==ITEM_SCISSORS)
                RETURN=DRAW_GAME;
            else if(COMPUTER_ITEM_ID==ITEM_IRON)
                RETURN=LOSE_GAME;
        }
        else if(PLAYER_ITEM_ID==ITEM_IRON){
            if(COMPUTER_ITEM_ID==ITEM_STONE)
                RETURN=WIN_GAME;
            else if(COMPUTER_ITEM_ID==ITEM_PAPER)
                RETURN=LOSE_GAME;
            else if(COMPUTER_ITEM_ID==ITEM_SCISSORS)
                RETURN=WIN_GAME;
            else if(COMPUTER_ITEM_ID==ITEM_IRON)
                RETURN=DRAW_GAME;
        }
        return RETURN;
    }

    // Game Events
    public static void winGame(Activity context){
        // Sound Effect
        startWinSound();
        // Vibration
        startGameVibration(VIBRATE_WIN_DURATION);
        // Random
        Random rand=new Random();
        // Win Text
        gameStatus.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.win_image));
        gameStatus.startAnimation(ScaleAnimation());
        // Get Scores
        temp_win=Integer.parseInt(getPlayerData(getPlayerDataPreferences(),"Game.Player.Winscore"));
        temp_score=Integer.parseInt(getPlayerData(getPlayerDataPreferences(),"Game.Player.Totalscore"));
        temp_money=Integer.parseInt(getPlayerData(getPlayerDataPreferences(),"Game.Player.Money"));
        // Count
        temp_win+=1;
        // Win Score count min. 3, max. 10 by random.
        // if random small than 3 to new score +3 and random+7
        int cscore=rand.nextInt(10);
        if(cscore<3) cscore=3+rand.nextInt(7);
        temp_score+=cscore;
        // Win Money count min. 1, max. 3.
        // Because we want to increase the number of plays for the store. :)
        int cmoney=rand.nextInt(3);
        if(cmoney==0) cmoney=1+rand.nextInt(2);
        temp_money+=cmoney;
        // Extra: Win X Score
        // If game detect win series (max. 5) to show serie score in game.
        // And if series is 4 and up to counting extra money and score.
        WIN_X_SCORE.add(WIN_GAME);
        if(WIN_X_SCORE.size()==2)
            gameStatusScore.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.game_win_x2));
        else if(WIN_X_SCORE.size()==3){
            gameStatusScore.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.game_win_x3));
            // Win the Achievement
            if(isSignedIn())
                unlockAchievement(getActivity().getString(R.string.series_3match_achievement));
        }
        else if(WIN_X_SCORE.size()==4) {
            gameStatusScore.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.game_win_x4));
            temp_money+=1;
            temp_score+=2;
        }
        else if(WIN_X_SCORE.size()==5) {
            gameStatusScore.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.game_win_x5));
            temp_money+=2;
            temp_score+=3;
            // Win the Achievement
            if(isSignedIn())
                unlockAchievement(getActivity().getString(R.string.series_5match_achievement));
        }
        else if(WIN_X_SCORE.size()>5) {
            gameStatusScore.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.game_win_x5plus));
            temp_money+=WIN_X_SCORE.size()-2;
            temp_score+=WIN_X_SCORE.size();
            // Win the Achievement
            if(isSignedIn())
                unlockAchievement(getActivity().getString(R.string.winner_achievement));
        }
        gameStatusScore.startAnimation(ShakeAnimation());
        // Player Win Animation
        JumpAnimation(playerName).start();
        // Print Round Statistic.
        playerWin.setText(String.valueOf(temp_win));
        playerScore.setText(String.valueOf(temp_score));
        playerMoney.setText(String.valueOf(temp_money));
        // Save Data
        saveData("Game.Player.Winscore",String.valueOf(temp_win));
        saveData("Game.Player.Totalscore",String.valueOf(temp_score));
        saveData("Game.Player.Money",String.valueOf(temp_money));
    }
    public static void drawGame(Activity context){
        // Sound Effect
        startDrawSound();
        // Vibration
        startGameVibration(VIBRATE_DRAW_DURATION);
        // Random
        Random rand=new Random();
        // Draw Text
        gameStatus.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.draw_image));
        gameStatus.startAnimation(ScaleAnimation());
        // Get Scores
        temp_draw=Integer.parseInt(getPlayerData(getPlayerDataPreferences(),"Game.Player.Drawscore"));
        temp_score=Integer.parseInt(getPlayerData(getPlayerDataPreferences(),"Game.Player.Totalscore"));
        temp_money=Integer.parseInt(getPlayerData(getPlayerDataPreferences(),"Game.Player.Money"));
        // Count
        temp_draw+=1;
        // Draw Score count min. 1, max. 3 by random.
        int cscore=rand.nextInt(3);
        if(cscore<3) cscore=1+rand.nextInt(2);
        temp_score+=cscore;
        // Draw Game Money is 1.
        temp_money+=1;
        // Extra: Remove Win Series
        if(WIN_X_SCORE!=null)
            WIN_X_SCORE.clear();
        gameStatusScore.setImageDrawable(ContextCompat.getDrawable(context,R.color.transparent));
        // Print Round Statistic.
        playerDraw.setText(String.valueOf(temp_draw));
        playerScore.setText(String.valueOf(temp_score));
        playerMoney.setText(String.valueOf(temp_money));
        // Save Data
        saveData("Game.Player.Drawscore",String.valueOf(temp_draw));
        saveData("Game.Player.Totalscore",String.valueOf(temp_score));
        saveData("Game.Player.Money",String.valueOf(temp_money));

    }
    public static void loseGame(Activity context){
        // Sound Effect
        startLoseSound();
        // Vibration
        startGameVibration(VIBRATE_LOSE_DURATION);
        // Random
        Random rand=new Random();
        // Lose Text
        gameStatus.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.lose_image));
        gameStatus.startAnimation(ScaleAnimation());
        // Get Scores
        temp_lose=Integer.parseInt(getPlayerData(getPlayerDataPreferences(),"Game.Player.Losescore"));
        temp_score=Integer.parseInt(getPlayerData(getPlayerDataPreferences(),"Game.Player.Totalscore"));
        // Count
        temp_lose+=1;
        // Lose Score is discount total score.
        // Discount min. 2, max. 5;
        int cscore=rand.nextInt(5);
        if(cscore<2) cscore=2+rand.nextInt(5);
        temp_score-=cscore;
        // Note! Min. score must be 0.
        if(temp_score<0) temp_score=0;
        // Lose is not count money.
        // Extra: Remove Win Series
        if(WIN_X_SCORE!=null)
            WIN_X_SCORE.clear();
        gameStatusScore.setImageDrawable(ContextCompat.getDrawable(context,R.color.transparent));
        // Computer Win Animation
        JumpAnimation(computerName).start();
        // Print Round Statistic.
        playerLose.setText(String.valueOf(temp_lose));
        playerScore.setText(String.valueOf(temp_score));
        // Save Data
        saveData("Game.Player.Losescore",String.valueOf(temp_lose));
        saveData("Game.Player.Totalscore",String.valueOf(temp_score));
    }

    // Save Game Data to Preferences.
    // Because if player save to google play games, getting data in Preferences.
    // : 'classes/LoadSaveData.java'
    public static void saveData(String data_id, String data_value){
        setPlayerData(getPlayerDataPreferences(),data_id,data_value);
    }

    // If player sign-in Google Play Games to live saving total score in leaderboard.
    public static void saveLeaderBoard(int Score){
        Games.getLeaderboardsClient(getActivity(), GoogleSignIn.getLastSignedInAccount(getActivity()))
                .submitScore(getActivity().getString(R.string.scoreboard_id), Score);
    }
    public static void saveAchievementScore(String achievementId, int increment){
        Games.getAchievementsClient(getActivity(), GoogleSignIn.getLastSignedInAccount(getActivity()))
                .increment(achievementId, 1);
    }
    public static void unlockAchievement(String achievementId){
        Games.getAchievementsClient(getActivity(), GoogleSignIn.getLastSignedInAccount(getActivity()))
                .unlock(achievementId);
    }

}
