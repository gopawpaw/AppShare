/**
 * 文件名: BluetoothTransferActivity.java
 * 版    权：  Copyright  LiJinHua  All Rights Reserved.
 * 描    述: 
 * 创建人: LiJinHua
 * 创建时间:  2014-5-19
 * 
 * 修改人：LiJinHua
 * 修改时间:2014-5-19 下午6:30:50
 * 修改内容：[修改内容]
 */
package im.device.appshare;

import im.device.appshare.widget.CircleProgresBar;

import java.text.DecimalFormat;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pingan.core.bluetooth.opp.BluetoothOppShareInfo;
import com.pingan.core.bluetooth.share.BluetoothOppObexShare;
import com.pingan.core.bluetooth.share.OppObexShareCallback;
import com.pingan.core.bluetooth.share.OppObexShareCallback.StatusType;

/**
 * 蓝牙传输页面
 * @author LiJinHua
 * @modify 2014-5-19 下午6:30:50
 */
public class BluetoothTransferActivity extends Activity implements OnClickListener{

	private static final String TAG = BluetoothTransferActivity.class.getSimpleName();
	
	public static void actionStart(Context context, PackageInfo appInfo,BluetoothDevice device) {
		Intent intent = new Intent(context, BluetoothTransferActivity.class);
		intent.putExtra("appInfo", appInfo);
		intent.putExtra("device", device);
		context.startActivity(intent);
	}

	private static final int HANDLER_WHAT_ONOPPOBEXSHARESTATUS = 2;
	
	private PackageInfo appInfo;
	private BluetoothDevice device;
	private String apkPath;
	private String appName;
	private int iconResId;
	
	private TextView mTextViewName;
	private CircleProgresBar mCircleProgresBar;
	private ImageView mIcon;
	private ImageView mClose;
	private TextView mTVFilesize,mTVSendsize,mTVTranspeed,mTVNeedtime;
	private int mProgressTotal = 0;
	private long mStartTime = 0;
	private long mCurrentTime = 0;
	private boolean mBluetoothConnected = false;
	private boolean mIsColse = false;
	private DecimalFormat mDecimalFormat = new DecimalFormat("##0.00");
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bluetooth_transfer);
		appInfo = getIntent().getExtras().getParcelable("appInfo");
		device = getIntent().getExtras().getParcelable("device");
		iconResId  = getIntent().getExtras().getInt("iconResId");
		
		apkPath = appInfo.applicationInfo.sourceDir;
		appName = appInfo.applicationInfo.loadLabel(getPackageManager())
		.toString();
		
		mTextViewName = (TextView) findViewById(R.id.name);
		mCircleProgresBar = (CircleProgresBar) findViewById(R.id.progress);
		mClose = (ImageView) findViewById(R.id.close);
		mIcon = (ImageView) findViewById(R.id.icon);
		mTVFilesize = (TextView) findViewById(R.id.tv_filesize);
		mTVSendsize = (TextView) findViewById(R.id.tv_sendsize);
		mTVTranspeed = (TextView) findViewById(R.id.tv_transpeed);
		mTVNeedtime = (TextView) findViewById(R.id.tv_needtime);
		
		mClose.setOnClickListener(this);
		if(device == null){
			return;
		}
		String name = device.getName();
		name = TextUtils.isEmpty(name) ? device.getAddress() : name;
		mTextViewName.setText("正在发送:《"+appName+"》 给 《"+name+"》");
		
		mCircleProgresBar.setProgress(0);
		mTVFilesize.setText("等待对方接受...");
		if(iconResId >0){
			mIcon.setImageResource(iconResId);
		}
		mBluetoothConnected =  false;
		sendToBluetoothDevice(device);
	}
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case HANDLER_WHAT_ONOPPOBEXSHARESTATUS:
				Object[] objs = (Object[]) msg.obj;
				onOppObexShareStatusUI((OppObexShareCallback.StatusType)objs[0],msg.arg1,(BluetoothOppShareInfo)objs[1]);
				break;
			default:
				break;
			}
		}
	};
	
	private void sendToBluetoothDevice(BluetoothDevice device) {
		AppLog.i(TAG, "sendToBluetoothDevice : " + device.getName() + "  :  "
				+ device.getAddress());
		mBluetoothOppObexShare = BluetoothOppObexShare.Factory.create(getApplicationContext(), device, mOppObexShareCallback).connect();
	}
	private BluetoothOppObexShare mBluetoothOppObexShare;
	private OppObexShareCallback mOppObexShareCallback = new OppObexShareCallback(){

		@Override
		public void onOppObexShareStatus(StatusType type, int value,
				BluetoothOppShareInfo shareInfo) {
			Message msg = new Message();
			msg.what = HANDLER_WHAT_ONOPPOBEXSHARESTATUS;
			msg.arg1 = value;
			msg.obj = new Object[]{type,shareInfo};
			mHandler.sendMessage(msg);
		}
	};
	
	private void onOppObexShareStatusUI(OppObexShareCallback.StatusType type, int value,
			BluetoothOppShareInfo shareInfo){
		if(mIsColse){
			return;
		}
		if(type == StatusType.onConnect){
			mBluetoothConnected = true;
			BluetoothOppShareInfo share = new BluetoothOppShareInfo(apkPath,"appshare-"+appName+".apk");
			mBluetoothOppObexShare.shareFile(share);
			Toast.makeText(BluetoothTransferActivity.this, "蓝牙连接建立成功！",
					Toast.LENGTH_SHORT).show();
		}else if(type == StatusType.onDisconnect){
			Toast.makeText(BluetoothTransferActivity.this, "蓝牙连接断开",
					Toast.LENGTH_SHORT).show();
		}else if(type == StatusType.onShareFailed){
			if(BluetoothOppShareInfo.STATUS_FORBIDDEN == value){
				Toast.makeText(BluetoothTransferActivity.this, "对方拒绝您的分享！",
						Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(BluetoothTransferActivity.this, "分享失败:"+value,
						Toast.LENGTH_SHORT).show();
			}
		}else if(type == StatusType.onShareSuccess){
			Toast.makeText(BluetoothTransferActivity.this, "分享成功",
					Toast.LENGTH_SHORT).show();
			mCircleProgresBar.setProgress(100);
			if(mBluetoothOppObexShare != null){
				mBluetoothOppObexShare.disconnect();
			}
			finish();
		}else if(type == StatusType.onShareTimeout){
			Toast.makeText(BluetoothTransferActivity.this, "分享超时",
					Toast.LENGTH_SHORT).show();
		}else if(type == StatusType.onTransferStart){
			mProgressTotal = value;
			mStartTime = System.currentTimeMillis();
		}else if(type == StatusType.onTransferProgress){
			int p = value;
			mCurrentTime = System.currentTimeMillis();
			
			long time = mCurrentTime - mStartTime;
			float byteBySecond = (float)p/(float)time;
			long needTime = (long) ((mProgressTotal-p)/byteBySecond);
			int tiemSecond = (int) (needTime/1000);
			if(tiemSecond<0){
				tiemSecond = 0;
			}
			
			float proFloat = (float)p /(float)mProgressTotal;
			float pro = (float) (proFloat * 100);
			pro = Float.valueOf(mDecimalFormat.format(pro));
			
			mCircleProgresBar.setProgress(pro,pro+"%");
			mTVFilesize.setText("文件大小："+(mProgressTotal/1024)+"kb");
			mTVSendsize.setText("已经发送："+(p/1024)+"kb");
			mTVTranspeed.setText("传输速度："+((p/1024)/(time/1000))+"kb/s");
			if(tiemSecond>60){
				int m = tiemSecond / 60;
				int s = tiemSecond % 60;
				mTVNeedtime.setText("剩余时间："+m+"分"+s+"秒");
			}else{
				mTVNeedtime.setText("剩余时间："+tiemSecond+"秒");
			}
		}
	}

	@Override
	public void onClick(View v) {
		mIsColse = true;
		if(mBluetoothOppObexShare != null && mBluetoothConnected){
			new Thread(){
				@Override
				public void run() {
					mBluetoothOppObexShare.disconnect();
				}
			}.start();
		}
		finish();
	}
}
