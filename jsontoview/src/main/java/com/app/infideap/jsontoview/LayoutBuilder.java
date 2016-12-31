package com.app.infideap.jsontoview;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Created by Shiburagi on 31/12/2016.
 */
public class LayoutBuilder {

    private static final String TAG = LayoutBuilder.class.getSimpleName();
    private final JsonView.ViewGroupParser parser;
    protected final Context context;

    public LayoutBuilder(JsonView.ViewGroupParser parser) {
        this.parser = parser;
        this.context = parser.getContext();
    }

    public void parse(ViewGroup viewGroup, JsonObject o) {
        JsonElement element = o.get(ViewProperties.CHILD);
        if (element != null)
            if (element.isJsonArray()) {
                parse(viewGroup, element.getAsJsonArray());
            }
    }

    public void parse(ViewGroup viewGroup, JsonArray array) {
        for (int i = 0; i < array.size(); i++) {
            JsonElement element = array.get(i);
            if (element.isJsonObject()) {
                parser.parse(viewGroup, element);
            }
        }
    }

    public void setup(TextView view, JsonObject object) {
        JsonElement element = object.get(ViewProperties.TEXT_COLOR);
        if (isNotNull(element)) {
            view.setTextColor(Color.parseColor(element.getAsString()));
        }
        element = object.get(ViewProperties.TEXT);
        if (isNotNull(element)) {
            view.setText(element.getAsString());
        }


        setup((View) view, object);


    }

    public void setup(View view, JsonObject object) {
        JsonElement element = object.get(ViewProperties.BACKGROUND_COLOR);
        if (isNotNull(element)) {
            Log.e(TAG, element.getAsString());
            view.setBackgroundColor(Color.parseColor(element.getAsString()));
        }

        element = object.get(ViewProperties.PADDING);
        if (isNotNull(element)) {
            int padding = (int) DimensionConverter.stringToDimension(element.getAsString(),
                    context.getResources().getDisplayMetrics());
            Log.e(TAG, "Padding : " + padding);
            view.setPadding(padding, padding, padding, padding);
        }

        element = object.get(ViewProperties.MARGIN);
        if (isNotNull(element)) {
            int padding = (int) DimensionConverter.stringToDimension(element.getAsString(),
                    context.getResources().getDisplayMetrics());
            Log.e(TAG, "Padding : " + padding);
            view.setPadding(padding, padding, padding, padding);
        }
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params == null)
            params = new ViewGroup
                    .LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        element = object.get(ViewProperties.WIDTH);
        if (isNotNull(element)) {
            params.width = size(params, element);
        }

        element = object.get(ViewProperties.HEIGHT);
        if (isNotNull(element)) {
            params.height = size(params, element);
        }

        view.setLayoutParams(params);

    }

    private int size(ViewGroup.LayoutParams params, JsonElement element) {

        Log.e(TAG, element.getAsString());
        String value = element.getAsString();
        switch (value) {
            case Constant.FILL:
                return ViewGroup.LayoutParams.MATCH_PARENT;
            case Constant.WRAP:
                return ViewGroup.LayoutParams.WRAP_CONTENT;
            default:
                return (int) DimensionConverter.stringToDimension(
                        value,
                        context.getResources().getDisplayMetrics()
                );
        }

    }


    private boolean isNotNull(JsonElement element) {
        if (element != null)
            return !element.isJsonNull();
        else
            return false;
    }
}
