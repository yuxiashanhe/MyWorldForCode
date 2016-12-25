package com.atguigu.floatview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/11/19.
 */
public class MyProgressView extends View {
    private int width = 300;
    private int height = 300;
    private Paint criclePaint;
    private Paint progressPaint;
    private Paint textPaint;
    private Bitmap bitmap;
    private Canvas bitmapCanvas;
    private Path path = new Path();
    private int progress = 50;
    private int maxProgress = 100;
    private GestureDetector detector;
    private int currentProgress = 0;
    private int count = 50;
    private boolean isSingTap = false;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
//            handler.postDelayed(doubleTapRunable,50);
        }
    };

    public MyProgressView(Context context) {
        super(context);
        initPaints();//初始化画笔
    }

    public MyProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaints();
    }

    public MyProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaints();
    }

    private void initPaints() {
        criclePaint = new Paint();
        criclePaint.setAntiAlias(true);
        criclePaint.setColor(Color.argb(0xff, 0x3a, 0x8c, 0x6c));

        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setColor(Color.argb(0xff, 0x4e, 0xc9, 0x63));
        progressPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));//设置模型，只绘制重叠的部分

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(30);

        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmapCanvas = new Canvas(bitmap);//创建一个画布，在图片上画圆

        //设置手势识别类，设置手势识别监听
        detector = new GestureDetector(new MyGestureDetectorListener());
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return detector.onTouchEvent(event);//将触摸监听交给手势识别对象
            }
        });
        setClickable(true);//设置此控件可以被点击

    }

    class MyGestureDetectorListener extends GestureDetector.SimpleOnGestureListener {
        //双击是调用此方法
        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            Toast.makeText(getContext(),"双击控件",Toast.LENGTH_SHORT).show();
            currentProgress = 0;
            startDoubleAnimation();
            return super.onDoubleTapEvent(e);
        }

        //单击时调用此方法
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Toast.makeText(getContext(),"单击控件",Toast.LENGTH_SHORT).show();
            isSingTap = true;
            startSingTapAnimation();
            return super.onSingleTapConfirmed(e);
        }
    }

    //创建单击动画
    private void startSingTapAnimation() {
        handler.postDelayed(singTapRunable,200);
    }

    private SingTapRunable singTapRunable = new SingTapRunable();
    class  SingTapRunable implements Runnable{

        @Override
        public void run() {
            count--;
            if(count >= 0) {
                invalidate();//刷新当前控件
                handler.postDelayed(singTapRunable,200);
            }else {
                handler.removeCallbacks(singTapRunable);
                count = 50;
            }
        }
    }

    //创建双击动画
    private void startDoubleAnimation() {
        handler.postDelayed(doubleTapRunable,50);
    }

    /**
     * 创建一个线程，发送当前进度
     */
    private DoubleTapRunable doubleTapRunable = new DoubleTapRunable();
    class  DoubleTapRunable implements Runnable{

        @Override
        public void run() {
            currentProgress++;
            if(currentProgress <= progress) {
                invalidate();//刷新view
                handler.postDelayed(doubleTapRunable,50);
            }else {
                handler.removeCallbacks(doubleTapRunable);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //画圆
        bitmapCanvas.drawCircle(width/2, height/2, width/2, criclePaint);
        path.reset();//重置path
        float y = (1 - (float)currentProgress/maxProgress) * height;//计算进度到y轴的坐标
        path.moveTo(width, y);
        path.lineTo(width, height);
        path.lineTo(0, height);
        path.lineTo(0,y);

        //做单双击判断，如果是单击，波浪线上下变化，并逐渐停止
        if(isSingTap) {
            Log.i("TAG","单击");
            float d = (float)count/50*10;
            if(count%2 == 0) {
                for (int i = 0;i < 8;i++){
                    path.rQuadTo(20,-d,40,0);
                    path.rQuadTo(20,d,40,0);
                }
            }else {
                for (int i = 0;i < 8;i++) {
                    path.rQuadTo(20, d, 40, 0);
                    path.rQuadTo(20, -d, 40, 0);
                }
            }

        }else {

            float d = (1 - (float)currentProgress/maxProgress)*10;
            for (int i = 0;i < 8;i++){
                path.rQuadTo(10,-d,20,0);//下波浪
                path.rQuadTo(10,d,20,0);//上波浪
            }
        }
        path.close();
        bitmapCanvas.drawPath(path,progressPaint);//画波浪进度

        //画文本进度
//        String text = (int)((float)currentProgress/maxProgress)*100 + "%";
        String text = currentProgress + "%";
        Log.i("TAG","文本"+text+" 的进度"+(int)currentProgress);
        float textWidth = textPaint.measureText(text);
        FontMetrics  metrics = textPaint.getFontMetrics();
        float baseLine = height / 2 - (metrics.ascent + metrics.descent) / 2;
        bitmapCanvas.drawText(text, width / 2 - textWidth / 2, baseLine, textPaint);

        canvas.drawBitmap(bitmap, 0, 0, null);//将整个，画在屏幕的画布上
    }
}
