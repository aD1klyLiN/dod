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

    DrawView drawView = new DrawView(acMenu.this);
    float[] itemCoords = new float[8];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "launch acMenu onCreate");
        super.onCreate(savedInstanceState);
        //в метод onCreate передаём объект DrawView
        drawView.setOnTouchListener(this);
        setContentView(drawView);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        RectF[] rectItems = new RectF[4];
        rectItems[0] = getRectF(itemCoords[0], itemCoords[1]);
        rectItems[1] = getRectF(itemCoords[2], itemCoords[3]);
        rectItems[2] = getRectF(itemCoords[4], itemCoords[5]);
        rectItems[3] = getRectF(itemCoords[6], itemCoords[6]);
        if (event.getAction()==MotionEvent.ACTION_DOWN) {
            if (rectItems[0].contains(event.getX(), event.getY())) {
                Log.d(TAG, "press New Game");
            } else if (rectItems[1].contains(event.getX(), event.getY())) {
                Log.d(TAG, "press Continue");
            } else if (rectItems[2].contains(event.getX(), event.getY())) {
                Log.d(TAG, "press Instructions");
            } else if (rectItems[3].contains(event.getX(), event.getY())) {
                Log.d(TAG, "press Quit");
            }
        }
        return false;
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
                        DrawMenu.drawBGround(acMenu.this, canvas, R.drawable.menu);
                        //itemCoords = DrawMenu.drawMenuItems(acMenu.this, canvas);
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
