/**
 * 文件名: HelpActivity.java
 * 版    权：  Copyright  LiJinHua  All Rights Reserved.
 * 描    述: 
 * 创建人: LiJinHua
 * 创建时间:  2014-11-30
 * 
 * 修改人：LiJinHua
 * 修改时间:2014-11-30 下午7:19:01
 * 修改内容：[修改内容]
 */
package im.device.appshare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.gopawpaw.droidcore.activity.BaseActivity;
import com.gopawpaw.droidcore.utils.CommonUtils;

/**
 * 
 * @author LiJinHua
 * @modify 2014-11-30 下午7:19:01
 */
public class LunchActivity extends BaseActivity implements OnClickListener{

	private LinearLayout llHelp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		llHelp = (LinearLayout) findViewById(R.id.ll_help);
		llHelp.setOnClickListener(this);
		
		SharedPreferences sp = CommonUtils.getSysShare(this, CommonUtils.SHARED_CONFIG_FILE_NAME);
		boolean isFirstLunch = sp.getBoolean("isFirstLunch", true);
		if(!isFirstLunch){
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			finish();
		}
	}
	
	@Override
	public void onClick(View v) {
		if(v == llHelp){
			SharedPreferences sp = CommonUtils.getSysShare(this, CommonUtils.SHARED_CONFIG_FILE_NAME);
			sp.edit().putBoolean("isFirstLunch", false).commit();
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			finish();
		}
	}

}
