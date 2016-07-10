package pes.dungeonofdoom;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by lylin on 27.06.16.
 */
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
                //DrawUtils.drawBGround(acIntro1.this, canvas, R.drawable.title, FLAG);
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
