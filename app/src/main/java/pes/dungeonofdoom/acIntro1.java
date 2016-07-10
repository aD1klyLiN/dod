package pes.dungeonofdoom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * вступительная заставка
 * закрывается нажатием на экран или кнопкой "Назад"
 */
public class acIntro1 extends AppCompatActivity implements View.OnTouchListener{

    public static final String TAG = "myTag";
    public static final String FLAG = "Intro"; //метка экрана

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //в метод onCreate передаём объект DrawView
        DrawView drawView = new DrawView(this);
        drawView.setOnTouchListener(this);
        setContentView(drawView);
        Log.d(TAG, "create acIntro1");
    }

    /**
     * метод для закрытия вступления и вызова меню
     */
    public void startMenu() {
        Log.d(TAG, "create acMenu1");
        Intent intent = new Intent(this, acMenu1.class);
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

}
