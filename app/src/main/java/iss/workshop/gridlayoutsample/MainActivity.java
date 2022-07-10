package iss.workshop.gridlayoutsample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private GridViewAdapter gridAdapter;
    private Button findLink;
    private EditText urlText;
    private String urlString;
    private ArrayList<String> sourceOfImages;
    private ArrayList<String> fileNames;
    //private final String IMAGE_DESTINATION_FOLDER = "C://images";

    private Button nextPage;

    private ArrayList<Integer> selectedImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sourceOfImages = new ArrayList<String>();
        fileNames = new ArrayList<String>();
        selectedImages = new ArrayList<Integer>();

        findLink = findViewById(R.id.submit_btn);
        urlText = (EditText) findViewById(R.id.url_input);

        nextPage = findViewById(R.id.next_page_btn);


        findLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                urlString = urlText.getText().toString();
                new FindImageInURLLink().execute();

                //able to get images now
                for (String imgUrl : sourceOfImages){
                    String destFilename = UUID.randomUUID().toString() + imgUrl.lastIndexOf(".") + 1;
                    //File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    //File destFile = new File(dir, destFilename);
                    fileNames.add(destFilename);
                    //downloadImage(imgUrl, files_names);
                }

                startDownloadService(sourceOfImages, fileNames);
            }
        });


        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGame();
            }
        });


        //find gridview
        GridView gridView = (GridView) findViewById(R.id.grid_view);

        //this is where you fetch the data (after scanning the website)
        // getData is initial dataset, will remove once getting items from website works
        gridAdapter = new GridViewAdapter(this, R.layout.cell, getData());

        //set adapter of gridview to the array of images
        gridView.setAdapter(gridAdapter);

        //setting action when clicked
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //click one, to select/remove from selection
                //if content is already 6, nothing happens
                ImageItem item = (ImageItem) adapterView.getItemAtPosition(i);
                LinearLayout lin = view.findViewById(R.id.bgcol);

                if (!item.isCurrentlySelected()){
                    lin.setBackgroundColor(getResources().getColor(R.color.red));
                    System.out.println(item.getDrawableTag());
                    selectedImages.add(item.getDrawableTag());

                    item.changeSelectedState();
                    //add to 6
                } else {
                    lin.setBackgroundColor(getResources().getColor(R.color.white));
                    System.out.println(item.getDrawableTag());
                    selectedImages.remove(item.getDrawableTag());

                    item.changeSelectedState();
                    //remove from 6
                }

                //if content is already 6, enable next page button

            }
        });
    }

    // This is where we save the image into an array
    private ArrayList<ImageItem> getData() {

        //take into account when populating less than 20
        //initialize array list
        final ArrayList<ImageItem> imageItems = new ArrayList<>();
        //getting ids from the R.string source
        TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);
        for (int i = 0; i < imgs.length(); i++) {

            //decoding bitmap from external storage
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs.getResourceId(i, -1));


            //set image to produced bitmap
            ImageItem imgItem = new ImageItem(bitmap);
            imgItem.setDrawableTag(imgs.getResourceId(i, -1));
            imageItems.add(imgItem);
        }
        return imageItems;
    }



    //starting the game
    public void startGame() {

        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        intent.putExtra("images", selectedImages);
        startActivity(intent);
    }

    private static void downloadImage(String strImageURL){
        //should save in the res.drawable folder
    }

    //jsoup implementation class
    public class FindImageInURLLink extends AsyncTask<Void, Void, Void>{
        ArrayList<String> images;

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Document doc = Jsoup.connect(urlString).get();

                Elements elems = doc.getElementsByTag("img");
                //Toast.makeText(MainActivity.this, "hello", Toast.LENGTH_SHORT).show();
                images = new ArrayList<String>();

                int counter = 0;
                for(Element elem : elems){
                    if (counter < 20){
                        if (elem.attr("src").startsWith("https://")) {
                            images.add(elem.attr("src"));
                            counter ++;
                        }
                    } else {
                        break;
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            sourceOfImages = images;
        }
    }


    protected void startDownloadService(ArrayList<String> urls, ArrayList<String> filenames) {
        for (int i = 0; i < filenames.size(); i++) {
            Intent intent = new Intent(this, DownloadService.class);
            intent.setAction("download");
            intent.putExtra("filename", filenames.get(i));
            intent.putExtra("where", urls.get(i));
            startService(intent);
        }
    }
}