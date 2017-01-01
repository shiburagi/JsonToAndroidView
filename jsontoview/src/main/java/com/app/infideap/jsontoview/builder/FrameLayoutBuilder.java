package com.app.infideap.jsontoview.builder;

import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.app.infideap.jsontoview.JsonView;
import com.google.gson.JsonObject;

/**
 * Created by Shiburagi on 31/12/2016.
 */
public class FrameLayoutBuilder extends LayoutBuilder {


    public FrameLayoutBuilder(JsonView.ViewGroupParser context) {
        super(context);
    }

    public ViewGroup create(JsonObject object) {
        LinearLayout linearLayout = new LinearLayout(context);
        return linearLayout;
    }
}
