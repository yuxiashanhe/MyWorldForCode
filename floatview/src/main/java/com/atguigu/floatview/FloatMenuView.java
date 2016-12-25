package com.atguigu.floatview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2016/11/19.
 */
public class FloatMenuView extends LinearLayout {

    private LinearLayout ll;
    private TranslateAnimation animation;

    public FloatMenuView(Context context) {
        super(context);
        View root = View.inflate(getContext(),R.layout.float_menu_view,null);
        ll = (LinearLayout) root.findViewById(R.id.ll);
        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,1.0f,Animation.RELATIVE_TO_SELF,0);
        animation.setDuration(500);
        animation.setFillAfter(true);
        ll.setAnimation(animation);
        root.setOnTouchListener(onTouchListener);
        addView(root);
    }

    public FloatMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FloatMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 启动动画
     */
    public void startAnimation(){
        animation.start();
    }

    /**
     * 创建触摸监听
     */
    private MenuOnTouchListener onTouchListener = new MenuOnTouchListener();
    class  MenuOnTouchListener implements View.OnTouchListener{

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            FloatViewManger manger = FloatViewManger.getInstance(getContext());
            manger.hideFloatMenuView();//隐藏小球
            manger.showFloatCircleView();//显示底部menu
            return false;
        }
    }

}
