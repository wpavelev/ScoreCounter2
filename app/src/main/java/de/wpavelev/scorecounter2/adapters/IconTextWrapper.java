package de.wpavelev.scorecounter2.adapters;

import androidx.annotation.NonNull;

public class IconTextWrapper {
    private String mText;
    private int mDrawable;

    public IconTextWrapper(String text, int drawableId) {
        this.mText = text;
        this.mDrawable = drawableId;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        this.mText = text;
    }

    public int getDrawable() {
        return mDrawable;
    }

    public void setDrawable(int drawable) {
        this.mDrawable = drawable;
    }

    @NonNull
    @Override
    public String toString() {
        return mText + "_"+ mDrawable;




    }
}
