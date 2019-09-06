package com.stylelist.ParseModels;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

/**
 * Created by security on 2/21/2018.
 */
@ParseClassName("StyleMan")
public class StyleMan extends ParseObject {

    public String styleName;
    public String styleDescription;
    public ParseFile styleImage;

    public boolean isSelected = false;

    public String getStyleName() {
        styleName = getString("StyleName");
        return styleName;
    }

    public String getStyleDescription() {
        styleDescription = getString("StyleDescription");
        return styleDescription;
    }

    public ParseFile getStyleImage() {
        try {
            styleImage = fetchIfNeeded().getParseFile("StyleImage");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return styleImage;
    }
}
