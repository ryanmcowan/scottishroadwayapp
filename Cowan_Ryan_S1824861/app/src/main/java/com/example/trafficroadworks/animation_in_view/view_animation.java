package com.example.trafficroadworks.animation_in_view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

public class view_animation {

    //this is animation class which perform the animation in recycler view from right to left position

    //animation time
    private static final long animation_time = 100;

    public static void FromRightToLeft(View unit, int ob, boolean on_connect) {
        if(!on_connect){
            ob = -1;
        }
        boolean not_first_item = ob == -1;
        ob = ob + 1;
        unit.setTranslationX(unit.getX() + 400);
        unit.setAlpha(0.f);
        AnimatorSet set_animation_in_view = new AnimatorSet();


        //object animator connect to view

        ObjectAnimator animation_in_view_Translate_to_Y = ObjectAnimator.ofFloat(unit, "translationX", unit.getX() + 400, 0);

        ObjectAnimator animation_in_view_Alpha = ObjectAnimator.ofFloat(unit, "alpha", 1.f);
        ObjectAnimator.ofFloat(unit, "alpha", 0.f).start();


        animation_in_view_Translate_to_Y.setStartDelay(not_first_item ? animation_time : (ob * animation_time));
        animation_in_view_Translate_to_Y.setDuration((not_first_item ? 2 : 1) * animation_time);

        //set animation to the view
        set_animation_in_view.playTogether(animation_in_view_Translate_to_Y, animation_in_view_Alpha);

        //start animation
        set_animation_in_view.start();
    }
}
