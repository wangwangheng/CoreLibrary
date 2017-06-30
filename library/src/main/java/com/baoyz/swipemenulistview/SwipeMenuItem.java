package com.baoyz.swipemenulistview;


import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * @author baoyz
 * @date 2014-8-23
 */
public class SwipeMenuItem {

    private int id;
    private Context mContext;
    private String title;
    private Drawable icon;
    private Drawable background;
    private int titleColor;
    private int titleSize;
    private int width;
    private int paddingLeft = 0;
    private int paddingRight = 0;
    private int paddingTop = 0;
    private int paddingBottom = 0;
    private int textBackgroud;
    private int layoutBackgroud;

    public int getLayoutBackgroud() {
        return layoutBackgroud;
    }

    public void setLayoutBackgroud(int layoutBackgroud) {
        this.layoutBackgroud = layoutBackgroud;
    }


    public boolean isSpecialView() {
        return isSpecialView;
    }

    public void setSpecialView(boolean specialView) {
        isSpecialView = specialView;
    }

    private boolean isSpecialView = false;

    public int getPaddingLeft() {
        return paddingLeft;
    }

    public void setPaddingLeft(int paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    public int getPaddingRight() {
        return paddingRight;
    }

    public void setPaddingRight(int paddingRight) {
        this.paddingRight = paddingRight;
    }

    public int getPaddingTop() {
        return paddingTop;
    }

    public void setPaddingTop(int paddingTop) {
        this.paddingTop = paddingTop;
    }

    public int getPaddingBottom() {
        return paddingBottom;
    }

    public void setPaddingBottom(int paddingBottom) {
        this.paddingBottom = paddingBottom;
    }

    public int getTextBackgroud() {
        return textBackgroud;
    }

    public void setTextBackgroud(int textBackgroud) {
        this.textBackgroud = textBackgroud;
    }


    public SwipeMenuItem(Context context) {
        mContext = context;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTitleColor() {
        return titleColor;
    }

    public int getTitleSize() {
        return titleSize;
    }

    public void setTitleSize(int titleSize) {
        this.titleSize = titleSize;
    }

    public void setTitleColor(int titleColor) {
        this.titleColor = titleColor;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTitle(int resId) {
        setTitle(mContext.getString(resId));
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public void setIcon(int resId) {
        this.icon = mContext.getResources().getDrawable(resId);
    }

    public Drawable getBackground() {
        return background;
    }

    public void setBackground(Drawable background) {
        this.background = background;
    }

    public void setBackground(int resId) {
        this.background = mContext.getResources().getDrawable(resId);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

}
