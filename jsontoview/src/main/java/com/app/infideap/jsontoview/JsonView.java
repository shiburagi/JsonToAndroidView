package com.app.infideap.jsontoview;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.app.infideap.httprequest.Atom;
import com.app.infideap.httprequest.FutureCallback;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Shiburagi on 31/12/2016.
 */
public class JsonView {
    private static final String TAG = JsonView.class.getSimpleName();

    public static void parse(final FrameLayout parent, String url) {
        Atom.with(parent.getContext()).load(url)
                .asJsonElement()
                .setCallback(new FutureCallback<JsonElement>() {
                    @Override
                    public void onCompleted(Exception e, JsonElement result) {
                        if (e != null) {
                            e.printStackTrace();
                        } else {
                            Log.e(TAG, new Gson().toJson(result));
                            new ViewGroupParser(parent.getContext()).parse(parent, result);
                        }
                    }
                });
    }

    public static class ViewGroupParser {
        private final FrameLayoutBuilder frameLayoutBuilder;
        private final LinearLayoutBuilder linearLayoutBuilder;
        private final RelativeLayoutBuilder relativeLayoutBuilder;
        private final EditTextLayoutBuilder editTextLayoutBuilder;
        private final Context context;

        public ViewGroupParser(Context context) {
            this.context = context;
            linearLayoutBuilder = new LinearLayoutBuilder( this);
            frameLayoutBuilder = new FrameLayoutBuilder( this);
            relativeLayoutBuilder = new RelativeLayoutBuilder( this);
            editTextLayoutBuilder = new EditTextLayoutBuilder( this);
        }


        public void parse(ViewGroup parent, JsonElement result) {
            if (result.isJsonObject()) {
                parse(parent, result.getAsJsonObject());
            }
//            else {
//                return parse(parent, result.getAsJsonObject());
//            }
        }

        private ViewGroup parse(ViewGroup parent, JsonObject object) {
            String type = object.get(ViewProperties.TYPE).getAsString();
            switch (type) {
                case ViewType.FRAME_LAYOUT:
                    parent.addView(frameLayoutBuilder.create(object));
                    break;
                case ViewType.LINEAR_LAYOUT:
                    parent.addView(linearLayoutBuilder.create(object));
                    break;
                case ViewType.RELATIVE_LAYOUT:
                    parent.addView(relativeLayoutBuilder.create(object));
                    break;
                case ViewType.EDITTEXT:
                    parent.addView(editTextLayoutBuilder.create(object));
                    break;
            }
            return null;
        }

        private ViewGroup parse(ViewGroup parent, JSONArray array) {
            for (int i = 0; i < array.length(); i++) {
                try {
                    Object o = array.get(i);
                    parent.addView(parse(parent, o));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        private View parse(ViewGroup parent, Object o) {
            return null;
        }

        private ViewGroup parse(ViewGroup parent, String key, JsonElement element) {
            if (element.isJsonObject()) {
                return parse(parent, element.getAsJsonObject());
            } else {
                return parse(parent, element.getAsJsonObject());
            }
        }

        public Context getContext() {
            return context;
        }
    }
}
