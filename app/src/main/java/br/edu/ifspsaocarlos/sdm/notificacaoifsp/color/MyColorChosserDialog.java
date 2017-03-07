package br.edu.ifspsaocarlos.sdm.notificacaoifsp.color;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifspsaocarlos.sdm.notificacaoifsp.R;


/**
 * Created by rapha on 3/4/2017.
 */

public class MyColorChosserDialog extends Dialog {

    private Button twentyOne;
    private List<Integer> colors;
    private List<ImageButton> buttons;

    private MyColorListener myColorListener;

    private int pixels;

    public MyColorChosserDialog(Context context, ArrayList<Integer> colors) {
        super(context);

        this.colors  = colors;
        buttons = new ArrayList<ImageButton>();
        final float scale = getContext().getResources().getDisplayMetrics().density;
        pixels = (int) (58 * scale + 0.5f);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(pixels, pixels);
        for (int i = 0; i < this.colors.size(); i++){
            ImageButton im = new ImageButton(context);
            im.setLayoutParams(lp);
            buttons.add(im);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_color_dialog);
        twentyOne =(Button)findViewById(R.id.b21);
        GridLayout gl = (GridLayout)findViewById(R.id.grid);
        for (int i = 0; i < buttons.size(); i++){
            gl.addView(buttons.get(i));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            Colorize();
        }else{
            ColorizeOld();
        }

        twentyOne.setVisibility(View.INVISIBLE);

        setListeners();
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(myColorListener != null)
                myColorListener.OnColorClick(v, (int)v.getTag());
            dismiss();
        }
    };

    private void setListeners() {
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setTag(colors.get(i));
            buttons.get(i).setOnClickListener(listener);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void Colorize() {
        for (int i = 0; i < buttons.size(); i++) {
            ShapeDrawable d = new ShapeDrawable(new OvalShape());
            d.setBounds(pixels, pixels, pixels, pixels);
            Log.e("Shape drown no", i + "");
            buttons.get(i).setVisibility(View.INVISIBLE);

            d.getPaint().setStyle(Paint.Style.FILL);
            d.getPaint().setColor(colors.get(i));

            buttons.get(i).setBackground(d);
        }
        animate();
    }

    private void ColorizeOld() {
        for (int i = 0; i < buttons.size(); i++) {
            ShapeDrawable d = new ShapeDrawable(new OvalShape());
            d.getPaint().setColor(colors.get(i));
            d.getPaint().setStrokeWidth(1f);
            d.setBounds(pixels, pixels, pixels, pixels);
            buttons.get(i).setVisibility(View.INVISIBLE);
            d.getPaint().setStyle(Paint.Style.FILL);
            d.getPaint().setColor(colors.get(i));
            //buttons.get(i).setBackgroundDrawable(d);
            buttons.get(i).setBackground(d);
        }
        animate();
    }

    private void animate(){
        Log.e("animate","true");

        android.os.Handler handler = new android.os.Handler();
        int counter = 85;

        ArrayList<Runnable> r = new ArrayList<Runnable>();
        for (int i = 0; i < buttons.size(); i++){
            final int finalI = i;
            Runnable r1 = new Runnable() {
                @Override
                public void run() {
                    Log.e("animator:" + finalI,"r");
                    animator(buttons.get(finalI));
                }
            };
            r.add(r1);
            handler.postDelayed(r1,counter * (i+1));
        }

        Runnable r9 = new Runnable() {
            @Override
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(getContext(),android.R.anim.fade_in);
                animation.setInterpolator(new AccelerateInterpolator());
                twentyOne.setAnimation(animation);
                twentyOne.setVisibility(View.VISIBLE);
                animation.start();
            }
        };
        r.add(r9);
        handler.postDelayed(r9,counter * 9);

        /*
        Runnable r1 = new Runnable() {
            @Override
            public void run() {
                Log.e("animator 1","r");
                animator(one);
            }
        };

        Runnable r2 = new Runnable() {
            @Override
            public void run() {
                animator(two);
                animator(six);
            }
        };

        Runnable r3 = new Runnable() {
            @Override
            public void run() {
                animator(three);
                animator(seven);
                animator(eleven);
            }
        };

        Runnable r4 = new Runnable() {
            @Override
            public void run() {
                animator(four);
                animator(eight);
                animator(twelve);
                animator(sixteen);
            }
        };

        Runnable r5 = new Runnable() {
            @Override
            public void run() {
                animator(five);
                animator(nine);
                animator(thirteen);
                animator(seventeen);
            }
        };

        Runnable r6 = new Runnable() {
            @Override
            public void run() {
                animator(ten);
                animator(fourteen);
                animator(eighteen);
            }
        };

        Runnable r7 = new Runnable() {
            @Override
            public void run() {
                animator(fifteen);
                animator(nineteen);
            }
        };

        Runnable r8 = new Runnable() {
            @Override
            public void run() {
                animator(twenty);
            }
        };

        Runnable r9 = new Runnable() {
            @Override
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(getContext(),android.R.anim.fade_in);
                animation.setInterpolator(new AccelerateInterpolator());
                twentyOne.setAnimation(animation);
                twentyOne.setVisibility(View.VISIBLE);
                animation.start();
            }
        };

        android.os.Handler handler = new android.os.Handler();
        int counter = 85;

        handler.postDelayed(r1,counter);
        handler.postDelayed(r2,counter * 2);
        handler.postDelayed(r3,counter * 3);
        handler.postDelayed(r4,counter * 4);
        handler.postDelayed(r5,counter * 5);
        handler.postDelayed(r6,counter * 6);
        handler.postDelayed(r7,counter * 7);
        handler.postDelayed(r8,counter * 8);
        handler.postDelayed(r9,counter * 9);
        */
    }

    private void animator(final ImageButton imageButton){
        Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.color_item);
        animation.setInterpolator(new AccelerateInterpolator());
        imageButton.setAnimation(animation);
        imageButton.setVisibility(View.VISIBLE);
        animation.start();
    }

    //CONSTANTS
    public final int red =        0xffF44336;
    public final int pink =       0xffE91E63;
    public final int Purple =     0xff9C27B0;
    public final int DeepPurple = 0xff673AB7;
    public final int Indigo =     0xff3F51B5;
    public final int Blue =       0xff2196F3;
    public final int LightBlue =  0xff03A9F4;
    public final int Cyan =       0xff00BCD4;
    public final int Teal =       0xff009688;
    public final int Green =      0xff4CAF50;
    public final int LightGreen = 0xff8BC34A;
    public final int Lime =       0xffCDDC39;
    public final int Yellow =     0xffFFEB3B;
    public final int Amber =      0xffFFC107;
    public final int Orange =     0xffFF9800;
    public final int DeepOrange = 0xffFF5722;
    public final int Brown =      0xff795548;
    public final int Grey =       0xff9E9E9E;
    public final int BlueGray =   0xff607D8B;
    public final int Black =      0xff000000;
    public final int White =      0xffffffff;


    public void setColorListener(MyColorListener listener){
        this.myColorListener = listener;
    }
}
