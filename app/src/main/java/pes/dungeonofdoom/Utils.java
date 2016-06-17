package pes.dungeonofdoom;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * вспомогательные методы
 */
public class Utils {

    /**
     * метод - генератор случайных чисел в заданном интервале
     * @param min - нижний предел
     * @param max - верхний предел
     * @return - случайное число в интервале [min, max]
     */
    public static int rnd(int min, int max) {
        return (int)((max - min + 1)*Math.random() + min);
    }

    /**
     * метод рисует текст с тенью в центре прямоугольной области
     * @param canv - канва
     * @param paint - кисть
     * @param rectF - координаты области
     * @param text - текст
     */
    public static void shadowText(Canvas canv, Paint paint, RectF rectF, String text) {
        paint.setColor(Color.DKGRAY);
        canv.drawText(text, rectF.centerX() + 1,
                rectF.centerY() + 1 + paint.getTextSize() / 2, paint);
        paint.setColor(Color.WHITE);
        canv.drawText(text, rectF.centerX() - 1,
                rectF.centerY() - 1 + paint.getTextSize()/2, paint);
    }

    /**
     * метод рисует текст с тенью в заданной точке
     * @param canv - канва
     * @param paint - кисть
     * @param x - координата Х
     * @param y - координата Y
     * @param text - текст
     */
    public static void shadowText(Canvas canv, Paint paint, float x, float y, String text) {
        paint.setColor(Color.DKGRAY);
        canv.drawText(text, x + 1,
                y + 1 + paint.getTextSize() / 2, paint);
        paint.setColor(Color.WHITE);
        canv.drawText(text, x - 1,
                y - 1 + paint.getTextSize()/2, paint);
    }
}
