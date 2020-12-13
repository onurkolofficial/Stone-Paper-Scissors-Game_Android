package com.onurkolofficial.spsgame.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.SnapshotsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.onurkolofficial.spsgame.R;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;

import static com.onurkolofficial.spsgame.classes.GameSound.startBackgroundMusic;
import static com.onurkolofficial.spsgame.classes.GameSound.startButtonClickSound;
import static com.onurkolofficial.spsgame.classes.LoadSaveData.loadGameData;
import static com.onurkolofficial.spsgame.classes.LoadSaveData.saveGameData;
import static com.onurkolofficial.spsgame.classes.LoadSaveData.showSaveLoadIntent;
import static com.onurkolofficial.spsgame.data.DataPreference.startPreferenceSession;

public class MainActivity extends AppCompatActivity {
    // Google Play Game Service
    // Client used to sign in with Google APIs
    private GoogleSignInClient mGoogleSignInClient;
    public static GoogleSignInAccount playerAccount;

    private static final int RC_SIGN_IN = 9001;
    private static final int RC_LEADERBOARD_UI = 9004;
    public static final int RC_SAVED_GAMES = 9009;
    private static final int RC_ACHIEVEMENT_UI = 9003;

    private static Context context;
    private static Activity activity;

    public TextView userDispName,startGameBtn,exitBtn,infoBtn,settingsBtn,signinBtn,signoutBtn,scoreboardBtn,saveloadBtn,achievementsBtn;

    public Intent settingsIntent,gameModeIntent,infoIntent;

    public static ProgressDialog loadingDialog;

    public static String GameMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set Context, Activity
        MainActivity.context=this;
        MainActivity.activity=this;

        // Start Preference Session.
        startPreferenceSession();

        // Create View
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ///////////////////////////////////
        // Initializing ADS. (StartApp SDK)
        // Ads Initialize
        //StartAppSDK.setTestAdsEnabled(true);
        String getAppId=getResources().getString(R.string.startapp_app_id);
        StartAppSDK.init(this, getAppId, false);
        // Disable Startapp Splash Screen.
        StartAppAd.disableSplash();
        //////////////////////////////////

        // Get View Elements
        userDispName=findViewById(R.id.userDisplayName);
        startGameBtn=findViewById(R.id.startGameButton);
        exitBtn=findViewById(R.id.exitGameButton);
        infoBtn=findViewById(R.id.infoButton);
        settingsBtn=findViewById(R.id.settingsButton);
        signinBtn=findViewById(R.id.signinButton);
        signoutBtn=findViewById(R.id.signoutButton);
        scoreboardBtn=findViewById(R.id.scoreboardButton);
        saveloadBtn=findViewById(R.id.saveloadButton);
        achievementsBtn=findViewById(R.id.achievementButton);

        // Loading Progress Dialog
        loadingDialog=new ProgressDialog(getActivity());
        loadingDialog.setMessage(getActivity().getString(R.string.loading_data_text));

        // GoogleSignInClient.
        GoogleSignInOptions signInOptions=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
                .requestScopes(Games.SCOPE_GAMES_SNAPSHOTS).requestProfile()
                .build();
        mGoogleSignInClient=GoogleSignIn.getClient(this, signInOptions);

        // Button Events
        startGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sound Effect
                startButtonClickSound();
                // Start Game Activity
                gameModeIntent=new Intent(getActivity(), GameModeActivity.class);
                startActivity(gameModeIntent);
            }
        });
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sound Effect
                startButtonClickSound();
                // Start Settings Activity
                settingsIntent=new Intent(getActivity(), SettingsActivity.class);
                startActivity(settingsIntent);
            }
        });
        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sound Effect
                startButtonClickSound();
                // Start Info Activity
                infoIntent=new Intent(getActivity(), InfoActivity.class);
                startActivity(infoIntent);
            }
        });
        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sound Effect
                startButtonClickSound();
                // Show Loading Dialog
                loadingDialog.show();
                // Signin Game
                showSignInIntent();
            }
        });
        signoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sound Effect
                startButtonClickSound();
                // Show Loading Dialog
                loadingDialog.show();
                // Signout Account
                signOut();
            }
        });
        scoreboardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sound Effect
                startButtonClickSound();
                // Show Loading Dialog
                loadingDialog.show();
                // Show Score Board
                showLeaderboard();
            }
        });
        saveloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sound Effect
                startButtonClickSound();
                // Show Loading Dialog
                loadingDialog.show();
                // Open Save Load Intent.
                showSaveLoadIntent();
            }
        });
        achievementsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sound Effect
                startButtonClickSound();
                // Show Loading Dialog
                loadingDialog.show();
                // Open Achievements.
                showAchievements();
            }
        });
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sound Effect
                startButtonClickSound();
                // Exit Game
                System.exit(0);
            }
        });
        // Start background music.
        startBackgroundMusic();
    }

    public static boolean isSignedIn() {
        return GoogleSignIn.getLastSignedInAccount(getActivity())!=null;
    }
    private void showSignInIntent() {
        startActivityForResult(mGoogleSignInClient.getSignInIntent(), RC_SIGN_IN);
    }
    private void signOut() {
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
            new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    // at this point, the user is signed out.
                    signinBtn.setVisibility(View.VISIBLE);
                    signoutBtn.setVisibility(View.GONE);
                    scoreboardBtn.setVisibility(View.GONE);
                    saveloadBtn.setVisibility(View.GONE);
                    achievementsBtn.setVisibility(View.GONE);
                    userDispName.setText(getString(R.string.player_text));
                    // Hide Loading Dialog
                    if(loadingDialog.isShowing())
                        loadingDialog.dismiss();
                }
        });
    }
    private void showAchievements() {
        Games.getAchievementsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .getAchievementsIntent()
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        // Hide Loading Dialog
                        if(loadingDialog.isShowing())
                            loadingDialog.dismiss();
                        startActivityForResult(intent, RC_ACHIEVEMENT_UI);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Success Sign-in
                playerAccount=task.getResult(ApiException.class);
            } catch (ApiException apiException) {
                new AlertDialog.Builder(this)
                        .setMessage(getString(R.string.unknown_error_text))
                        .setNeutralButton(android.R.string.ok, null)
                        .show();
            }
            // Hide Loading Dialog
            if(loadingDialog.isShowing())
                loadingDialog.dismiss();
        }
        // Check Save Game
        if(resultCode!=0) {
            if (data.hasExtra(SnapshotsClient.EXTRA_SNAPSHOT_METADATA)) {
                // Load Game
                loadGameData(data);
            } else if (data.hasExtra(SnapshotsClient.EXTRA_SNAPSHOT_NEW)) {
                // Save Game
                saveGameData();
            }
        }
    }

    public static Context getContext() {
        return MainActivity.context;
    }
    public static Activity getActivity(){
        return MainActivity.activity;
    }

    private void showLeaderboard() {
        Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .getLeaderboardIntent(getString(R.string.scoreboard_id))
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        // Hide Loading Dialog
                        if(loadingDialog.isShowing())
                            loadingDialog.dismiss();
                        startActivityForResult(intent, RC_LEADERBOARD_UI);
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Check for existing Google Sign In account.
        if(isSignedIn()){
            signinBtn.setVisibility(View.GONE);
            signoutBtn.setVisibility(View.VISIBLE);
            scoreboardBtn.setVisibility(View.VISIBLE);
            saveloadBtn.setVisibility(View.VISIBLE);
            achievementsBtn.setVisibility(View.VISIBLE);
            playerAccount=GoogleSignIn.getLastSignedInAccount(this);
            userDispName.setText(playerAccount.getDisplayName());
        }
        else{
            signinBtn.setVisibility(View.VISIBLE);
            signoutBtn.setVisibility(View.GONE);
            scoreboardBtn.setVisibility(View.GONE);
            saveloadBtn.setVisibility(View.GONE);
            achievementsBtn.setVisibility(View.GONE);
            userDispName.setText(getString(R.string.player_text));
        }
    }
}