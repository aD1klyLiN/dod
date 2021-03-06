package pes.dungeonofdoom;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

/**
 * вступительная заставка
 * закрывается нажатием на экран или кнопкой "Назад"
 */
public class acIntro extends AppCompatActivity implements View.OnTouchListener{

    public static final String TAG = "myTag";
    public static final String FLAG = "Intro"; //метка экрана

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //в метод onCreate передаём объект DrawView
        DrawView drawView = new DrawView(this);
        drawView.setOnTouchListener(this);
        setContentView(drawView);
        Log.d(TAG, "create acIntro");
    }

    /**
     * метод для закрытия вступления и вызова меню
     */
    public void startMenu() {
        Log.d(TAG, "create acMenu");
        Intent intent = new Intent(this, acMenu.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //обработаем нажатие на экран
        boolean action;
        if (event.getAction()==MotionEvent.ACTION_DOWN) {
            action = true;
            Log.d(TAG, "startMenu from onTouch");
            startMenu();
        } else {
            action = false;
        }
        return action;
    }

    @Override
    public void onBackPressed() {
        startMenu();
        super.onBackPressed();
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
                    /*AlertDialog.Builder bld = new AlertDialog.Builder(acIntro.this);
                    bld.setTitle("ERROR!!!")
                            .setCancelable(false);
                    AlertDialog ad = bld.create();
                    ad.show();*/
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
                        DrawUtils.drawBGround(acIntro.this, canvas, R.drawable.title, FLAG);
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
