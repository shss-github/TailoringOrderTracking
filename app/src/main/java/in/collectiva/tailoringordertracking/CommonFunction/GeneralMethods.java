package in.collectiva.tailoringordertracking.CommonFunction;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.view.Display;
import android.widget.ImageView;

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
}
