package de.wpavelev.scorecounter2.adapters;

import androidx.annotation.NonNull;

public class IconTextWrapper {
    private String text;
    private int drawable;

    public IconTextWrapper(String text, int drawableId) {
        this.text = text;
        this.drawable = drawableId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    @NonNull
    @Override
    public String toString() {
        return text + "_"+ drawable;




    }
}
