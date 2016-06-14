package pes.dungeonofdoom;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by lylin on 12.06.16.
 */
public class Utils {
    public static int rnd(int min, int max) {
        return (int)((max - min + 1)*Math.random() + min);
    }

    public static void shadowText(Canvas canv, Paint paint, RectF rectF, String text) {
        paint.setColor(Color.DKGRAY);
        canv.drawText(text, rectF.centerX() + 1,
                rectF.centerY() + 1 + paint.getTextSize() / 2, paint);
        paint.setColor(Color.WHITE);
        canv.drawText(text, rectF.centerX() - 1,
                rectF.centerY() - 1 + paint.getTextSize()/2, paint);
    }
}
