package com.yazhi1992.practice.streak_progress_bar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by zengyazhi on 17/2/13.
 * 条纹进度条
 */

public class StreakProgressbar extends SurfaceView implements SurfaceHolder.Callback{

    private Canvas canvas;
    // 条纹宽度
    private int hig = 35;
    private int a = 0;
    private SurfaceHolder surfaceHolder;
    private boolean isThreadRunning = true;
    private Thread Loadingthread = null;

    private void initview(Context context) {
        context = context;
        setZOrderOnTop(true);
        surfaceHolder = this.getHolder();
        surfaceHolder.setFormat(PixelFormat.TRANSPARENT);
        surfaceHolder.addCallback(this);
    }

    public StreakProgressbar(Context context) {
        super(context);
        initview(context);
    }

    public StreakProgressbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initview(context);
    }

    public StreakProgressbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initview(context);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    // 开启动画线程
    public void start2() {
        isThreadRunning = true;
        Loadingthread = new Thread() {
            @Override
            public void run() {
                while (isThreadRunning) {
                    try {
                        if (surfaceHolder != null) {
                            canvas = surfaceHolder.lockCanvas();
                            if (canvas != null) {
                                drawView();
                                surfaceHolder.unlockCanvasAndPost(canvas);
                            }
                            // 动画执行间隔，即滚动速度
                            Thread.sleep(100);
                            a++;
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        Log.d("InstallView", "InterruptedException");
                    }
                }
            }
        };
        Loadingthread.start();
    }

    // 绘制动画
    private void drawView() {
        final Paint paint_green = generatePaint(Color.parseColor("#F1C2C1"),
                Paint.Style.FILL, 1);
        final Paint paint_red = generatePaint(Color.parseColor("#ffffff"),
                Paint.Style.FILL, 1);
        canvas.clipRect(new RectF(0, 0, getWidth(), 100));
        int i;
        for (i = 0; i < getWidth() + 100; i = i + hig * 2) {
            // 画出第一个粉色梯形
            Path path = new Path();
            path.moveTo(i, 0);
            path.lineTo(i + hig, 0);
            path.lineTo(-60 + i, getHeight());
            path.lineTo(-(60 + hig) + i, getHeight());
            // 第二个白色梯形
            Path path2 = new Path();
            path2.moveTo(i + hig, 0);
            path2.lineTo(i + hig * 2, 0);
            path2.lineTo(-60 + i + hig, getHeight());
            path2.lineTo(-(60 + hig) + i + hig, getHeight());
            // 死循环来控制梯形交错显示
            if (a % 2 == 0) {
                canvas.drawPath(path, paint_green);
                canvas.drawPath(path2, paint_red);
            } else {
                canvas.drawPath(path, paint_red);
                canvas.drawPath(path2, paint_green);
            }
        }
    }

    private Paint generatePaint(int color, Paint.Style style, int width) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStyle(style);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(width);
        return paint;
    }
}
