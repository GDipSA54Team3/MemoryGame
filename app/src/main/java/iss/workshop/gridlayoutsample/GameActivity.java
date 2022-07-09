package iss.workshop.gridlayoutsample;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameActivity extends AppCompatActivity {

    ImageView imgView = null;
    int matchCount = 0;
    int posIndex = -1;
    Handler handler = new Handler();

    private Boolean lastImgIsFaceUp;
    private Boolean clickable;
    private int lastImgId;
    private ImageView lastClicked;
    private int matchedSets;
    private List<ImageView> matchedViews;
    private int seconds;
    private int minutes;
    private Boolean started;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get the images pass from main via intent
        Intent intent = getIntent();
        int[] gridImages = intent.getIntArrayExtra("images");
        //shuffle the incoming images in the array
        //shuffleArray(gridImages);
        //shuffle the positions
        int[] pos_ = {0, 1, 2, 3, 4, 5};

        List<Integer> pos = new ArrayList<Integer>();
        for (int p : pos_) {
            pos.add(p);
            pos.add(p);
        }

        Collections.shuffle(pos);

        startSettings();

        matchedViews = new ArrayList<ImageView>() {
        };

        setContentView(R.layout.activity_game);
        ImageAdapter imageAdapter = new ImageAdapter(this);
        GridView gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(imageAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageView im = (ImageView) view;
                if (clickable && matchedSets < pos_.length && !matchedViews.contains(im)) {
                    im.setImageResource(gridImages[pos.get(position)]);
                    // first flip
                    if (!lastImgIsFaceUp) {
                        started = true;
                        lastImgIsFaceUp = true;
                        lastImgId = gridImages[pos.get(position)];
                        lastClicked = im;
                        clickable = true;
                        //if not match
                    } else if (gridImages[pos.get(position)] != lastImgId) {
                        clickable = false;
                        // timer until user can click again
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // overturn mismatched pair
                                im.setImageResource(R.drawable.closed);
                                lastClicked.setImageResource(R.drawable.closed);
                                lastImgIsFaceUp = false;
                                clickable = true;
                            }
                        }, 1000);
                        //if match
                    } else if (gridImages[pos.get(position)] == lastImgId && lastClicked != im) {
                        lastImgIsFaceUp = false;
                        matchedSets++;
                        TextView textScore = findViewById(R.id.textMatches);
                        @SuppressLint("DefaultLocale") String text = String.format(
                                "%d of %d matched", matchedSets, pos_.length);
                        textScore.setText(text);
                        clickable = true;
                        matchedViews.add(im);
                        matchedViews.add(lastClicked);
                        if (matchedSets == pos_.length) {
                            started = false;
                            Intent intent = new Intent(GameActivity.this, PopUp.class);
                            intent.putExtra("endTime", String.format(
                                            "%02d:%02d", minutes, seconds));
                            startActivity(intent);
                        }
                    }

                }
            }
        });

        Button btn = findViewById(R.id.Restart);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });

        runTimer();
    }

    private void startSettings() {
        lastImgIsFaceUp = false;
        clickable = true;
        matchedSets = 0;
        seconds = 0;
        minutes = 0;
        started = false;
    }

    // To shuffle the elements of the array
//    static void shuffleArray(int[] array) {
//        Random random = new Random();
//        for (int i = array.length - 1; i > 0; i--) {
//            // ing random index from 0 to i
//            int j = random.nextInt(i + 1);
//            // Swap array[i] with the element at random index
//            int temp = array[i];
//            array[i] = array[j];
//            array[j] = temp;
//        }
//    }

    private void runTimer() {
        TextView txtTime = findViewById(R.id.textTimer);
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (seconds == 60) {
                    seconds = 0;
                    minutes++;
                }

                @SuppressLint("DefaultLocale") String text = String.format(
                        "%02d:%02d", minutes, seconds);
                txtTime.setText(text);

                if (started)
                    seconds++;
                handler.postDelayed(this, 1000);
            }
        });
    }
}