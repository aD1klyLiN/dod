package pes.dungeonofdoom;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

/**
 * экран характеристик персонажа
 *
 */
public class acChar extends AppCompatActivity implements View.OnTouchListener{

    public static final String TAG = "myTag";
    public static final String FLAG = "Char"; //метка экрана

    DrawView drawView;
    RectF[] itemCoords; //координаты кнопок управления
    Charc charc; //новый персонаж
    Intent it; //intent с именем персонажа

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //в метод onCreate передаём объект DrawView
        drawView = new DrawView(this);
        drawView.setOnTouchListener(this);
        setContentView(drawView);
        Log.d(TAG, "create acChar");

        itemCoords = new RectF[2];
        //создадим нового персонажа
        charc = new Charc();

        //получим имя персонажа, введённое в диалоге меню
        it = getIntent();
        charc = charc.genCharacter(it.getStringExtra("name"));
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //обработаем нажатие на экран
        if (event.getAction()==MotionEvent.ACTION_DOWN) {
            if (itemCoords[0].contains(event.getX(), event.getY())) {
                Log.d(TAG, "press Start"/* + event.getX() + " " + event.getY() + " "
                        + itemCoords[0].toString()*/);
                //переход к началу игры
            } else if (itemCoords[1].contains(event.getX(), event.getY())) {
                Log.d(TAG, "press Reset"/* + event.getX() + " " + event.getY() + " "
                        + itemCoords[1].toString()*/);
                //сгенерировать характеристики по новой
                charc = charc.genCharacter(it.getStringExtra("name"));
            }
        }
        return true;
    }

    private void setItemCoords (SurfaceHolder holder) {
        // задаём координаты пунктов меню
        Canvas canv = holder.lockCanvas();
        int w = canv.getWidth();
        int h = canv.getHeight();
        itemCoords[0] = new RectF((float)(w*0.15), (float)(h*0.7625),
                (float)(w*0.45), (float)(h*0.8625));
        itemCoords[1] = new RectF((float)(w*0.55), (float)(h*0.7625),
                (float)(w*0.85), (float)(h*0.8625));
        holder.unlockCanvasAndPost(canv);
            /*Log.d(TAG, "Item coords " + itemCoords[0].toString() + " " + itemCoords[1].toString()
                    + " " + itemCoords[2].toString() + " " + itemCoords[3].toString());*/
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

            //установим координаты кнопок меню для метода onTouch
            setItemCoords(holder);

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
                        //методы для рисования
                        DrawUtils.drawBGround(acChar.this, canvas, R.drawable.back, FLAG);
                        DrawUtils.drawCharItems(acChar.this, canvas, itemCoords, charc);
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
