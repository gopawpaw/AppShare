/**
 * 
 */
package im.device.appshare.utils;

import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

/**
 * @author jinhua
 *
 */
public class AnimationUtils {

	public static TranslateAnimation getAppAnimation(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta,long m) {
		TranslateAnimation alphaAnimation2 = new TranslateAnimation(fromXDelta, toXDelta, fromYDelta, toYDelta);  
		alphaAnimation2.setDuration(m);  
		alphaAnimation2.setRepeatCount(Animation.INFINITE);  
		alphaAnimation2.setRepeatMode(Animation.REVERSE);
		return alphaAnimation2;
	}
	
	public static  TranslateAnimation getAppRadomAnimation(){
		int n = (int)(Math.random()*10);
		TranslateAnimation anima = null;
		switch (n) {
		case 0:
			anima = AnimationUtils.getAppAnimation(0, 50, -40, 0,10000);
			break;
		case 1:
			anima = AnimationUtils.getAppAnimation(10, 20, -10, 0,15000);
			break;
		case 2:
			anima = AnimationUtils.getAppAnimation(30, 40, -30, 0,10000);
			break;
		case 3:
			anima = AnimationUtils.getAppAnimation(0, 10, -20, 0,15000);
			break;
		case 4:
			anima = AnimationUtils.getAppAnimation(40, 0, -20, 0,8000);
			break;
		case 5:
			anima = AnimationUtils.getAppAnimation(30, 10, -50, 0,9000);
			break;
		case 6:
			anima = AnimationUtils.getAppAnimation(0, 0, 0, -30,11000);
			break;
		case 7:
			anima = AnimationUtils.getAppAnimation(0, 0, 0, -30,13000);
			break;
		case 8:
			anima = AnimationUtils.getAppAnimation(0, 30, 0, -20,14000);
			break;
		case 9:
			anima = AnimationUtils.getAppAnimation(40, 20, 0, -50,12000);
			break;
		default:
			anima = AnimationUtils.getAppAnimation(0, 10, -30, 0,10000);
			break;
		}
		return anima;
	}
}
