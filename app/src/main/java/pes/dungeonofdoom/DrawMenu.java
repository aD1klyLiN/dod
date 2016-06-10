package pes.dungeonofdoom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

/**
 * Created by lylin on 09.06.16.
 */
public class DrawMenu {

    public static final String TAG = "myTag";

    static void drawBGround(Context ctx, Canvas canv, int resID) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        int cHeight = canv.getHeight();
        int cWidth = canv.getWidth();
        Bitmap bitmap = DrawIntro.decodeSampledBitmapFromResource(ctx.getResources(),
                resID, cWidth, cHeight);
        int bWidth = bitmap.getWidth();
        int bHeight = bitmap.getHeight();
        Rect rectSrc = new Rect(0, 0, bWidth, bHeight);
        Rect rectDst = new Rect(0, 0, cWidth, cHeight);
        canv.drawBitmap(bitmap, rectSrc, rectDst, paint);
    }

    static void drawMenuItems (Context ctx, Canvas canv, RectF[] itemCoords) {

        Paint paint = new Paint();
        String coords;

        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(24);

        //Log.d(TAG, "item 1 is " + itemCoords[0].toString());
        paint.setColor(Color.WHITE);
        canv.drawRect(itemCoords[0], paint);
        paint.setColor(Color.BLACK);
        canv.drawText("Новая игра", itemCoords[0].centerX(),
                itemCoords[0].centerY() + paint.getTextSize()/2, paint);

        //Log.d(TAG, "item 2 is " + itemCoords[1].toString());
        paint.setColor(Color.WHITE);
        canv.drawRect(itemCoords[1], paint);
        paint.setColor(Color.BLACK);
        canv.drawText("Продолжить", itemCoords[1].centerX(),
                itemCoords[1].centerY() + paint.getTextSize()/2, paint);

        //Log.d(TAG, "item 3 is " + itemCoords[2].toString());
        paint.setColor(Color.WHITE);
        canv.drawRect(itemCoords[2], paint);
        paint.setColor(Color.BLACK);
        canv.drawText("Инструкции", itemCoords[2].centerX(),
                itemCoords[2].centerY() + paint.getTextSize()/2, paint);

        //Log.d(TAG, "item 4 is " + itemCoords[3].toString());
        paint.setColor(Color.WHITE);
        canv.drawRect(itemCoords[3], paint);
        paint.setColor(Color.BLACK);
        canv.drawText("Выход", itemCoords[3].centerX(),
                itemCoords[3].centerY() + paint.getTextSize()/2, paint);

    }
}
