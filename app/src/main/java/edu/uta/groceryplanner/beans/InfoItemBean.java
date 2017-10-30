package edu.uta.groceryplanner.beans;

public class InfoItemBean {
    private int imageViewId;
    private String textViewTitle;

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
