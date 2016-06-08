package pes.dungeonofdoom;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class acIntro extends AppCompatActivity implements View.OnTouchListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //в метод onCreate передаём объект DrawView
        setContentView(new DrawView(this));
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    class DrawView extends SurfaceView implements SurfaceHolder.Callback{

        private DrawThread drawThread;

        public DrawView(Context context) {
            super(context);
            //получаем SurfaceHolder и сообщаем ему, что сами будем
            // обрабатывать его события
            getHolder().addCallback(this);
        }

        //событие - SurfaceView создан и готов к отображению информации
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            //создаем свой поток прорисовки, передаем ему SurfaceHolder
            drawThread = new DrawThread(getHolder());
            //ставим ему метку о том, что он может работать
            drawThread.setRunning(true);
            //стартуем его
            drawThread.start();
        }

        //событие - изменён формат или размер SurfaceView
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        //событие - вызывается перед тем, как SurfaceView будет уничтожен
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            boolean retry = true;
            //сообщаем потоку о том, что его работа должна быть прекращена
            drawThread.setRunning(false);
            //запускаем цикл, который ждет, пока не завершит работу
            // наш поток прорисовки
            while (retry) {
                try {
                    drawThread.join();
                    retry = false;
                } catch (InterruptedException e) {
                }
            }
        }

        //поток прорисовки, в нем и будет происходить рисование
        public class DrawThread extends Thread {
            private boolean running = false;
            private SurfaceHolder surfaceHolder;

            public DrawThread(SurfaceHolder surfaceHolder) {
                //в конструктор передаем SurfaceHolder, он нам нужен,
                // чтобы добраться до канвы
                this.surfaceHolder = surfaceHolder;
            }

            public void setRunning(boolean running) {
                //метод ставит метку работы, сообщающую потоку, можно ли работать
                this.running = running;
            }

            @Override
            public void run() {
                Canvas canvas;
                //цикл, который выполняется пока позволяет метка работы (running)
                while (running) {
                    //обнуляем переменную канвы
                    canvas = null;
                    try {
                        //от SurfaceHolder получаем канву методом lockCanvas
                        canvas = surfaceHolder.lockCanvas(null);
                        //проверяем, что канва не null, и можно рисовать
                        if (canvas == null)
                            continue;
                        //метод для рисования
                        drawIntro(canvas);
                    } finally {
                        if (canvas != null) {
                            //после того, как нарисовали, что хотели, мы возвращаем
                            //канву объекту SurfaceHolder
                            surfaceHolder.unlockCanvasAndPost(canvas);
                        }
                    }
                }
            }

            void drawIntro(Canvas canv) {
                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                int cHeight = canv.getHeight();
                int cWidth = canv.getWidth();
                Bitmap bitmap = decodeSampledBitmapFromResource(getResources(),
                        R.drawable.title, cWidth, cHeight);
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

            public Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
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

            public int calculateInSampleSize(
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
    }


}
