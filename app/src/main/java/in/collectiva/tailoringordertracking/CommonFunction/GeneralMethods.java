package in.collectiva.tailoringordertracking.CommonFunction;

import android.Manifest;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.view.Display;
import android.widget.ImageView;

import in.collectiva.tailoringordertracking.HomeMenu;
import in.collectiva.tailoringordertracking.R;

/**
 * Created by dhivya on 27/07/2017.
 */

public class GeneralMethods {
    public static void ResizeImage(ImageView imgLoginHeader,int lDrawableId, Display display, Resources res)
    {
        int displayWidth = display.getWidth();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, lDrawableId, options);
        int width = options.outWidth;
        if (width > displayWidth) {
            int widthRatio = Math.round((float) width / (float) displayWidth);
            options.inSampleSize = widthRatio;
        }
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeResource(res, lDrawableId, options);
        imgLoginHeader.setImageBitmap(bitmap);
    }

    public static void fnSendSMS(String MobileNo, String Msg)
    {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(MobileNo, null, Msg, null, null);
        smsManager = null;
    }
}
