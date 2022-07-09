package iss.workshop.gridlayoutsample;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PopUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_window);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * 0.7), (int)(height * 0.3));

        Intent intent = getIntent();
        String endTime = intent.getStringExtra("endTime").toString();
        TextView text = findViewById(R.id.EndTime);
        text.setText(endTime);

        Button btn1 = findViewById(R.id.EndReturn);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(PopUp.this, MainActivity.class));
            }
        });

        Button btn2 = findViewById(R.id.PlayAgain);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PopUp.this, GameActivity.class);
                startActivity(intent);
            }
        });
    }
}