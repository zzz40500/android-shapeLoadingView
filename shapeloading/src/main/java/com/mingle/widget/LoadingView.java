package com.mingle.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mingle.shapeloading.R;



/**
 * Created by zzz40500 on 15/4/6.
 */
public class LoadingView  extends FrameLayout {


    private ShapeLoadingView shapeLoadingView;

    private ImageView indicationIm;

    private TextView loadTextView;


    private   static  final int ANIMATION_DURATION=500;

    private String loadText;


    private     float mDistance =200;
    public LoadingView(Context context) {
        super(context);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs,0);
        init(context,attrs);

    }

    private void init(Context context,AttributeSet attrs) {

        TypedArray typedArray= context
                .obtainStyledAttributes(attrs, R.styleable.LoadingView);
        loadText=  typedArray.getString(R.styleable.LoadingView_loadingText);

        typedArray.recycle();
    }


    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context,attrs);
    }
    public  int dip2px( float dipValue){
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5f);
    }




    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();


      View view=  LayoutInflater.from(getContext()).inflate(R.layout.load_view,null);

        mDistance =dip2px(54f);

        LayoutParams layoutParams=new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        layoutParams.gravity= Gravity.CENTER;

        shapeLoadingView= (ShapeLoadingView) view.findViewById(R.id.shapeLoadingView);

        indicationIm= (ImageView) view.findViewById(R.id.indication);
        loadTextView= (TextView) view.findViewById(R.id.promptTV);

        setLoadingText(loadText);

        addView(view,layoutParams);

        this.postDelayed(new Runnable() {
            @Override
            public void run() {
                freeFall();
            }
        },900);


    }

    public void setLoadingText(CharSequence loadingText){
        loadTextView.setText(loadingText);
    }
    /**
     * 上抛
     */
    public void upThrow( ){

        ObjectAnimator objectAnimator=ObjectAnimator.ofFloat(shapeLoadingView,"translationY", mDistance,0);
        ObjectAnimator scaleIndication=ObjectAnimator.ofFloat(indicationIm,"scaleX",0.2f,1);


        ObjectAnimator objectAnimator1=null;
        switch (shapeLoadingView.getShape()){
            case SHAPE_RECT:


                objectAnimator1 = ObjectAnimator.ofFloat(shapeLoadingView, "rotation", 0, -120);

                break;
            case SHAPE_CIRCLE:
                objectAnimator1=   ObjectAnimator.ofFloat(shapeLoadingView,"rotation",0,180);

                break;
            case SHAPE_TRIANGLE:

                objectAnimator1 = ObjectAnimator.ofFloat(shapeLoadingView, "rotation", 0, 180);

                break;
        }


        objectAnimator.setDuration(ANIMATION_DURATION);
        objectAnimator1.setDuration(ANIMATION_DURATION);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator1.setInterpolator(new DecelerateInterpolator());
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.setDuration(ANIMATION_DURATION);
        animatorSet.playTogether(objectAnimator,objectAnimator1,scaleIndication);


        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                freeFall();


            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();


    }

    /**
     * 下落
     */
    public void freeFall(){

        ObjectAnimator objectAnimator=ObjectAnimator.ofFloat(shapeLoadingView,"translationY",0, mDistance);
        ObjectAnimator scaleIndication=ObjectAnimator.ofFloat(indicationIm,"scaleX",1,0.2f);


        objectAnimator.setDuration(ANIMATION_DURATION);
        objectAnimator.setInterpolator(new AccelerateInterpolator());
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.setDuration(ANIMATION_DURATION);
        animatorSet.playTogether(objectAnimator,scaleIndication);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {



                shapeLoadingView.changeShape();
                upThrow();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();



    }

}
