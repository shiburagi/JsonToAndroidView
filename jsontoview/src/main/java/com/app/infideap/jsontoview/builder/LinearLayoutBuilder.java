package com.app.infideap.jsontoview.builder;

import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.app.infideap.jsontoview.Constant;
import com.app.infideap.jsontoview.JsonView;
import com.app.infideap.jsontoview.ViewProperties;
import com.google.gson.JsonElement;
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
        JsonElement element = object.get(ViewProperties.ORIENTATION);
        if (isNotNull(element)) {
            switch (element.getAsString()) {
                case Constant.VERTICAL:
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    break;
                default:
                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    break;
            }
        }

        int gravity = getGravity(object, Gravity.START);
        Log.e(TAG, "gravity : " + gravity);
        linearLayout.setGravity(gravity);

        setup(linearLayout, object);
        super.parse(linearLayout, object);
        return linearLayout;
    }
}
