package com.app.infideap.jsontoview.builder;

import android.view.View;
import android.widget.ImageView;

import com.app.infideap.jsontoview.JsonView;
import com.google.gson.JsonObject;

/**
 * Created by Shiburagi on 31/12/2016.
 */
public class ImageViewLayoutBuilder extends LayoutBuilder {


    public ImageViewLayoutBuilder(JsonView.ViewGroupParser context) {
        super(context);
    }

    public View create(JsonObject object) {
        ImageView button = new ImageView(context);
//        editText.setText("test");
        setup(button, object);
        return button;
    }
}
