package org.usc.csci571.newsapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import org.usc.csci571.newsapp.MainActivity;
import org.usc.csci571.newsapp.R;

public class ErrorActivity extends AppCompatActivity {
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.error);

        this.button = findViewById(R.id.redirect_to_home_page_button);
        button.setOnClickListener(onClickListener());
    }

    private View.OnClickListener onClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ErrorActivity.this, MainActivity.class);
                startActivity(intent);
            }
        };
    }
}
