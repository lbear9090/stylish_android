package com.stylelist.Models;

/**
 * Created by security on 2/13/2018.
 */

public class MenuItem {

    public int iconResId;
    public int titleResId;
    public boolean isHeader;

    public MenuItem(int iconResId, int titleResId, boolean isHeader) {
        this.iconResId = iconResId;
        this.titleResId = titleResId;
        this.isHeader = isHeader;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public int getTitleResId() {
        return titleResId;
    }

    public void setTitleResId(int titleResId) {
        this.titleResId = titleResId;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }
}
