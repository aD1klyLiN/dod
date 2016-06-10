package pes.dungeonofdoom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class acMenu extends AppCompatActivity implements View.OnTouchListener{

    public static final String TAG = "myTag";

    DrawView drawView;
    RectF[] itemCoords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "launch acMenu onCreate");
        super.onCreate(savedInstanceState);
        //в метод onCreate передаём объект DrawView
        drawView = new DrawView(acMenu.this);
        drawView.setOnTouchListener(this);
        setContentView(drawView);

        itemCoords = new RectF[4];
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction()==MotionEvent.ACTION_DOWN) {
            if (itemCoords[0].contains(event.getX(), event.getY())) {
                Log.d(TAG, "press New Game"/* + event.getX() + " " + event.getY() + " "
                        + itemCoords[0].toString()*/);
            } else if (itemCoords[1].contains(event.getX(), event.getY())) {
                Log.d(TAG, "press Continue"/* + event.getX() + " " + event.getY() + " "
                        + itemCoords[1].toString()*/);
            } else if (itemCoords[2].contains(event.getX(), event.getY())) {
                Log.d(TAG, "press Instructions"/* + event.getX() + " " + event.getY() + " "
                        + itemCoords[2].toString()*/);
            } else if (itemCoords[3].contains(event.getX(), event.getY())) {
                Log.d(TAG, "press Quit"/* + event.getX() + " " + event.getY() + " "
                        + itemCoords[3].toString()*/);
            }
        }
        return true;
    }

    RectF getRectF (float x, float y) {
        return new RectF(x-25, y-200, x+25, y+200);
    }

    class DrawView extends SurfaceView implements SurfaceHolder.Callback{

        private DrawThread drawThread;

        public DrawView(Context context) {
            super(context);
            Log.d(TAG, "create new DrawView");
            //получаем SurfaceHolder и сообщаем ему, что сами будем
            // обрабатывать его события
            getHolder().addCallback(this);
        }

        //событие - SurfaceView создан и готов к отображению информации
        @Override
        public void surfaceCreated(SurfaceHolder holder) {

            // задаём координаты пунктов меню
            Canvas canv = holder.lockCanvas();
            int centerX = canv.getWidth()/2;
            int centerY = canv.getHeight()/2;
            itemCoords[0] = new RectF(centerX - 200, centerY - 165,
                    centerX + 200, centerY - 105);
            itemCoords[1] = new RectF(centerX - 200, centerY - 75,
                    centerX + 200, centerY - 15);
            itemCoords[2] = new RectF(centerX - 200, centerY + 15,
                    centerX + 200, centerY + 75);
            itemCoords[3] = new RectF(centerX - 200, centerY + 105,
                    centerX + 200, centerY + 165);
            holder.unlockCanvasAndPost(canv);
            /*Log.d(TAG, "Item coords " + itemCoords[0].toString() + " " + itemCoords[1].toString()
                    + " " + itemCoords[2].toString() + " " + itemCoords[3].toString());*/

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
                Log.d(TAG, "starting DrawThread");
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
                        DrawMenu.drawBGround(acMenu.this, canvas, R.drawable.menu);
                        DrawMenu.drawMenuItems(acMenu.this, canvas, itemCoords);
                    } finally {
                        if (canvas != null) {
                            //после того, как нарисовали, что хотели, мы возвращаем
                            //канву объекту SurfaceHolder
                            surfaceHolder.unlockCanvasAndPost(canvas);
                        }
                    }
                }
            }
        }
    }
}
