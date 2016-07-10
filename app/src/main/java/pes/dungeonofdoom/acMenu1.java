package pes.dungeonofdoom;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * экран главного меню
 */
public class acMenu1 extends AppCompatActivity implements View.OnTouchListener{

    public static final String TAG = "myTag";
    public static final String FLAG = "Menu"; //метка экрана

    DrawView drawView;
    RectF[] itemCoords; //массив с координатами кнопок меню

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "launch acMenu1 onCreate");
        super.onCreate(savedInstanceState);
        //в метод onCreate передаём объект DrawView
        drawView = new DrawView(acMenu1.this);
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
                //создание персонажа
                //нажатие на кнопку вызовет диалог ввода имени
                AlertDialog.Builder bld = new AlertDialog.Builder(acMenu1.this);
                final LinearLayout view = (LinearLayout) getLayoutInflater()
                        .inflate(R.layout.dlg_inputname, null);
                bld.setTitle(getResources().getString(R.string.input_name))
                        .setView(view)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText etInput = (EditText) view.findViewById(R.id.etInput);
                                String name = etInput.getText().toString();
                                //если имя введено верно, переход на экран характеристик
                                if (name.length()>0&&name.length()<=40) {
                                    Intent it = new Intent(acMenu1.this, acChar1.class);
                                    it.putExtra("name", name);
                                    startActivity(it);
                                } else {
                                    Toast.makeText(acMenu1.this, "Неверный ввод", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setCancelable(true);
                AlertDialog ad = bld.create();
                ad.show();
            } else if (itemCoords[1].contains(event.getX(), event.getY())) {
                Log.d(TAG, "press Continue"/* + event.getX() + " " + event.getY() + " "
                        + itemCoords[1].toString()*/);
                //нажатие на кнопку загрузит сохранённую игру
            } else if (itemCoords[2].contains(event.getX(), event.getY())) {
                Log.d(TAG, "press Instructions"/* + event.getX() + " " + event.getY() + " "
                        + itemCoords[2].toString()*/);
                //нажатие на кнопку покажет экран инструкций
            } else if (itemCoords[3].contains(event.getX(), event.getY())) {
                Log.d(TAG, "press Quit"/* + event.getX() + " " + event.getY() + " "
                        + itemCoords[3].toString()*/);
                //нажатие на кнопку завершит программу
            }
        }
        return true;
    }

    private void setItemCoords (SurfaceHolder holder) {
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
                        DrawUtils.drawBGround(acMenu1.this, canvas, R.drawable.menu, FLAG);
                        DrawUtils.drawMenuItems(acMenu1.this, canvas, itemCoords);
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
