package pes.dungeonofdoom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by lylin on 09.06.16.
 */
public class DrawMenu {

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

    static float[] drawMenuItems (Context ctx, Canvas canv) {
        float[] itemCoords = new float[8];
        Paint paint = new Paint();
        int centerX = canv.getWidth()/2;
        int centerY = canv.getHeight()/2;

        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(24);

        RectF menuItem = new RectF(centerX, centerY, centerX + 400, centerY + 50);

        menuItem.offset(-200, -115);
        itemCoords[0] = menuItem.centerX();
        itemCoords[1] = menuItem.centerY();
        paint.setColor(Color.WHITE);
        canv.drawRect(menuItem, paint);
        paint.setColor(Color.BLACK);
        canv.drawText("Новая игра", menuItem.centerX(), menuItem.centerY(), paint);

        menuItem.offset(0, 60);
        itemCoords[2] = menuItem.centerX();
        itemCoords[3] = menuItem.centerY();
        paint.setColor(Color.WHITE);
        canv.drawRect(menuItem, paint);
        paint.setColor(Color.BLACK);
        canv.drawText("Продолжить", menuItem.centerX(), menuItem.centerY(), paint);

        menuItem.offset(0, 60);
        itemCoords[4] = menuItem.centerX();
        itemCoords[5] = menuItem.centerY();
        paint.setColor(Color.WHITE);
        canv.drawRect(menuItem, paint);
        paint.setColor(Color.BLACK);
        canv.drawText("Инструкции", menuItem.centerX(), menuItem.centerY(), paint);

        menuItem.offset(0, 60);
        itemCoords[6] = menuItem.centerX();
        itemCoords[7] = menuItem.centerY();
        paint.setColor(Color.WHITE);
        canv.drawRect(menuItem, paint);
        paint.setColor(Color.BLACK);
        canv.drawText("Выход", menuItem.centerX(), menuItem.centerY(), paint);

        return itemCoords;
    }
}
