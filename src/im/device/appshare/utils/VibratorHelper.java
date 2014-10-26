/**
 * 文件名: VibratorHelper.java
 * 版    权：  Copyright  LiJinHua  All Rights Reserved.
 * 描    述: 
 * 创建人: LiJinHua
 * 创建时间:  2014-10-25
 * 
 * 修改人：LiJinHua
 * 修改时间:2014-10-25 下午3:54:44
 * 修改内容：[修改内容]
 */
package im.device.appshare.utils;

import android.app.Activity;
import android.app.Service;
import android.os.Vibrator;

/**
 * 
 * @author LiJinHua
 * @modify 2014-10-25 下午3:54:44
 */
public class VibratorHelper {

	public static void Vibrate(final Activity activity, long milliseconds) { 
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE); 
        vib.vibrate(milliseconds); 
    } 
    public static void Vibrate(final Activity activity, long[] pattern,boolean isRepeat) { 
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE); 
        vib.vibrate(pattern, isRepeat ? 1 : -1); 
    } 

}
