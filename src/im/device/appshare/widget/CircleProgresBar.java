/**
 * 文件名: CircleProgresBar.java
 * 版    权：  Copyright  LiJinHua  All Rights Reserved.
 * 描    述: 
 * 创建人: LiJinHua
 * 创建时间:  2014-5-19
 * 
 * 修改人：LiJinHua
 * 修改时间:2014-5-19 下午7:00:41
 * 修改内容：[修改内容]
 */
package im.device.appshare.widget;

import im.device.appshare.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.FontMetrics;
import android.graphics.RectF;
import android.graphics.Shader;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

/**
 * 圆形进度条
 * 
 * @author LiJinHua
 * @modify 2014-5-19 下午7:00:41
 */
public class CircleProgresBar extends View {

	 // 画圆环的画笔
	 private Paint mExcircleNormalPaint;
	 // 画圆环加强的画笔
	 private Paint mExcircleStrongPaint;
	// 画字体的画笔
	private Paint mTextPaint;

	// 半径
	private float innerCircleRadius;
	// 圆形颜色
	private int innerCircleColor;
	// 画实心圆的画笔
	private Paint innerCirclePaint;

	private float middleCircleRadius;
	private float middleCircleWidth;
	private int middleCircleColorNormal;
	private int middleCircleColorProgress;
	private Paint middleCircleNormalPaint;
	private Paint middleCircleProgressPaint;

	private float excirclACircleRadius;
	private float excirclACircleWidth;
	private int excirclACircleColorNormal;
	private int excirclACircleColorProgress;
	private Paint excirclACircleNormalPaint;
	private Paint excirclACircleProgressPaint;

	private float excirclBCircleRadius;
	private float excirclBCircleWidth;
	private int excirclBCircleColorNormal;
	private int excirclBCircleColorProgress;
	 private Paint excirclBCircleNormalPaint;
	 private Paint excirclBCircleProgressPaint;

	// app:middleCircleRadius="70dp"
	// app:middleCircleWidth="5dp"
	// app:middleCircleColorNormal="#d3faff"
	// app:middleCircleColorProgress="#d3faff"
	//
	// app:excirclACircleRadius="80dp"
	// app:excirclACircleWidth="1dp"
	// app:excirclACircleColorNormal="#d3faff"
	// app:excirclACircleColorProgress="#d3faff"
	//
	// app:excirclBCircleRadius="90dp"
	// app:excirclBCircleWidth="5dp"
	// app:excirclBCircleColorNormal="#d3faff"
	// app:excirclBCircleColorProgress="#d3faff"

	 // 圆环正常颜色
	 private int mExcircleNormalColor;
	 // 圆环加强颜色
	 private int mExcircleStrongColor;
	 // 圆环半径
	 private float mExcircleRadius;
	 // 圆环正常宽度
	 private float mExcircleWidthNormal;
	 // 圆环加强宽度
	 private float mExcircleWidthStrong;
	// 圆心x坐标
	private int mXCenter;
	// 圆心y坐标
	private int mYCenter;
	// 字的长度
	private float mTxtWidth;
	// 字的高度
	private float mTxtHeight;

	// 总进度
	private float mTotalProgress = 100;
	// 当前进度
	private float mProgress;

	private String mText;
	// private Drawable mIcon;


	/**
	 * @param context
	 */
	public CircleProgresBar(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public CircleProgresBar(Context context, AttributeSet attrs) {
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
	public CircleProgresBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	private void initAttrs(Context context, AttributeSet attrs) {
		TypedArray typeArray = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.CircleProgresBar, 0, 0);

		innerCircleRadius = typeArray.getDimension(
				R.styleable.CircleProgresBar_innerCircleRadius, 60);
		innerCircleColor = typeArray.getColor(
				R.styleable.CircleProgresBar_innerCircleColor, 0xd3faff);

		middleCircleRadius = typeArray.getDimension(
				R.styleable.CircleProgresBar_middleCircleRadius, 70);
		middleCircleWidth = typeArray.getDimension(
				R.styleable.CircleProgresBar_middleCircleWidth, 5);
		middleCircleColorNormal = typeArray.getColor(
				R.styleable.CircleProgresBar_middleCircleColorNormal, 0xd3faff);
		middleCircleColorProgress = typeArray.getColor(
				R.styleable.CircleProgresBar_middleCircleColorProgress,
				0xd3faff);

		excirclACircleRadius = typeArray.getDimension(
				R.styleable.CircleProgresBar_excirclACircleRadius, 80);
		excirclACircleWidth = typeArray.getDimension(
				R.styleable.CircleProgresBar_excirclACircleWidth, 5);
		excirclACircleColorNormal = typeArray.getColor(
				R.styleable.CircleProgresBar_excirclACircleColorNormal,
				0xd3faff);
		excirclACircleColorProgress = typeArray.getColor(
				R.styleable.CircleProgresBar_excirclACircleColorProgress,
				0xd3faff);

		excirclBCircleRadius = typeArray.getDimension(
				R.styleable.CircleProgresBar_excirclBCircleRadius, 90);
		excirclBCircleWidth = typeArray.getDimension(
				R.styleable.CircleProgresBar_excirclBCircleWidth, 5);
		excirclBCircleColorNormal = typeArray.getColor(
				R.styleable.CircleProgresBar_excirclBCircleColorNormal,
				0xd3faff);
		excirclBCircleColorProgress = typeArray.getColor(
				R.styleable.CircleProgresBar_excirclBCircleColorProgress,
				0xd3faff);

//		 mExcircleWidthNormal =
//		 typeArray.getDimension(R.styleable.CircleProgresBar_excircleWidthNormal,
//		 2);
//		 mExcircleWidthStrong =
//		 typeArray.getDimension(R.styleable.CircleProgresBar_excircleWidthStrong,
//		 10);
//		 mExcircleNormalColor =
//		 typeArray.getColor(R.styleable.CircleProgresBar_excircleNormalColor,
//		 0xFFFFFFFF);
//		 mExcircleStrongColor =
//		 typeArray.getColor(R.styleable.CircleProgresBar_excircleStrongColor,
//		 0xFFFFFFFF);
//		 mExcircleRadius =
//		 typeArray.getDimension(R.styleable.CircleProgresBar_excircleRadius,
//		 innerCircleRadius + mExcircleWidthStrong / 2);

		// app:middleCircleRadius="70dp"
		// app:middleCircleWidth="5dp"
		// app:middleCircleColorNormal="#d3faff"
		// app:middleCircleColorProgress="#d3faff"
		//
		// app:excirclACircleRadius="80dp"
		// app:excirclACircleWidth="1dp"
		// app:excirclACircleColorNormal="#d3faff"
		// app:excirclACircleColorProgress="#d3faff"
		//
		// app:excirclBCircleRadius="90dp"
		// app:excirclBCircleWidth="5dp"
		// app:excirclBCircleColorNormal="#d3faff"
		// app:excirclBCircleColorProgress="#d3faff"

		// int icon = typeArray.getResourceId(R.styleable.CircleProgresBar_icon,
		// 0);
		// if(icon != 0){
		// mIcon = context.getResources().getDrawable(icon);
		// }
	}

	private void initVariable() {

		mPaint.setColor(Color.RED);
		mPaint.setStyle(Paint.Style.FILL);

		innerCirclePaint = new Paint();
		innerCirclePaint.setAntiAlias(true);
		innerCirclePaint.setColor(innerCircleColor);
		innerCirclePaint.setStyle(Paint.Style.FILL);

		middleCircleNormalPaint = new Paint();
		middleCircleNormalPaint.setAntiAlias(true);
		middleCircleNormalPaint.setColor(middleCircleColorNormal);
		middleCircleNormalPaint.setStyle(Paint.Style.STROKE);
		middleCircleNormalPaint.setStrokeWidth(middleCircleWidth);

		middleCircleProgressPaint = new Paint();
		middleCircleProgressPaint.setAntiAlias(true);
		middleCircleProgressPaint.setColor(middleCircleColorProgress);
		middleCircleProgressPaint.setStyle(Paint.Style.STROKE);
		middleCircleProgressPaint.setStrokeWidth(middleCircleWidth);

		excirclACircleNormalPaint = new Paint();
		excirclACircleNormalPaint.setAntiAlias(true);
		excirclACircleNormalPaint.setColor(excirclACircleColorNormal);
		excirclACircleNormalPaint.setStyle(Paint.Style.STROKE);
		excirclACircleNormalPaint.setStrokeWidth(excirclACircleWidth);

		excirclACircleProgressPaint = new Paint();
		excirclACircleProgressPaint.setAntiAlias(true);
		excirclACircleProgressPaint.setColor(excirclACircleColorProgress);
		excirclACircleProgressPaint.setStyle(Paint.Style.STROKE);
		excirclACircleProgressPaint.setStrokeWidth(excirclACircleWidth);
		excirclACircleProgressPaint.setStrokeCap(Cap.SQUARE);

		 excirclBCircleNormalPaint = new Paint();
		 excirclBCircleNormalPaint.setAntiAlias(true);
		 excirclBCircleNormalPaint.setColor(excirclBCircleColorNormal);
		 excirclBCircleNormalPaint.setStyle(Paint.Style.STROKE);
		 excirclBCircleNormalPaint.setStrokeWidth(excirclBCircleWidth);
		
		 excirclBCircleProgressPaint = new Paint();
		 excirclBCircleProgressPaint.setAntiAlias(true);
		 excirclBCircleProgressPaint.setColor(excirclBCircleColorProgress);
		 excirclBCircleProgressPaint.setStyle(Paint.Style.STROKE);
		 excirclBCircleProgressPaint.setStrokeWidth(excirclBCircleWidth);

		 mExcircleNormalPaint = new Paint();
		 mExcircleNormalPaint.setAntiAlias(true);
		 mExcircleNormalPaint.setColor(mExcircleNormalColor);
		 mExcircleNormalPaint.setStyle(Paint.Style.STROKE);
		 mExcircleNormalPaint.setStrokeWidth(mExcircleWidthNormal);
		
		 mExcircleStrongPaint = new Paint();
		 mExcircleStrongPaint.setAntiAlias(true);
		 mExcircleStrongPaint.setColor(mExcircleStrongColor);
		 mExcircleStrongPaint.setStyle(Paint.Style.STROKE);
		 mExcircleStrongPaint.setStrokeWidth(mExcircleWidthStrong);

		mTextPaint = new Paint();
		mTextPaint.setAntiAlias(true);
		mTextPaint.setStyle(Paint.Style.FILL);
		mTextPaint.setColor(Color.parseColor("#797979"));
		mTextPaint.setTextSize(innerCircleRadius / 3);
		mTextPaint.setFakeBoldText(true);

		FontMetrics fm = mTextPaint.getFontMetrics();
		mTxtHeight = (int) Math.ceil(fm.descent - fm.ascent);

	}

	@Override
	protected void onDraw(Canvas canvas) {

		mXCenter = getWidth() / 2;
		mYCenter = getHeight() / 2;

		canvas.drawCircle(mXCenter, mYCenter, innerCircleRadius,
				innerCirclePaint);

		if (mProgress >= 0.0f) {

			float p = (float) mProgress / mTotalProgress;

			 drawCircleProgress(canvas,mXCenter,mYCenter,middleCircleRadius,middleCircleNormalPaint);
			 drawCircleProgress(canvas,mXCenter,mYCenter,middleCircleRadius,middleCircleProgressPaint,p,middleCircleWidth);

			drawCircleProgress(canvas, mXCenter, mYCenter,excirclACircleRadius, excirclACircleNormalPaint);
			drawCircleProgress(canvas, mXCenter, mYCenter,excirclACircleRadius, excirclACircleProgressPaint, p,excirclACircleWidth);

//			 drawCircleProgress(canvas,mXCenter,mYCenter,excirclBCircleRadius,excirclBCircleNormalPaint);
			 drawCircleProgress(canvas,mXCenter,mYCenter,excirclBCircleRadius,excirclBCircleProgressPaint,p,excirclBCircleWidth);


			String txt = mText;
			if (!TextUtils.isEmpty(txt)) {
				mTxtWidth = mTextPaint.measureText(txt, 0, txt.length());
				canvas.drawText(txt, mXCenter - mTxtWidth / 2, mYCenter
						+ mTxtHeight / 4, mTextPaint);
			}

//			String ctext = countText;
//			if (!TextUtils.isEmpty(ctext)) {
//				countTxtWidth = countTextPaint.measureText(ctext, 0,
//						ctext.length());
//				int y = (int) (mYCenter + mTxtHeight - countTxtHeight / 2.5);
//				canvas.drawText(ctext, mXCenter - countTxtWidth / 2, y,
//						countTextPaint);
//			}
			// if(mIcon != null){
			// mIcon.draw(canvas);
			// }
		}
	}

	Paint mPaint = new Paint();

	private void drawCircleProgress(Canvas canvas, int centerX, int centerY,
			float radius, Paint paint) {
		drawCircleProgress(canvas, centerX, centerY, radius, paint, 1, 0);
	}

	private void drawCircleProgress(Canvas canvas, int centerX, int centerY,
			float radius, Paint paint, float progress, float w) {

		RectF ovalS = new RectF();
		ovalS.left = (centerX - radius);
		ovalS.top = (centerY - radius);
		ovalS.right = radius * 2 + (centerX - radius);
		ovalS.bottom = radius * 2 + (centerY - radius);
		float sweepAngle = progress * 360;
		
		canvas.drawArc(ovalS, -90, sweepAngle, false, paint); //

		 if(progress < 1){
			 float[] center0 = getCirclePoint(centerX,centerY,radius,-90);
			 float cx0 = center0[0];
			 float cy0 = center0[1];
			 float[] center = getCirclePoint(centerX,centerY,radius,sweepAngle-90);
			 float cx = center[0];
			 float cy = center[1];
			 mPaint.setColor(paint.getColor());
			 canvas.drawCircle(cx0, cy0, w/2, mPaint);
			 canvas.drawCircle(cx, cy, w/2, mPaint);
		 }
	}

	private float[] getCirclePoint(int centerX, int centerY, float radius,
			float ao) {
		// 圆点坐标：(x0,y0)
		// 半径：r
		// 角度：a0
		// 则圆上任一点为：（x1,y1）
		// x1 = x0 + r * cos(ao * 3.14 /180 )
		// y1 = y0 + r * sin(ao * 3.14 /180 )
		float x = (float) (centerX + radius * Math.cos(ao * 3.14d / 180d));
		float y = (float) (centerY + radius * Math.sin(ao * 3.14d / 180d));
		return new float[] { x, y };
	}

	public void setProgress(float progress) {
		setProgress(progress, null);
	}

	public void setProgress(float progress, String text) {
		mProgress = progress;
		mText = text;
		postInvalidate();
	}
}
