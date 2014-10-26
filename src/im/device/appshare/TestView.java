/**
 * 文件名: TestView.java
 * 版    权：  Copyright  LiJinHua  All Rights Reserved.
 * 描    述: 
 * 创建人: LiJinHua
 * 创建时间:  2014-5-21
 * 
 * 修改人：LiJinHua
 * 修改时间:2014-5-21 上午11:31:14
 * 修改内容：[修改内容]
 */
package im.device.appshare;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * 
 * @author LiJinHua
 * @modify 2014-5-21 上午11:31:14
 */
public class TestView extends View {

	// 画实心圆的画笔
		private Paint mCirclePaint;
	
		private HashMap<Integer,ArrayList<Point>> devicesLocationMap = new HashMap<Integer,ArrayList<Point>>();
		
	/**
	 * @param context
	 */
	public TestView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public TestView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// 获取自定义的属性
		initVariable();
	}

	private void initVariable() {
		mCirclePaint = new Paint();
		mCirclePaint.setAntiAlias(true);
		mCirclePaint.setColor(Color.RED);
		mCirclePaint.setStyle(Paint.Style.FILL);
		
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if(devicesLocationMap == null){
			return;
		}
//		canvas.drawCircle(0, 0, 20, mCirclePaint);
//		canvas.drawCircle(480, 690, 20, mCirclePaint);
		Iterator<Integer> iterator = devicesLocationMap.keySet().iterator();
		while(iterator.hasNext()){
			
			Integer inte = iterator.next();
			if(inte == 0){
				mCirclePaint.setColor(Color.RED);
			}else if(inte == 1){
				mCirclePaint.setColor(Color.BLUE);
			}else if(inte == 2){
				mCirclePaint.setColor(Color.WHITE);
			}else if(inte == 3){
				mCirclePaint.setColor(Color.GREEN);
			}else{
				mCirclePaint.setColor(Color.CYAN);
			}
			ArrayList<Point> list = devicesLocationMap.get(inte);
			for(Point d : list){
				canvas.drawCircle(d.x, d.y, 10, mCirclePaint);
				canvas.drawText("x:"+d.x+" y:"+d.y,d.x, d.y, mCirclePaint);
			}
		}
		mCirclePaint.setColor(Color.RED);
	}
	
	public void addPoint(Point p){
		addPoint(p,0);
	}
	
	public void addPoint(Point p,int i){
		ArrayList<Point> list  = devicesLocationMap.get(i);
		if(list == null){
			list = new ArrayList<TestView.Point>();
			devicesLocationMap.put(i, list);
		}
		list.add(p);
	}
	
	public HashMap<Integer, ArrayList<Point>> getDevicesLocationMap() {
		return devicesLocationMap;
	}

	public void setDevicesLocationMap(
			HashMap<Integer, ArrayList<Point>> devicesLocationMap) {
		this.devicesLocationMap = devicesLocationMap;
	}

	public static class Point{
		public float x;
		public float y;
		public Point(float x, float y) {
			super();
			this.x = x;
			this.y = y;
		}
	}

}
