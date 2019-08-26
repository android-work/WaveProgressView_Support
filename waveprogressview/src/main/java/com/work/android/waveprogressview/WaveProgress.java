package com.work.android.waveprogressview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


public class WaveProgress extends View {

    private Paint paint;
    private Bitmap bitmap;
    private Path path;
    private int mWaveHeight = 20;
    private int offset;
    private int baseLine;
    private float progress;
    private Paint wavePaint;
    private Paint textPaint;
    private int mWidth;
    private int mHeight;
    private int mWaveWidth;
    private int count = 1;
    private ValueAnimator valueAnimator;
    private long waveSpeed = 100;
    private boolean tag = false;

    public WaveProgress(Context context) {
        this(context,null,0);
    }

    public WaveProgress(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public WaveProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
    }

    public void setWaveSpeed(long speed){
        waveSpeed = speed;
    }

    public void setWaveColor(int color){
        wavePaint.setColor(color);
        invalidate();
    }

    public void setTextColor(int color){
        textPaint.setColor(color);
        invalidate();
    }

    public void setProgress(float progress){
        this.baseLine = (int) ((1 - progress) * mHeight);
        this.progress = progress;
        invalidate();
    }

    public void setWaveHeight(int height){
        mWaveHeight = height;
    }

    public void setWaveCount(int count){
        mWaveWidth = mWidth / 4 / this.count;
        this.count = count;
        invalidate();
    }

    public void setTextSize(float textSize){
        textPaint.setTextSize(textSize);
        invalidate();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            offset += 3;
            if (offset > mWidth * 1 ){
                offset = 0;
            }
            invalidate();

            if (!tag) {
                this.sendEmptyMessageDelayed(0, waveSpeed);
            }
        }
    };

    private void initView() {

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        wavePaint = new Paint();
        wavePaint.setAntiAlias(true);
        wavePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        wavePaint.setColor(getResources().getColor(R.color.color1));

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setColor(Color.RED);
        textPaint.setTextSize(50);

        path = new Path();

    }

    public void setBG(int background){

        setBackground(getResources().getDrawable(background));
    }

    public Bitmap getBitmaps() throws Exception{
        Bitmap bitmap;
        Drawable background = getBackground();
        if (background == null){
            throw new Exception("you need set background");
        }else {
            if (background instanceof BitmapDrawable) {
                bitmap = ((BitmapDrawable) background).getBitmap();
            } else {
                int intrinsicWidth = background.getIntrinsicWidth();
                int intrinsicHeight = background.getIntrinsicHeight();
                bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                background.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                Log.e("tag", canvas.getWidth() + "::" + canvas.getHeight());
                background.draw(canvas);
            }
            bitmap = Bitmap.createScaledBitmap(bitmap, mWidth, mHeight, false);
        }
        return bitmap;
    }

    public void startAnimation(){

        tag = false;
        handler.sendEmptyMessage(0);
    }

    public void stopAnimator(){

        tag = true;
        handler.removeCallbacks(null);
        offset = 0;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = getSize(200, widthMeasureSpec);
        mHeight = getSize(200, heightMeasureSpec);

        setMeasuredDimension(mWidth, mHeight);

        setWaveCount(count);

        this.baseLine = (int) ((1 - progress) * mHeight);
        mWaveWidth = mWidth / 4 / count;

        try {
            bitmap = getBitmaps();
        } catch (Exception e) {
            e.printStackTrace();
        }

        startAnimation();
    }

    private int getSize(int defaultSize,int measureSpace){

        int mode = MeasureSpec.getMode(measureSpace);
        int measureSize = MeasureSpec.getSize(measureSpace);

        int size = defaultSize;
        switch (mode){
            //没有限制模式
            case MeasureSpec.UNSPECIFIED:
                size = defaultSize;
                break;
            //最大模式：wrap_content
            case MeasureSpec.AT_MOST:
                size = measureSize;
                break;
            //精确模式:match_parent、具体大小
            case MeasureSpec.EXACTLY:
                size = measureSize;
                break;
        }

        return size;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (bitmap == null){
            return;
        }

        Bitmap bitmap = creageCanvas();

        canvas.drawBitmap(bitmap,0,0,null);

    }

    private Bitmap creageCanvas() {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        this.path.reset();
        this.path.moveTo(-mWaveWidth * 5 ,baseLine);
        int nextFlagPoint = 0;
        for (int i = 1; i < Math.round(mWidth / mWaveWidth) + 1; i ++){
            this.path.quadTo((nextFlagPoint+1) * mWaveWidth - mWaveWidth - offset ,baseLine+mWaveHeight ,(nextFlagPoint+2) * mWaveWidth - mWaveWidth - offset,baseLine);
            this.path.quadTo((nextFlagPoint+3) * mWaveWidth - mWaveWidth - offset,baseLine-mWaveHeight,(nextFlagPoint+4) * mWaveWidth - mWaveWidth - offset,baseLine);

            nextFlagPoint += 4;
        }
        if (offset >= mWidth){
            offset = -mWaveWidth;
        }
        this.path.lineTo(mWidth , getHeight());
        this.path.lineTo(0,getHeight());
        this.path.close();

        canvas.drawPath(this.path,wavePaint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));

        canvas.drawBitmap(this.bitmap,0,0,paint);
        float v = textPaint.measureText(currentProgress(baseLine));
        float v1 = (getWidth() - v) / 2;
        canvas.drawText(currentProgress(baseLine),v1,getHeight()/2,textPaint);

        return bitmap;
    }

    private String currentProgress(float progress){
        float curPro = (mHeight-progress) / mHeight;
        return  ((int)(curPro * 100)) +"%";
    }

}
