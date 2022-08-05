package com.example.emptyactivity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.app.StatusBarManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.webkit.ConsoleMessage;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.io.Console;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Boolean isSystemBarVisible = false;

    Button firstButton;
    EditText textInput;
    TextView textView;

    RecyclerView recyclerView;

    WindowInsetsControllerCompat windowInsetsControllerCompat;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        window.setStatusBarColor(Color.TRANSPARENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            params.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS;
        }


        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        windowInsetsControllerCompat = ViewCompat.getWindowInsetsController(getWindow().getDecorView());
        windowInsetsControllerCompat.setAppearanceLightStatusBars(true);

        Resources.Theme theme;
        setContentView(R.layout.activity_main);


        //windowInsetsControllerCompat.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_SWIPE);

        actionBar = getSupportActionBar();
        //actionBar.hide();


        if (actionBar != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

                //actionBar.setTitle(String.valueOf(setTranslucent(true)));

            }
        }

  /*      if (isSystemBarVisible) {
            windowInsetsControllerCompat.show(WindowInsetsCompat.Type.statusBars());
        } else {
            windowInsetsControllerCompat.hide(WindowInsetsCompat.Type.statusBars());
        }*/


        firstButton = findViewById(R.id.firstButton);
        textInput = findViewById(R.id.textInput);
        textView = findViewById(R.id.textView);
        firstButton.setOnClickListener(this);
        textInput.setVerticalScrollBarEnabled(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.firstButton:
                textView.setText(textInput.getText());

                isSystemBarVisible = !isSystemBarVisible;
                if (isSystemBarVisible) {
                    windowInsetsControllerCompat.show(WindowInsetsCompat.Type.statusBars());
                    actionBar.show();

                } else {
                    windowInsetsControllerCompat.hide(WindowInsetsCompat.Type.statusBars());
                    actionBar.hide();

                }
                break;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}