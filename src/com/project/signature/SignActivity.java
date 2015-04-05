package com.project.signature;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import com.project.signature.utils.Globals;
import com.project.signture.entities.Signature;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class SignActivity extends Activity {

	DrawingView dv ;   
	private Paint mPaint;
	//private DrawingManager mDrawingManager=null;
	Button verify;
	LinearLayout base;
	ProgressDialog barProgressDialog;
	Handler updateBarHandler;
	boolean stop;
	private float size_counter;
    private int up_down_counter;
    private int time_counter = 0;
    //List<Float> pressure_values = new ArrayList<Float>();
    private float pressure_values;
    List<Long> time_start = new ArrayList<Long>();
    List<Long> time_end = new ArrayList<Long>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    base = new LinearLayout(this);
	    base.setOrientation(LinearLayout.VERTICAL);
	    
	    //Verify Button
	    verify = new Button(this);
	    verify.setText("Verify");
	    verify.setGravity(Gravity.CENTER | Gravity.BOTTOM);
	    if(!Globals.sign_screen){
	    	verify.setText("Store Trial "+Globals.attempt);
	    	Globals.attempt++;
	    }
	    verify.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				
				for(int i=0;i<time_start.size();i++)
					time_counter+=(time_end.get(i)-time_start.get(i));
				
				if(!Globals.sign_screen){
					DisplayMetrics dm = new DisplayMetrics();
					size_counter = (size_counter/dm.xdpi)*25.4f;
					Signature signature = new Signature(pressure_values,size_counter, up_down_counter, time_counter, Globals.user);
					signature.save();
				}else{
					//Verify
				}
				// TODO Auto-generated method stub
				launchBarDialog(v);
			}
	    });
	    
	    dv = new DrawingView(this);
	    dv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 600));
	    base.addView(dv);
	    base.addView(verify);
	    setContentView(base);
	    mPaint = new Paint();
	    mPaint.setAntiAlias(true);
	    mPaint.setDither(true);
	    mPaint.setColor(Color.BLACK);
	    mPaint.setStyle(Paint.Style.STROKE);
	    mPaint.setStrokeJoin(Paint.Join.ROUND);
	    mPaint.setStrokeCap(Paint.Cap.ROUND);
	    mPaint.setStrokeWidth(3);  
	    
	    updateBarHandler = new Handler();
	}
	
	private void writeToFile(int data, String fileName) {
	    try {
	    	File myFile = new File("/sdcard/"+fileName);
			myFile.createNewFile();
			FileOutputStream fOut = new FileOutputStream(myFile);
	        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fOut);
	        outputStreamWriter.write(""+data);
	        outputStreamWriter.close();
	    }
	    catch (IOException e) {
	        Log.e("Exception", "File write failed: " + e.toString());
	    } 
	}
	
	private void writeToFile(Float data, String fileName) {
	    try {
	    	File myFile = new File("/sdcard/"+fileName);
			myFile.createNewFile();
			FileOutputStream fOut = new FileOutputStream(myFile);
	        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fOut);
	        //for(int i=0; i<data.size();i++)
	        //	outputStreamWriter.write(data.get(i).toString()+"\n");
	        outputStreamWriter.write(""+data);
	        outputStreamWriter.close();
	    }
	    catch (IOException e) {
	        Log.e("Exception", "File write failed: " + e.toString());
	    } 
	}
	
	public void launchBarDialog(View view) {
				stop = false;
		        barProgressDialog = new ProgressDialog(this);
		        if(Globals.sign_screen){
		        	barProgressDialog.setTitle("Verifying");
		        	barProgressDialog.setMessage("Signature verification in progress ...");
		        }else{
		        	barProgressDialog.setTitle("Storing");
		        	barProgressDialog.setMessage("Signature storing in progress ...");
		        }
		        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_HORIZONTAL);
		        barProgressDialog.setProgress(0);
		        barProgressDialog.setMax(20);
		        barProgressDialog.show();
		        new Thread(new Runnable() {
		            @Override
		            public void run() {
		                try {
		                    while (barProgressDialog.getProgress() <= barProgressDialog.getMax() && stop == false) {
		                        Thread.sleep(1000);
		                        updateBarHandler.post(new Runnable() {
		                            public void run() {
		                                barProgressDialog.incrementProgressBy(2);
		                              }
		                          });
		                        if (barProgressDialog.getProgress() == barProgressDialog.getMax()) {
		                            barProgressDialog.dismiss();
		                            //updateBarHandler.removeCallbacks(r)
		                            stop = true;
		                            Intent list;
		                            if(Globals.attempt==4)
		                            	Globals.sign_screen = true;
		                            if(Globals.sign_screen){
		                            	list = new Intent(SignActivity.this, documentActivity.class);
		                            }else{
		                            	list = new Intent(SignActivity.this, SignActivity.class);
		                            }
		                            startActivity(list);
		            				
		                        }
		                    }
            				
		                } catch (Exception e) {
		                }
		            }
		        }).start();
		    }


	 public class DrawingView extends View {

	        public int width;
	        public  int height;
	        private Bitmap  mBitmap;
	        private Canvas  mCanvas;
	        private Path    mPath;
	        private Paint   mBitmapPaint;
	        Context context;
	        private Paint circlePaint;
	        private Path circlePath;

	        private float mX, mY;
	        private static final float TOUCH_TOLERANCE = 4;
	        
	        public DrawingView(Context c) {
		        super(c);
		        context=c;
		        mPath = new Path();
		        mBitmapPaint = new Paint(Paint.DITHER_FLAG);  
		        circlePaint = new Paint();
		        circlePath = new Path();
		        circlePaint.setAntiAlias(true);
		        circlePaint.setColor(Color.BLUE);
		        circlePaint.setStyle(Paint.Style.STROKE);
		        circlePaint.setStrokeJoin(Paint.Join.MITER);
		        circlePaint.setStrokeWidth(4f);
		        
		        size_counter = 0;
		        up_down_counter = 0;
	        }

	        @Override
	        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		        super.onSizeChanged(w, h, oldw, oldh);
	
		        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		        mCanvas = new Canvas(mBitmap);
	        }
	        
	        @Override
	        protected void onDraw(Canvas canvas) {
		        super.onDraw(canvas);
	
		        canvas.drawBitmap( mBitmap, 0, 0, mBitmapPaint);
	
		        canvas.drawPath( mPath,  mPaint);
	
		        canvas.drawPath( circlePath,  circlePaint);
	        }

	        private void touch_start(float x, float y) {
	        	time_start.add(SystemClock.uptimeMillis());
	        	up_down_counter++;
	        	
		        mPath.reset();
		        mPath.moveTo(x, y);
		        mX = x;
		        mY = y;
	        }
	        
	        private void touch_move(float x, float y) {
		        float dx = Math.abs(x - mX);
		        float dy = Math.abs(y - mY);
		        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
		             mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
		            mX = x;
		            mY = y;
	
		            circlePath.reset();
		            circlePath.addCircle(mX, mY, 30, Path.Direction.CW);
		        }
	        }
	        
	        private void touch_up() {
	        	time_end.add(SystemClock.uptimeMillis());
		        mPath.lineTo(mX, mY);
		        circlePath.reset();
		        // commit the path to our offscreen
		        mCanvas.drawPath(mPath,  mPaint);
		        // kill this so we don't double draw
		        mPath.reset();
		        
//		        runOnUiThread(new Runnable() {
//		        	public void run() {
//		        		Toast.makeText(context, up_down_counter, Toast.LENGTH_LONG).show();
//		        	}
//		        });
	        }

	        @Override
	        public boolean onTouchEvent(MotionEvent event) {
		        float x = event.getX();
		        float y = event.getY();
		        //pressure_values.add(event.getPressure(0));
		        pressure_values+=event.getPressure(0);
		        size_counter++;
		        switch (event.getAction()) {
		            case MotionEvent.ACTION_DOWN:
		                touch_start(x, y);
		                invalidate();
		                break;
		            case MotionEvent.ACTION_MOVE:
		                touch_move(x, y);
		                invalidate();
		                break;
		            case MotionEvent.ACTION_UP:
		                touch_up();
		                invalidate();
		                break;
		        }
		        return true;
		        }  
	        }
}