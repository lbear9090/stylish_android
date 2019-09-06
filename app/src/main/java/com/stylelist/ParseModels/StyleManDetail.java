package com.stylelist.ParseModels;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

/**
 * Created by security on 2/21/2018.
 */

@ParseClassName("StyleManDetail")
public class StyleManDetail extends ParseObject {

    public String styleName;
    public String styleDetail;
    public ParseFile styleImage;

    public String getStyleName() {
        styleName = getString("StyleName");
        return styleName;
    }

    public String getStyleDetail() {
        styleDetail = getString("StyleDetail");
        return styleDetail;
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
