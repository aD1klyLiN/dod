package pes.dungeonofdoom;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by lylin on 08.06.16.
 */
public class DrawBGround {

    static void drawBGround(Context ctx, Canvas canv, int resID) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        int cHeight = canv.getHeight();
        int cWidth = canv.getWidth();
        Bitmap bitmap = decodeSampledBitmapFromResource(ctx.getResources(),
                resID, cWidth, cHeight);
        int bWidth = bitmap.getWidth();
        int bHeight = bitmap.getHeight();
        int x1, y1, x2, y2;
        double sX = (double)cWidth/bWidth;
        double sY = (double)cHeight/bHeight;
        if (sX>sY) {
            x1 = (int)(cWidth-bWidth*sY)/2;
            y1 = 0;
            x2 = (int)(x1 + bWidth*sY);
            y2 = cHeight;
        } else {
            x1 = 0;
            y1 = (int)(cHeight-bHeight*sX)/2;
            x2 = cWidth;
            y2 = (int)(y1 + bHeight*sX);
        }
        Rect rectSrc = new Rect(0, 0, bWidth, bHeight);
        Rect rectDst = new Rect(x1, y1, x2, y2);
                /*Rect rectDst = new Rect((w-bitmap.getWidth())/2, (h-bitmap.getHeight())/2,
                        (w-bitmap.getWidth())/2+bitmap.getWidth(),
                        (h-bitmap.getHeight())/2+bitmap.getHeight());*/
        canv.drawARGB(100, 0, 0, 0);
        canv.drawBitmap(bitmap, rectSrc, rectDst, paint);
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                  int reqWidth, int reqHeight) {

        // Читаем с inJustDecodeBounds=true для определения размеров
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Вычисляем inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Читаем с использованием inSampleSize коэффициента
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Реальные размеры изображения
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Вычисляем наибольший inSampleSize, который будет кратным двум
            // и оставит полученные размеры больше, чем требуемые
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
