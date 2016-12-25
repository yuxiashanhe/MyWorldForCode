package com.atguigu.floatview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Administrator on 2016/11/17.
 *
 * 创建：自定义悬浮小球的控件
 */
public class FloatCircleView extends View {
    public int width = 150;
    public int height = 150;
    private Paint circlePaint;
    private Paint textPaint;
    private String text = "50%";
    private boolean drag = false;
    private Bitmap bitmap;

    public FloatCircleView(Context context) {
        super(context);
        initPaints();//为什么只能在这里进行画笔的初始化工作，在另外两个构造中无法进行
    }

    public FloatCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FloatCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initPaints() {
        circlePaint = new Paint();//创建一个画笔，用于画圆
        circlePaint.setColor(Color.GRAY);
        circlePaint.setAntiAlias(true);//抗锯齿效果
        Log.i("TAG", "初始化画笔circlePaint--------");

        textPaint = new Paint();
        textPaint.setColor(Color.RED);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(25);
        textPaint.setFakeBoldText(true);//设置字体加粗
        Log.i("TAG", "初始化画笔textPaint--------");


        Bitmap src = BitmapFactory.decodeResource(getResources(), R.drawable.main_community);
        bitmap = Bitmap.createScaledBitmap(src, width, height, true);//对图片进行尺寸压缩

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(drag) {
            canvas.drawBitmap(bitmap,0,0,null);//滑动时改变图标，效果？？
        }else{
            //画圆
            canvas.drawCircle(width/2,height/2,width/2,circlePaint);

            //画文本
            float textWidth = textPaint.measureText(text);
            float x = width/2 - textWidth/2;//x轴坐标为文本左边界线的位置
            FontMetrics metrics = textPaint.getFontMetrics();//获取文字规格
            float dy = (metrics.ascent + metrics.descent)/2;
            float y = height/2 - dy;//y轴为文本基线的位置（baseLine）
            canvas.drawText(text, x, y, textPaint);
        }

    }

    /**
     * 设置是否处于滑动状态
     * @param b
     */
    public void setDragState(boolean b) {
        drag = b;
        initPaints();
    }
}
