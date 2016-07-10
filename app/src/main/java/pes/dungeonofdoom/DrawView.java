package pes.dungeonofdoom;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by lylin on 27.06.16.
 */
public class DrawView extends SurfaceView implements SurfaceHolder.Callback{

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
                    /*AlertDialog.Builder bld = new AlertDialog.Builder(acIntro1.this);
                    bld.setTitle("ERROR!!!")
                            .setCancelable(false);
                    AlertDialog ad = bld.create();
                    ad.show();*/
            }
        }
    }
}
