/**
 * 文件名: ObservableHorizontalScrollView.java
 * 版    权：  Copyright  LiJinHua  All Rights Reserved.
 * 描    述: 
 * 创建人: LiJinHua
 * 创建时间:  2014-11-8
 * 
 * 修改人：LiJinHua
 * 修改时间:2014-11-8 下午9:15:54
 * 修改内容：[修改内容]
 */
package im.device.appshare.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

/**
 * 
 * @author LiJinHua
 * @modify 2014-11-8 下午9:15:54
 */
public class ObservableHorizontalScrollView extends HorizontalScrollView {

	public interface ScrollViewListener{
		void onScrollChanged(ObservableHorizontalScrollView scrollView,int x,int y,int oldx,int oldy);
	}
	
	private ScrollViewListener scrollViewListener;
	
	/**
	 * @param context
	 */
	public ObservableHorizontalScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public ObservableHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public ObservableHorizontalScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		// TODO Auto-generated method stub
		super.onScrollChanged(l, t, oldl, oldt);
		if(scrollViewListener != null){
			scrollViewListener.onScrollChanged(this, l, t, oldl, oldt);
		}
	}

	public ScrollViewListener getScrollViewListener() {
		return scrollViewListener;
	}

	public void setScrollViewListener(ScrollViewListener scrollViewListener) {
		this.scrollViewListener = scrollViewListener;
	}
}
