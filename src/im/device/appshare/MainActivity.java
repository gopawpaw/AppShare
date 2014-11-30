/**
 * 文件名: MainActivity.java
 * 版    权：  Copyright  LiJinHua  All Rights Reserved.
 * 描    述: 
 * 创建人: LiJinHua
 * 创建时间:  2014-10-22
 * 
 * 修改人：LiJinHua
 * 修改时间:2014-10-22 下午10:40:15
 * 修改内容：[修改内容]
 */
package im.device.appshare;

import im.device.appshare.TestView.Point;
import im.device.appshare.animation.shortcut.view.DragController;
import im.device.appshare.animation.shortcut.view.DragLayer;
import im.device.appshare.animation.shortcut.view.DragLayer.OnDockEndListener;
import im.device.appshare.animation.shortcut.view.DragLayer.OnDragEndListener;
import im.device.appshare.animation.shortcut.view.DragLayer.OnMoveListener;
import im.device.appshare.utils.Tools;
import im.device.appshare.utils.VibratorHelper;
import im.device.appshare.widget.BluetoothScanerView;
import im.device.appshare.widget.ObservableHorizontalScrollView;
import im.device.appshare.widget.ObservableHorizontalScrollView.ScrollViewListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gopawpaw.core.bluetooth.scanner.BluetoothScanner;
import com.gopawpaw.core.bluetooth.scanner.ScannerCallback;
import com.gopawpaw.droidcore.AppConfig;
import com.gopawpaw.droidcore.activity.BaseActivity;
import com.gopawpaw.droidcore.http.HttpHelper;
import com.gopawpaw.droidcore.http.action.HttpAction;
import com.gopawpaw.droidcore.http.action.HttpActionListener;
import com.gopawpaw.droidcore.utils.CommonUtils;
import com.tendcloud.tenddata.TCAgent;

/**
 * 
 * @author LiJinHua
 * @modify 2014-10-22 下午10:40:15
 */
public class MainActivity extends BaseActivity implements
OnDockEndListener, OnDragEndListener,OnMoveListener,OnLongClickListener, ScannerCallback,HttpActionListener,OnClickListener,ScrollViewListener{

	private static final int HANDLER_WHAT_FOUND_BLUTOOTH = 0;
	
	private static final int HANDLER_WHAT_FOUND_APP = 1;
	
	private static final int HANDLER_WHAT_SCROLL_ANIMATION = 2;
	
	private static final int HANDLER_WHAT_SCROLL_ANIMATION_START = 3;
	
	private static final int HANDLER_WHAT_ACTION_SCANER= 4;
	
	private LinearLayout mLLApps;
	private LinearLayout mLLBlutooths;
	private LayoutInflater mLayoutInflater;
	private EditText mAppInput;
	private ImageView mIVFingerLeft;
	private ImageView mIVFingerRight;
	private ImageView mIVMoreAppLeft;
	private ImageView mIVMoreAppRight;
	private ImageView mIVHelp;
	private ObservableHorizontalScrollView mHSVBlutooths;
	private ObservableHorizontalScrollView mHSVApps;
	private BluetoothScanerView mBluetoothScanerView;
	/**
	 * 拖动层
	 */
	private DragLayer mDragLayer;
	
	/**
	 * 蓝牙View列表,用于计算是否发生碰撞
	 */
	private ArrayList<View> mViewBluetooth = new ArrayList<View>();
//	private ArrayList<View> mViewApps = new ArrayList<View>();
	/**
	 * 发生碰撞的蓝牙
	 */
	private View mBlutoothHit;

	/**
	 * 蓝牙被撞击标识符
	 */
	private boolean isBlutoothHitFlag = false;
	
	private TestView mTestView;
	
	private Point mMoveBeginPoint = new Point(0, 0);
	
	private BluetoothScanner bluetoothScanner;
	private boolean isContinuScaner = true;
	private HashMap<String,BluetoothDevice> mBluetoothDeviceMap = new HashMap<String,BluetoothDevice>();
	private List<PackageInfo> mListData =  new ArrayList<PackageInfo>();
	/**
	 * 选中的app信息
	 */
	private PackageInfo mSelectedPackageInfo;
	/**
	 * 选中的设备信息
	 */
	private BluetoothDevice mSelectedBluetoothDevice;
	
	private HttpAction mAction;
	
	private String mApkUrl = null;
	
	private int mWindowWidth;
	
	private LoadAsyncTask load;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppLog.d(TAG, "onCreate load="+load);
		setContentView(R.layout.main);
		mLLApps = (LinearLayout) findViewById(R.id.ll_apps);
		mLLBlutooths = (LinearLayout) findViewById(R.id.ll_blutooths);
		mHSVBlutooths = (ObservableHorizontalScrollView) findViewById(R.id.hsv_blutooths);
		mHSVApps = (ObservableHorizontalScrollView) findViewById(R.id.hsv_apps);
		mDragLayer = (DragLayer) findViewById(R.id.draglayer);
		mAppInput = (EditText) findViewById(R.id.et_app_input);
		mIVFingerLeft = (ImageView) findViewById(R.id.iv_finger_left);
		mIVFingerRight = (ImageView) findViewById(R.id.iv_finger_right);
		mIVMoreAppLeft = (ImageView) findViewById(R.id.iv_more_app_left);
		mIVMoreAppRight = (ImageView) findViewById(R.id.iv_more_app_right);
		mIVHelp = (ImageView) findViewById(R.id.iv_help);
		mBluetoothScanerView = (BluetoothScanerView)findViewById(R.id.bsv_blutooth_scaner);
		
		mTestView = (TestView) findViewById(R.id.radar_scaner_testview);
		
		mLayoutInflater = LayoutInflater.from(this);
		
		mDragLayer.setOnDockEndListener(this);
		mDragLayer.setOnDragEndListener(this);
		mDragLayer.setOnMoveListener(this);
		mIVFingerLeft.setOnClickListener(this);
		mIVFingerRight.setOnClickListener(this);
		mIVMoreAppLeft.setOnClickListener(this);
		mIVMoreAppRight.setOnClickListener(this);
		mIVHelp.setOnClickListener(this);
		mAppInput.addTextChangedListener(mTextWatcher);
		mHSVBlutooths.setScrollViewListener(this);
		mHSVApps.setScrollViewListener(this);
		
		mWindowWidth = this.getWindowManager().getDefaultDisplay().getWidth();
		
		load = new LoadAsyncTask();
		load.execute("");
		
		TCAgent.init(this);
		AppConfig.init(this);
		if(AppConfig.IS_PRODUCT){
			HttpHelper.setHostUrl(AppConfig.URL_ID_HOST_PRD);
		}else{
			HttpHelper.setHostUrl(AppConfig.URL_ID_HOST_STG);
		}
		
		String imei = CommonUtils.getIMEI(this);
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("app", "im.device.appshare");
		params.put("client", "1");
		params.put("version", CommonUtils.getVersionName(this));
		params.put("imei", imei);
		
		mAction = new HttpAction(this, this);
		mAction.sendRequest(getString(R.string.url_app_update),params);
		
		Map<String, Object> map = Tools.getDeviceInfo(this);
		TCAgent.onEvent(this, "Open-AppShare",imei,map);
		
//		mBluetoothScanerView.startScner();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
//		startScanerRadar();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
//		stopScanerRadar();
//		mBluetoothDeviceMap.clear();
//		mViewBluetooth.clear();
//		mLLChild.removeAllViews();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		if(bluetoothScanner != null){
			bluetoothScanner.closeBluetooth();
			bluetoothScanner = null;
		}
		
		if (load != null && load.getStatus() != AsyncTask.Status.FINISHED){
			load.cancel(true);
			AppLog.d(TAG, "onDestroy load.isCancelled:"+load.isCancelled());
			load = null;
		}
		
		mHandler.removeMessages(HANDLER_WHAT_ACTION_SCANER);
	}
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HANDLER_WHAT_FOUND_BLUTOOTH:
				BluetoothDevice device = (BluetoothDevice) msg.obj;
//				for (int i = 0; i < 5; i++) {
					updateDeviceView(device,msg.arg1);
//				}
				break;
			case HANDLER_WHAT_FOUND_APP:
				List<PackageInfo> listData = (List<PackageInfo>) msg.obj;
				updateAppsView(listData);
				break;
			case HANDLER_WHAT_SCROLL_ANIMATION:
				int[] x = (int[]) msg.obj;
				int p = msg.arg1;
				AppLog.i(TAG, "p:"+p+" x[p]:"+x[p]);
				if(p+1 < x.length){
					mHSVBlutooths.scrollBy(x[p], 0);
					actionScrollAnimation(x,p+1);
				}
				break;
			case HANDLER_WHAT_SCROLL_ANIMATION_START:
				this.removeMessages(HANDLER_WHAT_SCROLL_ANIMATION_START);
				int offsetX = mLLBlutooths.getWidth() - mWindowWidth - mHSVBlutooths.getScrollX();
				AppLog.d(TAG, "mLLBlutooths.getWidth():"+mLLBlutooths.getWidth()+" offsetX:"+offsetX+" mWindowWidth:"+mWindowWidth);
				int[] xx = getAnimationX(offsetX);
				actionScrollAnimation(xx,0);
				break;
			case HANDLER_WHAT_ACTION_SCANER:
				this.removeMessages(HANDLER_WHAT_ACTION_SCANER);
				AppLog.i(TAG, "actionScaner = false isContinuScaner="+isContinuScaner);
				if(isContinuScaner){
					bluetoothScanner.startDiscovery();
				}
				break;
			default:
				break;
			}
		};
	};
	

	private Animation getSunAnimation() {
		/**加载透明动画**/
		Animation animation = AnimationUtils.loadAnimation(this, R.anim.sun);
		animation.setRepeatCount(Animation.INFINITE);  
		animation.setRepeatMode(Animation.REVERSE);
		return animation;
	}
	
	@Override
	public void onDragEnd(View dragView, float x, float y) {
		AppLog.d(TAG, "onDragEnd:"+x+" , "+y);
		dragView.setVisibility(View.VISIBLE);
		dragView.startAnimation(im.device.appshare.utils.AnimationUtils.getAppRadomAnimation());
		
		if(mBlutoothHit != null){
			mBlutoothHit.setSelected(false);
			mBlutoothHit = null;
			isBlutoothHitFlag = false;
		}
		if(mSelectedPackageInfo != null && mSelectedBluetoothDevice !=null){
			actionShare(mSelectedPackageInfo, mSelectedBluetoothDevice);
			mSelectedPackageInfo = null;
			mSelectedBluetoothDevice = null;
		}
	}

	@Override
	public void onDockEnd(int inWhich) {
		AppLog.d(TAG, "onDockEnd inWhich:"+inWhich);
	}

	@Override
	public void onMove(View v, float x, float y) {
		AppLog.d(TAG, "onMove:"+x+" , "+y);
		if(mMoveBeginPoint.x < 0){
			mMoveBeginPoint.x = x;
			mMoveBeginPoint.y = y;
		}
		bluetoothMatchHit(v, x, y);
	}

	@Override
	public boolean onLongClick(View v) {
		mMoveBeginPoint.x = -1;
		mMoveBeginPoint.y = -1;
		VibratorHelper.Vibrate(this, 5);
		mDragLayer.startDrag(v, null, null, DragController.DRAG_ACTION_COPY,
				1f);
		v.clearAnimation();
		v.setVisibility(View.INVISIBLE);
		mSelectedPackageInfo = (PackageInfo) v.getTag();
		return false;
	}
	
	/**
	 * 
	 * [银行卡匹配碰撞]<BR>
	 * [功能详细描述]
	 * 
	 * @param v
	 *            触摸移动的View
	 * @param x
	 *            触摸点X坐标
	 * @param y
	 *            触摸点Y坐标
	 */
	private void bluetoothMatchHit(View v, float x, float y) {
		
		View view = getMatchHit(v, x, y);

		if(view != null && mBlutoothHit != null && view!=mBlutoothHit){
			mBlutoothHit.setSelected(false);
			mBlutoothHit = null;
			isBlutoothHitFlag = false;
			mSelectedBluetoothDevice = null;
			return;
		}
		
		if (view != null) {
			if (mBlutoothHit == null || !isBlutoothHitFlag) {
				VibratorHelper.Vibrate(this, 2);
				isBlutoothHitFlag = true;
				mBlutoothHit = view;
				view.setSelected(true);
				mSelectedBluetoothDevice = (BluetoothDevice) mBlutoothHit.getTag();
				return;
			}
		} else {

			if (isBlutoothHitFlag && mBlutoothHit != null) {
				mBlutoothHit.setSelected(false);
			}
			mBlutoothHit = null;
			isBlutoothHitFlag = false;
			mSelectedBluetoothDevice = null;
		}
	}
	
	private int[] location = new int[2];
	/**
	 * 
	 * [获取匹配碰撞的View]<BR>
	 * [功能详细描述]
	 * 
	 * @param v
	 *            触摸移动的View
	 * @param x
	 *            触摸点X坐标
	 * @param y
	 *            触摸点Y坐标
	 * @return 返回发生碰撞的View
	 */
	private View getMatchHit(View v, float x, float y) {

		float offsetX = x-mMoveBeginPoint.x;
		float offsetY = y-mMoveBeginPoint.y;
		
		View appname = v.findViewById(R.id.tv_appname);
		appname.getLocationOnScreen(location);
		
		float appLeftTopX = location[0];
		float appLeftTopY = location[1];
		float appRightBottomX = location[0] + appname.getWidth();
		float appRightBottomY = location[1] + appname.getHeight();
		float moveViewx = (appLeftTopX+appRightBottomX)/2 + offsetX;
		float moveViewy = (appLeftTopY+appRightBottomY)/2 + offsetY;
		View view = null;
		for (View blutoothView : mViewBluetooth) {
			blutoothView.getLocationOnScreen(location);

			float leftTopX = location[0];
			float leftTopY = location[1];
			float rightBottomX = location[0] + blutoothView.getWidth();
			float rightBottomY = location[1] + blutoothView.getHeight();
			if (isHit(moveViewx,moveViewy, leftTopX, leftTopY, rightBottomX, rightBottomY)) {
				// 发生碰撞的蓝牙
				view = blutoothView;
			}
		}
		return view;
	}
	
	/**
	 * [判断是否属于碰撞范围]<BR>
	 * [功能详细描述]
	 * 
	 * @param touchX
	 *            触摸点X坐标
	 * @param touchY
	 *            触摸点Y坐标
	 * @param leftTopX
	 *            View的左上角X坐标
	 * @param leftTopY
	 *            View的左上角Y坐标
	 * @param rightBottomX
	 *            View的右下角X坐标
	 * @param rightBottomY
	 *            View的右下角Y坐标
	 * @return 是否属于碰撞
	 */
	private boolean isHit(float touchX, float touchY, float leftTopX,
			float leftTopY, float rightBottomX, float rightBottomY) {

		if (touchX > leftTopX && touchX < rightBottomX && touchY > leftTopY
				&& touchY < rightBottomY) {
			return true;
		}

		return false;
	}
	
	class LoadAsyncTask extends AsyncTask<String, Integer, List<PackageInfo>>{
		
		@Override
		protected List<PackageInfo> doInBackground(String... params) {
			if(isCancelled()) return null;
			List<PackageInfo> list = getAppItemViewList();
			if(isCancelled()) return null;
			Collections.sort(list, mAppComparator);
			if(isCancelled()) return null;
			return list;
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			if(isCancelled()) return ;
			super.onProgressUpdate(values);
		}
		
		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			AppLog.d(TAG, "LoadAsyncTask onCancelled");
		}
		
		@Override
		protected void onCancelled(List<PackageInfo> result) {
			// TODO Auto-generated method stub
			super.onCancelled(result);
			AppLog.d(TAG, "LoadAsyncTask onCancelled result:"+result);
		}
		
		@Override
		protected void onPostExecute(List<PackageInfo> result) {
			AppLog.w(TAG, "onPostExecute isCancelled():"+isCancelled());
			if(isCancelled()) return ;
			
			mListData = result;
			updateAppsView(result);
		}
	}
	
	private List<PackageInfo> getAppItemViewList() {
		List<PackageInfo> packages = getPackageManager()
				.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
		List<PackageInfo> list = new ArrayList<PackageInfo>();
		
		for (int i = 0; i < packages.size(); i++) {
			PackageInfo packageInfo = packages.get(i);
			if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
				continue;// 如果系统应用，则不添加至appList
			}
			list.add(packageInfo);
		}
		return list;
	}
	
	private AppComparator mAppComparator = new AppComparator();
	
	class AppComparator implements Comparator<PackageInfo>{

		@Override
		public int compare(PackageInfo lhs, PackageInfo rhs) {
			String appName1 = lhs.applicationInfo.loadLabel(
					MainActivity.this.getPackageManager()).toString();
			String appName2 = rhs.applicationInfo.loadLabel(
					MainActivity.this.getPackageManager()).toString();
			return appName1.compareTo(appName2);
		}
	}
	
	@SuppressLint("NewApi")
	private void updateAppsView(List<PackageInfo> data){
		AppLog.i(TAG, "updateAppsView start");
		int count = mLLApps.getChildCount();
		if(count>0){
			AppLog.d(TAG, "mLLApps.getWidth befor:"+mLLApps.getWidth()+" count:"+count);
			for (int i = 0; i < count; i++) {
				View v = mLLApps.getChildAt(i);
				boolean flag = false;
				PackageInfo packageInfo = (PackageInfo) v.getTag();
				AppLog.i(TAG, "updateAppsView i"+i+" "+packageInfo.packageName);
				for(int n=0;n<data.size();n++){
					PackageInfo pi = data.get(n);
					if(packageInfo.packageName.equals(pi.packageName)){
						AppLog.d(TAG, "updateAppsView i"+i+" "+packageInfo.packageName);
						v.setVisibility(View.VISIBLE);
						v.startAnimation(im.device.appshare.utils.AnimationUtils.getAppRadomAnimation());
						flag = true;
						break;
					}
				}
				if(!flag){
					Animation a = v.getAnimation();
					if(a != null){
						a.cancel();
						v.clearAnimation();
					}
					v.setVisibility(View.GONE);
				}
			}
		}else{
			AppLog.d(TAG, "updateAppsView:"+data.size());
			for(int position = 0;position <data.size() ;position++){
				PackageInfo packageInfo = data.get(position);
				String appName = packageInfo.applicationInfo.loadLabel(
						this.getPackageManager()).toString();
				
				Drawable appIcon = packageInfo.applicationInfo
						.loadIcon(this.getPackageManager());
				
				View view = null;
				int k = position % 3;
				if(k == 0){
					view = mLayoutInflater.inflate(R.layout.app_item1, null);
				}else if(k == 1){
					view = mLayoutInflater.inflate(R.layout.app_item2, null);
				}else if(k == 2){
					view = mLayoutInflater.inflate(R.layout.app_item3, null);
				}else{
					view = mLayoutInflater.inflate(R.layout.app_item1, null);
				}
				ImageView ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
				TextView tvAppName = (TextView) view.findViewById(R.id.tv_appname);
				ivIcon.setImageDrawable(appIcon);
				tvAppName.setText(appName);
				view.setTag(packageInfo);
				view.setOnLongClickListener(this);
				mLLApps.addView(view);
//				view.startAnimation(im.device.appshare.utils.AnimationUtils.getAppRadomAnimation());
			}
		}
		AppLog.i(TAG, "updateAppsView end");
		if(mLLApps.getWidth() >= mWindowWidth){
			mIVMoreAppRight.setVisibility(View.VISIBLE);
		}else{
			mIVMoreAppRight.setVisibility(View.INVISIBLE);
		}
	}
	
	private void startScanerRadar(){
		if(bluetoothScanner != null){
			bluetoothScanner.cancelDiscovery();
			bluetoothScanner = null;
		}
		bluetoothScanner = BluetoothScanner.Factory.create(this.getApplicationContext());
		bluetoothScanner.setCallback(this);
		isContinuScaner = true;
		actionScaner();
	}
	
	private void stopScanerRadar(){
		isContinuScaner = false;
		if(bluetoothScanner != null){
			bluetoothScanner.cancelDiscovery();
		}
	}
	
	private void actionScaner(){
		if(!bluetoothScanner.startDiscovery()){
			mHandler.sendEmptyMessageDelayed(HANDLER_WHAT_ACTION_SCANER, 4000);
		}else{
			AppLog.i(TAG, "actionScaner = true");
		}
	}
	
	@Override
	public void onFoundDevice(BluetoothDevice device,short rssi) {
		AppLog.w(TAG, "onFoundDevice isFinishing():"+isFinishing()+" "+this);
		if(device != null){
			String key = device.getName()+":"+device.getAddress();
			AppLog.i(TAG, "onFoundDevice:"+key);
			if(null == mBluetoothDeviceMap.get(key)){
				mBluetoothDeviceMap.put(key, device);
				Message msg = new Message();
				msg.what = HANDLER_WHAT_FOUND_BLUTOOTH;
				msg.obj = device;
				msg.arg1 = rssi;
				mHandler.sendMessage(msg);
			}
		}
	}
	
	@Override
	public void onScanFinished() {
		AppLog.i(TAG,"onScanFinished");
		if(isContinuScaner){
			actionScaner();
		}
	}
	
	private void updateDeviceView(BluetoothDevice device,int rssi){
		View view ;
		int n = (int)(Math.random()*3);
		switch (n) {
		case 0:
			view = mLayoutInflater.inflate(R.layout.device_item1, null);
			break;
		case 1:
			view = mLayoutInflater.inflate(R.layout.device_item2, null);
			break;
		case 2:
			view = mLayoutInflater.inflate(R.layout.device_item3, null);
			break;
		default:
			view = mLayoutInflater.inflate(R.layout.device_item1, null);
			break;
		}
		
		mViewBluetooth.add(view);
		mLLBlutooths.addView(view);
		TextView name = (TextView) view.findViewById(R.id.tv_name);
		name.setText(device.getName());
		view.setTag(device);
		
		mHandler.sendEmptyMessageDelayed(HANDLER_WHAT_SCROLL_ANIMATION_START, 500);
		
	}
	
	private int[] getAnimationX(int offsetX){
		if(offsetX < 10){
			int[] x = new int[1];
			x[0] = offsetX;
			return x;
		}else{
			int a = 2;
			int t = (int) Math.sqrt((offsetX/a));
			int[] x = new int[2*t];
			int s = 0;
			for(int i=0;i<x.length;i++){
				// S = a(t*t)/2;
				int tt = (i+1);
				if((i+1)>=t){
					tt = (2*t) - (i+1);
				}
				int st = (a*(tt*tt))/2;
				if(i>=x.length){
					AppLog.d(TAG, "break i="+i+" x.length="+x.length);
					break;
				}
				x[i] = Math.abs(st - s);
				s = st;
			}
			return x;
		}
	}
	
	private void actionScrollAnimation(int[] x,int p){
		Message msg = new Message();
		msg.what = HANDLER_WHAT_SCROLL_ANIMATION;
		msg.obj = x;
		msg.arg1 = p;
		mHandler.sendMessageDelayed(msg, 10);
	}
	
	/**
	 * 执行分享
	 * @param p
	 * @param d
	 * @author LiJinHua
	 * @modify 2014-10-26 上午11:34:29
	 */
	private void actionShare(PackageInfo p,BluetoothDevice d){
		BluetoothTransferActivity.actionStart(this, p, d);
	}

	@Override
	public void onHttpActionResponse(int state, Object data, String urlId,
			int connectionId, Object obj) {
		JSONObject json = null;
		try {
			String str = new String((byte[])data);
			json = new JSONObject(str);
		} catch (Exception e) {
			// TODO: handle exception
		}
		AppLog.d(TAG, "state="+state+" onHttpActionResponse="+json);
		Message msg = new Message();
		msg.what = state;
		msg.arg2 = connectionId;
		msg.obj = json;

		mUIHandler.sendMessage(msg);
	}

	private Handler mUIHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == HttpActionListener.STATE_SUCCESS){
				JSONObject json = (JSONObject) msg.obj;
				
				String flagStr = (String)com.gopawpaw.droidcore.utils.Tools.getValuseFromJSONObject(json, JsonKey.DATA,JsonKey.FLAG);
				String msgtip = (String)com.gopawpaw.droidcore.utils.Tools.getValuseFromJSONObject(json, JsonKey.DATA,JsonKey.MSG);
				String apkurl = (String)com.gopawpaw.droidcore.utils.Tools.getValuseFromJSONObject(json, JsonKey.DATA,JsonKey.URL);
				
				int flag = 0;
				try {
					flag = Integer.parseInt(flagStr);
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				showUpdateMessage(flag,msgtip,apkurl);
			}
//				showUpdateMessage(0,"无升级提示","http://www.demo.com/apk.apk");
//				showUpdateMessage(1,"建议升级提示","http://www.demo.com/apk.apk");
//				showUpdateMessage(2,"强制升级提示","http://www.demo.com/apk.apk");
//				showUpdateMessage(3,"公告提示","http://www.demo.com/apk.apk");
		}
	};
	
	/**
	 * 显示更新信息
	 * @author EX-LIJINHUA001
	 * @date 2013-4-12
	 * @param flag
	 * @param msg
	 * @param apkUrl
	 */
	private void showUpdateMessage(int flag,String msg,String apkUrl){
		mApkUrl = apkUrl;
		switch (flag) {
		case 0:
			/**无需升级**/
			break;
		case 1:
			/**建议升级**/
			showMessageDialog(0, msg, getString(R.string.sure), getString(R.string.cancel));
			break;
		case 2:
			/**强制升级**/
			showMessageDialog(1, msg, getString(R.string.sure), getString(R.string.cancel));
			break;
		case 3:
			/**公告**/
			showMessageDialog(2, msg, getString(R.string.sure));
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onMessageDialogBtn1Click(int id) {
		//确定
		if(id == 0 || id == 1){
			Uri uri = Uri.parse(mApkUrl);
			Intent intent = new Intent(Intent.ACTION_VIEW,uri);
			startActivity(intent);
			if(id == 1){
				CommonUtils.finishProgram();
			}
		}
	}
	
	@Override
	protected void onMessageDialogBtn2Click(int id) {
		if(id == 1){
			//强制升级，取消
			CommonUtils.finishProgram();
		}
	}
	
	@Override
	protected void onMessageDialogCancel(int id) {
		if(id == 1){
			//强制升级，取消
			CommonUtils.finishProgram();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_help:
			Intent intent=new Intent();
			intent.setClass(this,AboutActivity.class);
			startActivity(intent);
			break;
		case R.id.iv_finger_left:
			scrollBlutoothsToBegin();
			break;
		case R.id.iv_finger_right:
			scrollBlutoothsToEnd();
			break;
		case R.id.iv_more_app_left:
			scrollAppToBegin();
			break;
		case R.id.iv_more_app_right:
			scrollAppToEnd();
			break;
		default:
			break;
		}
	}
	
	private TextWatcher mTextWatcher = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable s) {
			searchAppsName(s.toString());
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
		}
	};
	
	private void searchAppsName(final String name) {
		AppLog.i(TAG, "searchAppsName : "+name);
		Tools.searchInfoInThread(MainActivity.this, mListData,
						Tools.TYPE_APP_NAME, name,mHandler,HANDLER_WHAT_FOUND_APP);
	}

	@Override
	public void onScrollChanged(ObservableHorizontalScrollView scrollView,
			int x, int y, int oldx, int oldy) {
		if(mHSVBlutooths == scrollView){
			if(mLLBlutooths.getWidth() < mWindowWidth){
				mIVFingerRight.setVisibility(View.INVISIBLE);
			}else{
				if(x < 10){
					mIVFingerLeft.setVisibility(View.INVISIBLE);
					mIVFingerRight.setVisibility(View.VISIBLE);
				}else if (x >= (mLLBlutooths.getWidth() - mWindowWidth - 10)) {
					mIVFingerLeft.setVisibility(View.VISIBLE);
					mIVFingerRight.setVisibility(View.INVISIBLE);
				}else {
					mIVFingerLeft.setVisibility(View.VISIBLE);
					mIVFingerRight.setVisibility(View.VISIBLE);
				}
			}
		}else if(mHSVApps == scrollView){
			if(mLLApps.getWidth() < mWindowWidth){
				mIVMoreAppRight.setVisibility(View.INVISIBLE);
			}else{
				if(x < 10){
					mIVMoreAppLeft.setVisibility(View.INVISIBLE);
					mIVMoreAppRight.setVisibility(View.VISIBLE);
				}else if (x >= (mLLApps.getWidth() - mWindowWidth - 10)) {
					mIVMoreAppLeft.setVisibility(View.VISIBLE);
					mIVMoreAppRight.setVisibility(View.INVISIBLE);
				}else {
					mIVMoreAppLeft.setVisibility(View.VISIBLE);
					mIVMoreAppRight.setVisibility(View.VISIBLE);
				}
			}
		}
	}
	
	private void scrollBlutoothsToBegin(){
		mHSVBlutooths.scrollTo(0, 0);
	}
	
	private void scrollBlutoothsToEnd(){
		mHSVBlutooths.scrollTo(mLLBlutooths.getWidth() - mWindowWidth, 0);
	}

	private void scrollAppToBegin(){
		mHSVApps.scrollTo(0, 0);
	}
	
	private void scrollAppToEnd(){
		mHSVApps.scrollTo(mLLApps.getWidth() - mWindowWidth, 0);
	}
}
