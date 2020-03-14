package tw.org.iii.ellen.ellen07;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

public class MyView extends View {

    private Bitmap ballBmp, bgBmp ;
    private MainActivity activity ;
    private Resources resources ;
    private Paint paint ;
    private int viewW, viewH ;
    private float ballW, ballH, ballX, ballY, dx, dy ;
    private boolean isInit ;
    private Timer timer ;
    private GestureDetector gd ;

    public MyView(Context context) {
        super(context);
        //setBackgroundColor(Color.CYAN) ;
        setBackgroundResource(R.drawable.bg2);
        activity = (MainActivity)context ;
        resources = activity.getResources() ;

        timer = new Timer() ;
        gd = new GestureDetector(new MyGDListener()) ;

        Log.v("ellen",(context instanceof MainActivity) + "") ;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.v("ellen","onTouch") ;
        return gd.onTouchEvent(event);
    }

    private class MyGDListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            Log.v("ellen","onFling") ;

            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public boolean onDown(MotionEvent e) {

            Log.v("ellen","onDown") ;

            return true; //super.onDown(e);
        }
    }

    private void init() {
        isInit = true;

        paint = new Paint();
        paint.setAlpha(127);
        ballBmp = BitmapFactory.decodeResource(resources, R.drawable.volleyball);
        //bgBmp = BitmapFactory.decodeResource(resources,R.drawable.bg) ;

        viewH = getHeight();
        viewW = getWidth();

        ballW = viewW / 6f;
        ballH = ballW;

        Matrix matrix = new Matrix();  //負責影像轉換
        matrix.postScale(ballW / ballBmp.getWidth(), ballH / ballBmp.getHeight());
        ballBmp = Bitmap.createBitmap(ballBmp, 0, 0, ballBmp.getWidth(), ballBmp.getHeight(), matrix, false);
        //matrix.reset() ; 可以reset之後再重設
        Log.v("ellen", "(" + viewW + "," + viewH + ")");

        ballX = ballY = 100 ;
        dx = dy = 15 ;
        timer.schedule(new RefreshView(),0,17) ;
        timer.schedule(new BallTask(), 1*1000,30);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas) ;
        if (! isInit)   init() ;

        canvas.drawBitmap(ballBmp,ballX,ballY,paint) ;
        //canvas.drawBitmap(bgBmp,0,0,null) ;

    }

    private class RefreshView extends TimerTask{
        @Override
        public void run() {
            postInvalidate() ;
        }
    }

    private class BallTask extends TimerTask{
        @Override
        public void run() {
            if (ballX < 0 || ballX+ballW > viewW)
                dx *= -1 ;
            if (ballY < 0 || ballY+ballH > viewH)
                dy *= -1 ;
            ballX += dx ;
            ballY += dy ;
            //postInvalidate() ;  //做完這件事之後再更新畫面

        }
    }
}
