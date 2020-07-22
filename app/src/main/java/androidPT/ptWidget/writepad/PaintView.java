package androidPT.ptWidget.writepad;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.MotionEvent;
import android.view.View;

public class PaintView extends View {
    private Paint mPaint;
    private Path mPath;
    private Bitmap mBitmap;
    private Canvas mCanvas;

    private int screenWidth, screenHeight;
    private float currentX, currentY;
    //是否画过
    private boolean flag = false;

    public PaintView(Context context, int screenWidth, int screenHeight) {
        super(context);
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true); // 去除锯齿
        mPaint.setStrokeWidth(5);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLACK);

        mPath = new Path();

        mBitmap = Bitmap.createBitmap(screenWidth, screenHeight, Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
//      mCanvas.drawColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mBitmap, 0, 0, null);
        canvas.drawPath(mPath, mPaint);
    }

    public void setMath(int width,int height){
        this.screenWidth = width;
        this.screenHeight = height;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                currentX = x;
                currentY = y;
                mPath.moveTo(currentX, currentY);
                break;
            case MotionEvent.ACTION_MOVE:
                currentX = x;
                currentY = y;
                mPath.quadTo(currentX, currentY, x, y); // 画线
                break;
            case MotionEvent.ACTION_UP:
                mCanvas.drawPath(mPath, mPaint);
                flag = true;
                break;
        }

        invalidate();
        return true;
    }

    public Bitmap getPaintBitmap() {
        return resizeImage(mBitmap, screenWidth, screenHeight);
    }

    public Path getPath() {
        return mPath;
    }

    // 缩放
    public static Bitmap resizeImage(Bitmap bitmap, int width, int height) {
        int originWidth = bitmap.getWidth();
        int originHeight = bitmap.getHeight();

        float scaleWidth = ((float) width) / originWidth;
        float scaleHeight = ((float) height) / originHeight;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, originWidth,
                originHeight, matrix, true);
        return resizedBitmap;
    }

    //清除画板
    public void clear() {
        if (mCanvas != null) {
            mPath.reset();
            mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            mBitmap = Bitmap.createBitmap(screenWidth, screenHeight, Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
            invalidate();
        }
    }

    //加载图片
    public void setBitmap(Bitmap bitmap, int screenWidth, int screenHeight){
        mBitmap = resizeImage(bitmap,screenWidth,screenHeight);
        mCanvas = new Canvas(mBitmap);
        mCanvas.drawBitmap(mBitmap, 0, 0, null);
        mCanvas.drawPath(mPath, mPaint);
        invalidate();
    }


    public boolean isDraw(){
        return flag;
    }
}