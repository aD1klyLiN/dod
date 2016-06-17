package pes.dungeonofdoom;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * вспомогательные методы для отрисовки
 */
public class DrawUtils {

    /**
     * метод рисует на экране фон
     * @param ctx - контекст
     * @param canv - канва, на которой нужно рисовать
     * @param resID - ID картинки
     * @param flag - метка экрана
     */
    public static void drawBGround(Context ctx, Canvas canv, int resID, String flag) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //получим размеры канвы
        int cHeight = canv.getHeight();
        int cWidth = canv.getWidth();
        //в bitmap загружаем нужную картинку, подогнанную под канву
        Bitmap bitmap = decodeSampledBitmapFromResource(ctx.getResources(),
                resID, cWidth, cHeight);
        //получим размеры картинки
        int bWidth = bitmap.getWidth();
        int bHeight = bitmap.getHeight();
        Rect rectSrc, rectDst;
        //выберем способ отображения картинки в зависимости от
        //метки экрана
        switch (flag) {
            case (acIntro.FLAG):
                //для вступительного экрана картинка отобразится с сохранением
                //пропорций
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
                rectSrc = new Rect(0, 0, bWidth, bHeight);
                rectDst = new Rect(x1, y1, x2, y2);
                    /*Rect rectDst = new Rect((w-bitmap.getWidth())/2, (h-bitmap.getHeight())/2,
                            (w-bitmap.getWidth())/2+bitmap.getWidth(),
                            (h-bitmap.getHeight())/2+bitmap.getHeight());*/
                canv.drawARGB(100, 0, 0, 0);
                break;
            case (acMenu.FLAG):
            case (acChar.FLAG):
                //для экранов:
                //- меню
                //- характеристики персонажа
                //картинка будет растянута на весь экран
                rectSrc = new Rect(0, 0, bWidth, bHeight);
                rectDst = new Rect(0, 0, cWidth, cHeight);
                break;
            default:
                rectSrc = new Rect(0, 0, bWidth, bHeight);
                rectDst = new Rect(0, 0, cWidth, cHeight);
                break;
        }

        //выведем bitmap с картинкой на канву
        canv.drawBitmap(bitmap, rectSrc, rectDst, paint);
    }

    /**
     * метод создаёт bitmap с подогнанной под размеры экрана картинкой
     * @param res - источник ресурсов
     * @param resId - ID картинки
     * @param reqWidth - требуемая ширина
     * @param reqHeight - требуемая высота
     * @return - готовый bitmap с картинкой
     */
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

    /**
     * метод считает степень сжатия картинки
     * @param options - параметры загрузки bitmap
     * @param reqWidth - требуемая ширина
     * @param reqHeight - требуемая высота
     * @return - готовый коэффициент сжатия
     */
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

    /**
     * метод рисует пункты меню на экране главного меню
     * @param ctx - контекст
     * @param canv - канва
     * @param itemCoords - массив координат пунктов меню
     */
    static void drawMenuItems (Context ctx, Canvas canv, RectF[] itemCoords) {

        //задаём кисть
        Paint paint = new Paint();
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(28);
        //получаем названия пунктов меню из ресурсов
        String[] item = ctx.getResources().getStringArray(R.array.menuItems);

        for (int i = 0; i < item.length; i++) {
            //Log.d(TAG, "item 1 is " + itemCoords[i].toString());
            paint.setColor(ctx.getResources().getColor(R.color.button));
            //рисуем кнопку
            canv.drawRoundRect(itemCoords[i], 30, 10, paint);
            //рисуем текст на кнопке
            Utils.shadowText(canv, paint, itemCoords[i], item[i]);
        }

    }

    /**
     * метод рисует характеристики персонажа и кнопки управления
     * на экране характеристик
     * @param ctx - контекст
     * @param canv - канва
     * @param itemCoords - координаты кнопок управления
     * @param charc - отображаемый персонаж
     */
    static void drawCharItems (Context ctx, Canvas canv, RectF[] itemCoords, Charc charc) {

        //подготовим кисть
        Paint paint = new Paint();
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(28);
        //получим названия кнопок управления из ресурсов
        String[] item = ctx.getResources().getStringArray(R.array.char_items);

        for (int i = 0; i < item.length; i++) {
            //Log.d(TAG, "item 1 is " + itemCoords[i].toString());
            paint.setColor(ctx.getResources().getColor(R.color.button));
            //рисуем кнопку
            canv.drawRoundRect(itemCoords[i], 30, 10, paint);
            //рисуем текст на кнопке
            Utils.shadowText(canv, paint, itemCoords[i], item[i]);
        }

        //получаем размеры канвы
        int w = canv.getWidth();
        int h = canv.getHeight();
        //рисуем заголовок с именем персонажа
        Utils.shadowText(canv, paint, (float)(0.5*w), (float)(0.1413*h), charc.getCname());
        //меняем кисть
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(22);
        //получаем названия характеристик из ресурсов
        String[] attrNames = ctx.getResources().getStringArray(R.array.char_attrs);
        //получаем значения характеристик персонажа
        String[] attrs = charc.printStats();
        //вспомогательные величины для размещения колонок с характеристиками
        float x = (float)(0.15*w);
        float y = (float)(0.2451*h);

        for (int i=0; i<=5; i++) {
            //левая колонка - статы
            Utils.shadowText(canv, paint, x, y, attrNames[i]);
            Utils.shadowText(canv, paint, (float)(x+0.2*w), y, attrs[i]);
            y = (float)(y + 0.0519*h);
        }

        y = (float)(0.2451*h);

        for (int i=6; i<=11; i++) {
            //правая колонка - скиллы
            Utils.shadowText(canv, paint, (float)(x+0.4*w), y, attrNames[i]);
            Utils.shadowText(canv, paint, (float)(x+0.7*w), y, attrs[i]);
            y = (float)(y + 0.0519*h);
        }

        y = (float)(y + 0.0729*h);

        for (int i=12; i<=13; i++) {
            //подвал - опыт и золото
            Utils.shadowText(canv, paint, (float)(0.5*w), y, attrNames[i]);
            Utils.shadowText(canv, paint, (float)(0.7*w), y, attrs[i]);
            y = (float)(y + 0.0519*h);
        }

    }

}
