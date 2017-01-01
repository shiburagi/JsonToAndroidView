package com.app.infideap.jsontoview.builder;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.infideap.jsontoview.Constant;
import com.app.infideap.jsontoview.DimensionConverter;
import com.app.infideap.jsontoview.JsonActivity;
import com.app.infideap.jsontoview.JsonView;
import com.app.infideap.jsontoview.ViewProperties;
import com.app.infideap.jsontoview.util.IdManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.TreeSet;

/**
 * Created by Shiburagi on 31/12/2016.
 */
public class LayoutBuilder {

    private static final String TAG = LayoutBuilder.class.getSimpleName();
    private final JsonView.ViewGroupParser parser;
    protected final JsonActivity context;

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
            String text = element.getAsString();
            if (text.charAt(0) == '@')
                view.setText(((AppCompatActivity) context).getIntent().getStringExtra(text));
            else
                view.setText(text);
        }

        element = object.get(ViewProperties.TEXT_SIZE);
        if (isNotNull(element)) {
            view.setTextSize(size(element));
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

        element = object.get(ViewProperties.ID);
        if (isNotNull(element)) {
            IdManager.getInstance().create(view, element.getAsString());
            Log.e(TAG, "ID : " + element.getAsString() + ", " + view.getId());
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
            params.width = size(element);
        }

        element = object.get(ViewProperties.HEIGHT);
        if (isNotNull(element)) {
            params.height = size(element);
        }


        view.setLayoutParams(params);


        element = object.get(ViewProperties.ON_CLICK);
        if (isNotNull(element)) {

            final JsonElement finalElement = element;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Intent intent = new Intent(context, JsonActivity.class);
                    if (finalElement.isJsonObject()) {
                        extract(intent, finalElement.getAsJsonObject());
                    } else {
                        intent.putExtra(JsonActivity.PAGE, finalElement.getAsString());
                    }
                    context.startActivity(intent);
                }
            });
        }


    }

    private void extract(Intent intent, JsonObject object) {
        Log.e(TAG, "extract()");
        JsonElement element = object.get(ViewAction.ACTION);
        if (isNotNull(element)) {
            intent.putExtra(JsonActivity.PAGE, element.getAsString());
        }
        element = object.get(ViewAction.PARAMS);
        if (isNotNull(element)) {
            if (element.isJsonArray()) {
                JsonArray array = element.getAsJsonArray();
                for (int i = 0; i < array.size(); i++) {
                    JsonElement jsonElement = array.get(i);
                    if (jsonElement.isJsonPrimitive()) {
                        String id = jsonElement.getAsString();
                        int idInt = IdManager.getInstance().getId(id);
                        Log.e(TAG, "Params Id : " + id+", "+idInt);
                        View view = context.findViewById(idInt);
                        Log.e(TAG, "View : " + String.valueOf(view));
                        if (view != null)
                            if (view instanceof TextView) {
                                String text = ((TextView) view).getText().toString();
                                Log.e(TAG, "Params : " + text);
                                intent.putExtra(id, text);
                            }
                    }
                }
            }
        }
    }

    public int getGravity(JsonObject object, int _default) {
        JsonElement element = object.get(ViewProperties.GRAVITY);
        if (isNotNull(element)) {
            Log.e(TAG, "Gravity : " + element.getAsString());
            String[] gravities = element.getAsString().split("\\|");
            TreeSet<Integer> set = new TreeSet<>();
            for (String gravity : gravities) {
                Log.e(TAG, gravity);
                switch (gravity) {
                    case Constant.CENTER:
                        set.add(Gravity.CENTER);
                        break;
                    case Constant.LEFT:
                        set.add(Gravity.LEFT);
                        break;
                    case Constant.RIGHT:
                        set.add(Gravity.RIGHT);
                        break;
                    case Constant.TOP:
                        set.add(Gravity.TOP);
                        break;
                    case Constant.BOTTOM:
                        set.add(Gravity.BOTTOM);
                        break;
                    case Constant.CENTER_HORIZONTAL:
                        set.add(Gravity.CENTER_HORIZONTAL);
                        break;
                    case Constant.CENTER_VERTICAL:
                        set.add(Gravity.CENTER_VERTICAL);
                        break;
                }
            }

            if (set.size() > 0) {
                int gravity = set.pollFirst();
                Log.e(TAG, "Gravity i : " + gravity);
                for (int i : set) {
                    gravity = gravity | i;
                    Log.e(TAG, "Gravity i : " + gravity);
                }

                return gravity;
            }
        }
        return _default;
    }

    private int size(JsonElement element) {

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


    protected boolean isNotNull(JsonElement element) {
        if (element != null)
            return !element.isJsonNull();
        else
            return false;
    }
}
