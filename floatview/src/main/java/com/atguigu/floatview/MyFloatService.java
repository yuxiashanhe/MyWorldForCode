package com.atguigu.floatview;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Administrator on 2016/11/18.
 *
 * 用服务来开启显示小浮球，防止在activity调用finsh()方法是被销毁
 *
 */
public class MyFloatService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        //展示小球
        FloatViewManger manger = FloatViewManger.getInstance(this);
        manger.showFloatCircleView();
        super.onCreate();
    }
}
