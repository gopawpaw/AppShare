/**
 * 文件名: BluetoothScanerView.java
 * 版    权：  Copyright  LiJinHua  All Rights Reserved.
 * 描    述: 
 * 创建人: LiJinHua
 * 创建时间:  2014-11-9
 * 
 * 修改人：LiJinHua
 * 修改时间:2014-11-9 下午6:25:46
 * 修改内容：[修改内容]
 */
package im.device.appshare.widget;

import im.device.appshare.R;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import com.gopawpaw.droidcore.log.AppLog;

/**
 * 蓝牙扫瞄view
 * @author LiJinHua
 * @modify 2014-11-9 下午6:25:46
 */
public class BluetoothScanerView extends View {
	private final static String TAG = BluetoothScanerView.class.getSimpleName();
	private final static int HANDLER_WAHT_UPDATE = 1;
	private Bitmap scanerIconA;
	private Bitmap scanerIconB;
	// 圆环半径
	private float scanerCircleRadius;

	// 圆心x坐标
	private int mXCenter;
		// 圆心y坐标
	private int mYCenter;
		
	private ArrayList<Point> mListPoint = new ArrayList<BluetoothScanerView.Point>();
	
	private Point mCurrentPoint;
	
	private boolean mIsScner = false;
	
	private Handler mHandler = null;
	
	/**
	 * @param context
	 */
	public BluetoothScanerView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initVariable();
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public BluetoothScanerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// 获取自定义的属性
		initAttrs(context, attrs);
		initVariable();
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public BluetoothScanerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initAttrs(context, attrs);
		initVariable();
	}

	private void initAttrs(Context context, AttributeSet attrs) {
		TypedArray typeArray = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.BluetoothScanerView, 0, 0);

		scanerCircleRadius = typeArray.getDimension(
				R.styleable.BluetoothScanerView_scanerCircleRadius, 60);

		 int iconA = typeArray.getResourceId(R.styleable.BluetoothScanerView_scanerIconA,0);
		 int iconB = typeArray.getResourceId(R.styleable.BluetoothScanerView_scanerIconB,0);
		 if(iconA != 0){
			 scanerIconA=BitmapFactory.decodeResource(context.getResources(), iconA);
		 }
		 if(iconB != 0){
			 scanerIconB=BitmapFactory.decodeResource(context.getResources(), iconB);
		 }
	}

	private void initVariable() {
		
		mPaint.setColor(Color.RED);
		mPaint.setStyle(Paint.Style.FILL);
		
//		mXCenter = getWidth() / 2;
//		mYCenter = getHeight() / 2;
//		if(mXCenter>0){
//			for (int i = 0; i < 360; i++) {
//				mListPoint.add(getCirclePoint(mXCenter,mYCenter,scanerCircleRadius,i));
//			}
//		}
		
//		startScner();
		
		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				AppLog.d(TAG, "handleMessage msg.what:"+msg.what);
				switch (msg.what) {
				case HANDLER_WAHT_UPDATE:
					this.removeMessages(HANDLER_WAHT_UPDATE);
					if(mIsScner){
						if(mListPoint.size()<=0){
							Message m = new Message();
							m.what = HANDLER_WAHT_UPDATE;
							m.arg1 = 0;
							mHandler.sendMessageDelayed(m, 3000);
							return;
						}
						
						int index = msg.arg1;
						Point p = mListPoint.get(index);
						if(p == null){
							return;
						}
						mCurrentPoint = p;
						AppLog.d(TAG, "HANDLER_WAHT_UPDATE x:"+mCurrentPoint.x+" y:"+mCurrentPoint.y);
						postInvalidate();
						++index;
						if(index>=mListPoint.size()){
							index = 0;
						}
						Message m = new Message();
						m.what = HANDLER_WAHT_UPDATE;
						m.arg1 = index;
						mHandler.sendMessageDelayed(m, 50);
					}
					break;
				default:
					break;
				}
			}
		};
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		AppLog.i(TAG, "onDraw mXCenter : "+mXCenter+"===================");
		if(mXCenter<=0){
			mXCenter = getWidth() / 2;
			mYCenter = getHeight() / 2;
			for (int i = 3; i < 360; i=i+3) {
				Point p = getCirclePoint(mXCenter,mYCenter,scanerCircleRadius,i);
				p.x = p.x-(scanerIconA.getWidth()/2);
				p.y = p.y-(scanerIconB.getHeight()/2);
				if((i>10 && i<30) || (i>100 && i<130) || (i>200 && i<150) ){
					p.blutoothShow = true;
				}
				mListPoint.add(p);
			}
			AppLog.i(TAG, "onDraw mListPoint size: "+mListPoint.size()+"===================");
		}
		
		if(mCurrentPoint != null){
			AppLog.d(TAG, "onDraw x:"+mCurrentPoint.x+" y:"+mCurrentPoint.y+" scanerIcon:"+scanerIconA);
//			if(mCurrentPoint.blutoothShow){
//				if(scanerIconA != null){
//					canvas.drawBitmap(scanerIconA, mCurrentPoint.x, mCurrentPoint.y, mPaint);
//				}
//			}else{
				if(scanerIconB != null){
					canvas.drawBitmap(scanerIconB, mCurrentPoint.x, mCurrentPoint.y, mPaint);
				}
//			}
		}
	}
	
	Paint mPaint = new Paint();

	private Point getCirclePoint(int centerX, int centerY, float radius,
			float ao) {
		// 圆点坐标：(x0,y0)
		// 半径：r
		// 角度：a0
		// 则圆上任一点为：（x1,y1）
		// x1 = x0 + r * cos(ao * 3.14 /180 )
		// y1 = y0 + r * sin(ao * 3.14 /180 )
		Point p = new Point();
		p.x = (float) (centerX + radius * Math.cos(ao * 3.14d / 180d));
		p.y = (float) (centerY + radius * Math.sin(ao * 3.14d / 180d));
		return p;
	}

	private class Point{
		private float x;
		private float y;
		private boolean blutoothShow = false;
	}
	
	public void startScner(){
		AppLog.d(TAG, "startScner");
		mIsScner = true;
		Message msg = new Message();
		msg.what = HANDLER_WAHT_UPDATE;
		msg.arg1 = 0;
		mHandler.sendMessage(msg);
	}
	
	public void stopScner(){
		mIsScner = false;
	}
}
