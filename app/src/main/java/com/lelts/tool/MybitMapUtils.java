package com.lelts.tool;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

public class MybitMapUtils {
	 /**
     * 
     * TODO<图片模糊化处理> 
     * @throw 
     * @return Bitmap 
     * @param bitmap 源图片
     * @param radius The radius of the blur Supported range 0 < radius <= 25
     * @param context 上下文
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint("NewApi")
    public static Bitmap blurBitmap(Bitmap bitmap,float radius,Context context){  
         
        //Let's create an empty bitmap with the same size of the bitmap we want to blur  
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);  
           
        //Instantiate a new Renderscript  
        RenderScript rs = RenderScript.create(context);  
           
        //Create an Intrinsic Blur Script using the Renderscript  
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));  
           
        //Create the Allocations (in/out) with the Renderscript and the in/out bitmaps  
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap);  
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);  
           
        //Set the radius of the blur  
        if(radius > 25){
            radius = 25.0f;
        }else if (radius <= 0){
            radius = 1.0f;
        }
        blurScript.setRadius(radius);  
           
        //Perform the Renderscript  
        blurScript.setInput(allIn);  
        blurScript.forEach(allOut);  
           
        //Copy the final bitmap created by the out Allocation to the outBitmap  
        allOut.copyTo(outBitmap);  
           
        //recycle the original bitmap  
        bitmap.recycle();  
        bitmap = null;
        //After finishing everything, we destroy the Renderscript.  
        rs.destroy();  
           
        return outBitmap;  
           
           
    }
}
