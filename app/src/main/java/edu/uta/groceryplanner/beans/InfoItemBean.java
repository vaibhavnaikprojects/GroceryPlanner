package edu.uta.groceryplanner.beans;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InfoItemBean {
    public int imageViewId;
    public String textViewTitle;

    public InfoItemBean(int imageViewId, String textViewTitle) {
        this.imageViewId = imageViewId;
        this.textViewTitle = textViewTitle;
    }
    public int getImageViewId() {
        return imageViewId;
    }

    public void setImageViewId(int imageViewId) {
        this.imageViewId = imageViewId;
    }
    public String getTextViewTitle() {
        return textViewTitle;
    }
    public void setTextViewTitle(String textViewTitle) {
        this.textViewTitle = textViewTitle;
    }
}
