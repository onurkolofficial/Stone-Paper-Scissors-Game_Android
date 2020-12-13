package com.onurkolofficial.spsgame.classes;

import android.animation.ObjectAnimator;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class GameAnimations {

    public static Animation RotateAnimation(){
        RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.ABSOLUTE);
        anim.setDuration(500);
        return anim;
    }
    public static Animation ReverseRotateAnimation(){
        RotateAnimation anim = new RotateAnimation(360.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.ABSOLUTE);
        anim.setDuration(500);
        return anim;
    }
    public static Animation ShakeAnimation() {
        RotateAnimation anim=new RotateAnimation(-10, 10,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(new CycleInterpolator(4));
        anim.setRepeatMode(Animation.ABSOLUTE);
        anim.setStartOffset(50);
        anim.setDuration(1000);
        return anim;
    }
    public static Animation ScaleAnimation(){
        Animation anim = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f); // Pivot point of Y scaling
        anim.setFillAfter(false); // Needed to keep the result of the animation
        anim.setDuration(1000);
        return anim;
    }
    public static ObjectAnimator JumpAnimation(TextView object){
        ObjectAnimator animObject = ObjectAnimator.ofFloat(object, "translationY", -100f, 0f);
        animObject.setDuration(2000);
        animObject.setInterpolator(new BounceInterpolator());
        animObject.setRepeatCount(0);
        return animObject;
    }
    public static ObjectAnimator JumpAnimation(ImageView object){
        ObjectAnimator animObject = ObjectAnimator.ofFloat(object, "translationY", -90f, 0f);
        animObject.setDuration(2000);
        animObject.setInterpolator(new BounceInterpolator());
        animObject.setRepeatCount(0);
        return animObject;
    }
    public static ObjectAnimator JumpAnimation(Button object){
        ObjectAnimator animObject = ObjectAnimator.ofFloat(object, "translationY", -90f, 0f);
        animObject.setDuration(2000);
        animObject.setInterpolator(new BounceInterpolator());
        animObject.setRepeatCount(0);
        return animObject;
    }
    public static ObjectAnimator JumpAnimation(ImageButton object){
        ObjectAnimator animObject = ObjectAnimator.ofFloat(object, "translationY", -90f, 0f);
        animObject.setDuration(2000);
        animObject.setInterpolator(new BounceInterpolator());
        animObject.setRepeatCount(0);
        return animObject;
    }

}
