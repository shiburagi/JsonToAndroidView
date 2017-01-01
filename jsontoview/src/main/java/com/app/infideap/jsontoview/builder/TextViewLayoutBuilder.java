package com.app.infideap.jsontoview.builder;

import android.view.Gravity;
import android.view.View;

import com.app.infideap.jsontoview.JsonView;
import com.app.infideap.stylishwidget.view.ATextView;
import com.google.gson.JsonObject;

/**
 * Created by Shiburagi on 31/12/2016.
 */
public class TextViewLayoutBuilder extends LayoutBuilder {


    public TextViewLayoutBuilder(JsonView.ViewGroupParser context) {
        super(context);
    }

    public View create(JsonObject object) {
        ATextView textView = new ATextView(context);
//        editText.setText("test");
        textView.setGravity(getGravity(object, Gravity.START));

        setup(textView, object);
        return textView;
    }
}
