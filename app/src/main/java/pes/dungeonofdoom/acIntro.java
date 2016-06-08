package pes.dungeonofdoom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class acIntro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //в метод onCreate передаём объект DrawView
        setContentView(new DrawView(this));
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
        class DrawThread extends Thread {
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
                        drawFigs(canvas);
                    } finally {
                        if (canvas != null) {
                            //после того, как нарисовали, что хотели, мы возвращаем
                            //канву объекту SurfaceHolder
                            surfaceHolder.unlockCanvasAndPost(canvas);
                        }
                    }
                }
            }

            public void drawFigs(Canvas canvas) {
                Paint p = new Paint();
                RectF rectf = new RectF(700,100,800,150);
                float[] points = new float[]{100,50,150,100,150,200,50,200,50,100};
                float[] points1 = new float[]{300,200,600,200,300,300,600,300,400,100,400,400,500,100,500,400};
                canvas.drawARGB(80, 102, 204, 255);

                p.setColor(Color.RED);
                p.setStrokeWidth(10);

                // рисуем точки их массива points
                canvas.drawPoints(points,p);

                // рисуем линии по точкам из массива points1
                canvas.drawLines(points1,p);

                // перенастраиваем кисть на зеленый цвет
                p.setColor(Color.GREEN);

                // рисуем закругленный прямоугольник по координатам из rectf
                // радиусы закругления = 20
                canvas.drawRoundRect(rectf, 20, 20, p);

                // смещаем коорднаты rectf на 150 вниз
                rectf.offset(0, 150);
                // рисуем овал внутри прямоугольника rectf
                canvas.drawOval(rectf, p);

                // смещаем rectf в (900,100) (левая верхняя точка)
                rectf.offsetTo(900, 100);
                // увеличиваем rectf по вертикали на 25 вниз и вверх
                rectf.inset(0, -25);
                // рисуем дугу внутри прямоугольника rectf
                // с началом в 90, и длиной 270
                // соединение крайних точек через центр
                canvas.drawArc(rectf, 90, 270, true, p);

                // смещаем коорднаты rectf на 150 вниз
                rectf.offset(0, 150);
                // рисуем дугу внутри прямоугольника rectf
                // с началом в 90, и длиной 270
                // соединение крайних точек напрямую
                canvas.drawArc(rectf, 90, 270, false, p);

                // перенастраиваем кисть на толщину линии = 3
                p.setStrokeWidth(3);
                // рисуем линию (150,450) - (150,600)
                canvas.drawLine(150, 450, 150, 600, p);

                // перенастраиваем кисть на синий цвет
                p.setColor(Color.BLUE);

                // настраиваем размер текста = 30
                p.setTextSize(30);
                // рисуем текст в точке (150,500)
                canvas.drawText("text left", 150, 500, p);

                // настраиваем выравнивание текста на центр
                p.setTextAlign(Paint.Align.CENTER);
                // рисуем текст в точке (150,525)
                canvas.drawText("text center", 150, 525, p);

                // настраиваем выравнивание текста на левое
                p.setTextAlign(Paint.Align.RIGHT);
                // рисуем текст в точке (150,550)
                canvas.drawText("text right", 150, 550, p);
            }
        }
    }


}
