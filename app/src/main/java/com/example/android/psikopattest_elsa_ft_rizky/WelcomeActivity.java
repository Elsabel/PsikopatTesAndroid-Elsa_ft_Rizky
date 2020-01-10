package com.example.android.psikopattest_elsa_ft_rizky;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        ImageView logoImage = findViewById(R.id.logo);
        logoImage.startAnimation(AnimationUtils.loadAnimation(this, R.anim.zoom_in));

        TextView welcomeText = findViewById(R.id.welcome_text);
        welcomeText.startAnimation(AnimationUtils.loadAnimation(this, R.anim.zoom_in));


        Button startButton = findViewById(R.id.start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
