package com.app.infideap.jsontoview;

import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.JsonObject;

/**
 * Created by Shiburagi on 31/12/2016.
 */
public class RelativeLayoutBuilder extends LayoutBuilder {


    public RelativeLayoutBuilder(JsonView.ViewGroupParser context) {
        super(context);
    }

    public ViewGroup create(JsonObject object) {
        LinearLayout linearLayout = new LinearLayout(context);
        return linearLayout;
    }
}
