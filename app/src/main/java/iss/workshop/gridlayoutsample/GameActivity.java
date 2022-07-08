package iss.workshop.gridlayoutsample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    ImageView imgView = null;
    int matchCount = 0;
    int posIndex = -1;
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get the images pass from main via intent
        Intent intent = getIntent();
        int[] gridImages = intent.getIntArrayExtra("images");
        //shuffle the incoming images in the array
        //shuffleArray(gridImages);
        //shuffle the positions
        int[] pos = {0,1,2,3,4,5,0,1,2,3,4,5};
        shuffleArray(pos);

        setContentView(R.layout.activity_game);
        ImageAdapter imageAdapter = new ImageAdapter(this);
        GridView gridView = (GridView)findViewById(R.id.gridView);
        gridView.setAdapter(imageAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (posIndex < 0 ) {
                    posIndex = position;
                    imgView = (ImageView) view;

                    ( (ImageView) view).setImageResource(R.drawable.half_open);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ((ImageView) view).setImageResource(gridImages[pos[position]]);
                        }
                    }, 200);
                }
                else {
                    if (posIndex == position) {

                        ((ImageView) view).setImageResource(R.drawable.half_open);

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ((ImageView) view).setImageResource(R.drawable.closed);
                            }
                        }, 200);

                    } else if (pos[posIndex] != pos[position]) {
                        imgView.setImageResource(R.drawable.closed);
                        Toast.makeText(GameActivity.this, "Does Not Match! Try again", Toast.LENGTH_LONG).show();
                    } else {
                        ((ImageView) view).setImageResource(gridImages[pos[position]]);
                        matchCount++;
                        if (matchCount == 6) {
                            Toast.makeText(GameActivity.this, "You Win!", Toast.LENGTH_LONG).show();
                        }
                    }
                    posIndex = -1;
                }
            }
        });
    }
    // To shuffle the elements of the array
    static void shuffleArray( int[] array)
    {
        Random random = new Random();
        for (int i = array.length-1; i > 0; i--) {
            // ing random index from 0 to i
            int j = random.nextInt(i+1);
            // Swap array[i] with the element at random index
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }
}