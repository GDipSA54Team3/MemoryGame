package iss.workshop.gridlayoutsample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private GridViewAdapter gridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button findLink = findViewById(R.id.submit_btn);
        EditText urlText = findViewById(R.id.url_input);
        Button nextPage = findViewById(R.id.next_page_btn);

        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGame();
            }
        });


        //find gridview
        GridView gridView = (GridView) findViewById(R.id.grid_view);
        //this is where you fetch the data (after scanning the website)
        gridAdapter = new GridViewAdapter(this, R.layout.cell, getData());

        //set adapter of gridview to the array of images
        gridView.setAdapter(gridAdapter);

        //setting action when clicked
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //click one, to select/remove from selection
                //if content is already 6, nothing happens
                //if content is already 6, enable next page button
                ImageItem item = (ImageItem) adapterView.getItemAtPosition(i);
                Toast.makeText(MainActivity.this, item.getImage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Prepare some dummy data for gridview
    private ArrayList<ImageItem> getData() {
        final ArrayList<ImageItem> imageItems = new ArrayList<>();
        TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);
        for (int i = 0; i < imgs.length(); i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs.getResourceId(i, -1));
            imageItems.add(new ImageItem(bitmap));
        }
        return imageItems;
    }

    public void startGame() {

        int[] selectedImages = new int[] {
                R.drawable.image_1,
                R.drawable.image_2,
                R.drawable.image_3,
                R.drawable.image_4,
                R.drawable.image_5,
                R.drawable.image_6,
        };
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        intent.putExtra("images", selectedImages);
        startActivity(intent);
    }
}