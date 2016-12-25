package com.atguigu.floatview;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Toast;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2016/11/17.
 *
 * 小浮球的管理类
 *
 */
public class FloatViewManger {
    private Context context;
    //通过WindowManager来控制浮窗体的显示、隐藏和位置的改变
    private final WindowManager wm;
    private FloatCircleView circleView;
    private LayoutParams params;
    private View.OnTouchListener circleViewTouchListener = new View.OnTouchListener() {

        private float y0;
        private float x0;
        private float startY;
        private float startX;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    startX = event.getRawX();
                    x0 = event.getRawX();
                    y0 = event.getRawY();
                    startY = event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float x = event.getRawX();
                    float y = event.getRawY();
                    float dx = x - startX;
                    float dy = y - startY;
                    params.x += dx;
                    params.y += dy;
                    circleView.setDragState(true);//true设置的是拖中状态,显示不同的效果
                    wm.updateViewLayout(circleView,params);
                    startX = x;
                    startY = y;
                    break;
                case MotionEvent.ACTION_UP:
                    float x1 = event.getRawX();
                    if(x1 > getScreenWidth()/2) {
                        params.x = getScreenWidth() - circleView.width;//如果控件x轴坐标大于屏幕一般就让其靠于右边
                    }else{
                        params.x = 0;
                    }
                    circleView.setDragState(false);
                    wm.updateViewLayout(circleView,params);
                    
                    if(Math.abs(x1 - x0) > 6) {
                        return true;//如果判断滑动距离大于6，判断为滑动操作，将事件消费掉，不在传递
                    }else {
                        return false;
                    }
                default:
                    break;
            }
            return false;
        }
    };
    private final FloatMenuView floatMenuView;

    /**
     * 获取当前屏幕的宽
     * @return
     */
    public int getScreenWidth(){
        return wm.getDefaultDisplay().getWidth();//用wm获取当前窗体屏幕的宽
    }

    /**
     * 获取屏幕的高
     */
    public int getScreenHeight(){
        return wm.getDefaultDisplay().getHeight();
    }

    /**
     * ，通过反射的方式，获取状态栏的高
     * @return
     */
    public int getStatusHeight(){
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object o = c.getInterfaces();
            Field field = c.getField("status_bar_height");
            int x = (Integer)field.get(o);
            int height = context.getResources().getDimensionPixelSize(x);
            return height;
        } catch (Exception e) {
            return 0;
        }

    }

    /**
     * 小球的点击监听事件
     */
    private OnClickListener  circleViewClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(context,"小球被点击",Toast.LENGTH_SHORT).show();
            //隐藏circleView, 显示菜单栏，开启动画
            wm.removeView(circleView);
            showFloatMenuView();
            floatMenuView.startAnimation();
        }

    };

    /**
     * 显示底部menu菜单
     */
    private void showFloatMenuView() {
        LayoutParams params = new LayoutParams();
        params.width = getScreenWidth();
        params.height = getScreenHeight() - getStatusHeight();
        params.gravity = Gravity.BOTTOM|Gravity.LEFT;//设置控件的相对位置，在顶部和左部对齐
        params.x = 0;//x轴偏移量为0
        params.y = 0;//y轴偏移量为0
        params.type = LayoutParams.TYPE_TOAST;//LayoutParams.TYPE_PHONE设置控件的类型，向电话一样在其他应用之上
        params.flags = LayoutParams.FLAG_NOT_FOCUSABLE|LayoutParams.FLAG_NOT_TOUCH_MODAL;//设置1不与其他应用墙焦点|触摸获取焦点
        params.format = PixelFormat.RGBA_8888;//设置背景像素为透明的
        wm.addView(floatMenuView, params);

    }

    /**
     * 初始化
     * @param context
     */
    private FloatViewManger(Context context) {
        this.context = context;
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        circleView = new FloatCircleView(context);//初始化小球控件
        circleView.setOnTouchListener(circleViewTouchListener);//设置小球的触摸监听
        circleView.setOnClickListener(circleViewClickListener);
        floatMenuView = new FloatMenuView(context);
    }

    private static FloatViewManger instance;

    //懒汉式单例模式，防止线程并发的方式
    public static FloatViewManger getInstance(Context context) {
        if (instance == null) {
            synchronized (FloatViewManger.class) {
                if (instance == null) {
                    instance = new FloatViewManger(context);
                }
            }
        }
        return instance;
    }

    /**
     * 添加小球布局
     */
    public void showFloatCircleView(){
        if(params == null) {
            params = new LayoutParams();
            params.width = circleView.width;
            params.height = circleView.height;
            params.gravity = Gravity.TOP|Gravity.LEFT;//设置控件的相对位置，在顶部和左部对齐
            params.x = 0;//x轴偏移量为0
            params.y = 0;//y轴偏移量为0
            params.type = LayoutParams.TYPE_TOAST;//LayoutParams.TYPE_PHONE设置控件的类型，向电话一样在其他应用之上
            params.flags = LayoutParams.FLAG_NOT_FOCUSABLE|LayoutParams.FLAG_NOT_TOUCH_MODAL;//设置1不与其他应用墙焦点|触摸获取焦点
            params.format = PixelFormat.RGBA_8888;//设置背景像素为透明的
        }

        wm.addView(circleView, params);
    }

    //设置隐藏小球
    public void hideFloatMenuView() {
        wm.removeView(floatMenuView);
    }
}
