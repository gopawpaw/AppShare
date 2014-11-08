/**
 * 
 */
package im.device.appshare.adapter;


import im.device.appshare.R;

import java.util.List;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AppHorizontalListViewAdapter extends BaseAdapter implements OnLongClickListener{
	private Context mContext;
	private LayoutInflater mInflater;
	private List<PackageInfo> mListData;
	private OnLongClickListener onLongClickAppListener;
	
	public AppHorizontalListViewAdapter(Context context, List<PackageInfo> listData){
		this.mContext = context;
		this.mListData = listData;
		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
	}
	@Override
	public int getCount() {
		if (mListData != null) {
			return mListData.size();
		}
		return 0;
	}
	@Override
	public PackageInfo getItem(int position) {
		if (mListData != null) {
			return mListData.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@TargetApi(Build.VERSION_CODES.FROYO)
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView==null){
			holder = new ViewHolder();
			convertView = createItemView(position);
			holder.mAppIcon=(ImageView)convertView.findViewById(R.id.iv_icon);
			holder.mAppName=(TextView)convertView.findViewById(R.id.tv_appname);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		
		PackageInfo packageInfo =getItem(position);
		String appName = packageInfo.applicationInfo.loadLabel(
				mContext.getPackageManager()).toString();
		
		Drawable appIcon = packageInfo.applicationInfo
				.loadIcon(mContext.getPackageManager());
		
		holder.mAppName.setText(appName);
		holder.mAppIcon.setImageDrawable(appIcon);
		
		Animation a = convertView.getAnimation();
		if(a != null){
			a.cancel();
		}
		convertView.setOnLongClickListener(this);
		convertView.startAnimation(im.device.appshare.utils.AnimationUtils.getAppRadomAnimation());
		return convertView;
	}

	private static class ViewHolder {
		private TextView mAppName ;
		private ImageView mAppIcon;
	}
	
	private View createItemView(int position){
		View view = null;
		int k = position % 3;
		if(k == 0){
			view = mInflater.inflate(R.layout.app_item1, null);
		}else if(k == 1){
			view = mInflater.inflate(R.layout.app_item2, null);
		}else if(k == 2){
			view = mInflater.inflate(R.layout.app_item3, null);
		}else{
			view = mInflater.inflate(R.layout.app_item1, null);
		}
		return view;
	}
	public void setData(List<PackageInfo> data) {
		this.mListData = data;
	}
	
	@Override
	public boolean onLongClick(View v) {
		boolean flag = false;
		if(onLongClickAppListener != null){
			flag = onLongClickAppListener.onLongClick(v);
		}
		return flag;
	}
	public OnLongClickListener getOnLongClickAppListener() {
		return onLongClickAppListener;
	}
	public void setOnLongClickAppListener(OnLongClickListener onLongClickAppListener) {
		this.onLongClickAppListener = onLongClickAppListener;
	}
	
}