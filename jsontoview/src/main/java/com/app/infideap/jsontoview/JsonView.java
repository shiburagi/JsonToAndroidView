package com.app.infideap.jsontoview;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.app.infideap.httprequest.Atom;
import com.app.infideap.httprequest.FutureCallback;
import com.app.infideap.jsontoview.builder.ButtonLayoutBuilder;
import com.app.infideap.jsontoview.builder.EditTextLayoutBuilder;
import com.app.infideap.jsontoview.builder.FrameLayoutBuilder;
import com.app.infideap.jsontoview.builder.LinearLayoutBuilder;
import com.app.infideap.jsontoview.builder.RelativeLayoutBuilder;
import com.app.infideap.jsontoview.builder.TextViewLayoutBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

/**
 * Created by Shiburagi on 31/12/2016.
 */
public class JsonView {
    private static final String TAG = JsonView.class.getSimpleName();
    private static final Gson GSON = new Gson();

    public static void parse(final JsonActivity jsonActivity, final FrameLayout parent, String url) {
        FirebaseDatabase.getInstance().getReference()
                .child(url)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.e(TAG, String.valueOf(dataSnapshot.getValue()));
                        JsonElement element = GSON.fromJson(
                                GSON.toJson((Map<String, Object>) dataSnapshot.getValue()),
                                JsonElement.class
                        );
                        parent.removeAllViews();
                        new ViewGroupParser(parent.getContext())
                                .parse(
                                        jsonActivity,
                                        parent,
                                        element
                                );

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private static void parseUrl(final FrameLayout parent, String url) {
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
        private final TextViewLayoutBuilder textViewLayoutBuilder;
        private final EditTextLayoutBuilder editTextLayoutBuilder;
        private final ButtonLayoutBuilder butonLayoutBuilder;
        private final Context context;

        public ViewGroupParser(Context context) {
            this.context = context;
            linearLayoutBuilder = new LinearLayoutBuilder(this);
            frameLayoutBuilder = new FrameLayoutBuilder(this);
            relativeLayoutBuilder = new RelativeLayoutBuilder(this);
            editTextLayoutBuilder = new EditTextLayoutBuilder(this);
            butonLayoutBuilder = new ButtonLayoutBuilder(this);
            textViewLayoutBuilder = new TextViewLayoutBuilder(this);
        }


        public void parse(JsonActivity jsonActivity, FrameLayout parent, JsonElement element) {
            if (element.isJsonObject()) {
                JsonObject object = element.getAsJsonObject();
                JsonElement jsonElement = object.get(ViewProperties.SHOW_BACK_BUTTON);
                if (jsonElement != null)
                    if (!jsonElement.isJsonNull()) {
                        jsonActivity.getSupportActionBar()
                                .setDisplayHomeAsUpEnabled(jsonElement.getAsBoolean());
                    }
                parse(parent, object);
            }
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
                case ViewType.TEXTVIEW:
                    parent.addView(textViewLayoutBuilder.create(object));
                    break;
                case ViewType.BUTTON:
                    parent.addView(butonLayoutBuilder.create(object));
                    break;
            }
            return null;
        }


        public Context getContext() {
            return context;
        }

    }
}
