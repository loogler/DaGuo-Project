package com.daguo.modem.photo;

/*******************************************************************************
 *版本探测  基类 
 *
 *******************************************************************************/

import android.content.Context;
import android.os.Build;

public final class Util_GestureDetector_Versioned {

    public static Util_GestureDetector newInstance(Context context,
                                              OnGestureListener listener) {
        final int sdkVersion = Build.VERSION.SDK_INT;
        Util_GestureDetector detector;

        if (sdkVersion < Build.VERSION_CODES.ECLAIR) {
            detector = new Util_GestureDetector_Cupcake(context);
        } else if (sdkVersion < Build.VERSION_CODES.FROYO) {
            detector = new Util_GestureDetector_Eclair(context);
        } else {
            detector = new Util_GestureDetector_Froyo(context);
        }

        detector.setOnGestureListener(listener);

        return detector;
    }

}