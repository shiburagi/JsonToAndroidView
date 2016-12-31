package com.app.infideap.jsontoview;

import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.JsonObject;

/**
 * Created by Shiburagi on 31/12/2016.
 */
public class LinearLayoutBuilder extends LayoutBuilder {


    private static final String TAG = LinearLayoutBuilder.class.getSimpleName();

    public LinearLayoutBuilder(JsonView.ViewGroupParser context) {
        super(context);
    }

    public ViewGroup create(JsonObject object) {
        LinearLayout linearLayout = new LinearLayout(context);
        setup(linearLayout, object);
        super.parse(linearLayout,object);
        return linearLayout;
    }
}
