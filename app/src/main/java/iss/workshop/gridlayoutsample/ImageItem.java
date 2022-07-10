package iss.workshop.gridlayoutsample;

import android.graphics.Bitmap;

public class ImageItem {
    private Bitmap image;
    private boolean currentlySelected;
    private Integer drawableTag;

    public ImageItem(Bitmap image) {
        super();
        this.image = image;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public boolean isCurrentlySelected() {
        return currentlySelected;
    }

    public void changeSelectedState(){
        if (currentlySelected){
            currentlySelected = false;
        } else {
            currentlySelected = true;
        }
    }

    public void setDrawableTag(int drawableTag) {
        this.drawableTag = drawableTag;
    }

    public Integer getDrawableTag() {
        return drawableTag;
    }
}