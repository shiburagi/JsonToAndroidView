package com.app.infideap.jsontoview.builder;

import android.view.Gravity;
import android.view.View;

import com.app.infideap.jsontoview.JsonView;
import com.app.infideap.stylishwidget.view.AEditText;
import com.google.gson.JsonObject;

/**
 * Created by Shiburagi on 31/12/2016.
 */
public class EditTextLayoutBuilder extends LayoutBuilder {


    public EditTextLayoutBuilder(JsonView.ViewGroupParser context) {
        super(context);
    }

    public View create(JsonObject object) {
        AEditText editText = new AEditText(context);
//        editText.setText("test");
        editText.setGravity(getGravity(object, Gravity.START));

        setup(editText, object);
        return editText;
    }
}
