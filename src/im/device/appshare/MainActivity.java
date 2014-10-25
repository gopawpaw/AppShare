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

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.gopawpaw.droidcore.activity.BaseActivity;

/**
 * 
 * @author LiJinHua
 * @modify 2014-10-22 下午10:40:15
 */
public class MainActivity extends BaseActivity {

	private LinearLayout mLLApps_a,mLLApps_b;
	private LinearLayout mLLChild;
	private LayoutInflater mLayoutInflater;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mLLApps_a = (LinearLayout) findViewById(R.id.ll_apps_a);
		mLLApps_b = (LinearLayout) findViewById(R.id.ll_apps_b);
		mLLChild = (LinearLayout) findViewById(R.id.ll_child);
		mLayoutInflater = LayoutInflater.from(this);
		
		for(int i=0;i<10;i++){
			mLLApps_a.addView(mLayoutInflater.inflate(R.layout.app_item1, null));
			mLLApps_a.addView(mLayoutInflater.inflate(R.layout.app_item2, null));
			mLLApps_a.addView(mLayoutInflater.inflate(R.layout.app_item3, null));
			
			mLLApps_b.addView(mLayoutInflater.inflate(R.layout.app_item2, null));
			mLLApps_b.addView(mLayoutInflater.inflate(R.layout.app_item1, null));
			mLLApps_b.addView(mLayoutInflater.inflate(R.layout.app_item3, null));
			
			mLLChild.addView(mLayoutInflater.inflate(R.layout.device_item1, null));
			mLLChild.addView(mLayoutInflater.inflate(R.layout.device_item2, null));
			mLLChild.addView(mLayoutInflater.inflate(R.layout.device_item3, null));
			mLLChild.addView(mLayoutInflater.inflate(R.layout.device_item4, null));
			
		}
		
		
		
	}
}
