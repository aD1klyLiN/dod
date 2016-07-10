package pes.dungeonofdoom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

public abstract class acBase extends AppCompatActivity implements View.OnTouchListener{

    public DrawView drawView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drawView = setDrawView();
        drawView.setOnTouchListener(this);
        setContentView(drawView);
    }

    public abstract DrawView setDrawView();

    @Override
    public abstract boolean onTouch(View v, MotionEvent event);
}
