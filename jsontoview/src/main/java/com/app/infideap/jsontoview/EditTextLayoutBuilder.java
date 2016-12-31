package com.app.infideap.jsontoview;

import android.view.View;
import android.widget.EditText;

import com.google.gson.JsonObject;

/**
 * Created by Shiburagi on 31/12/2016.
 */
public class EditTextLayoutBuilder extends LayoutBuilder {


    public EditTextLayoutBuilder(JsonView.ViewGroupParser context) {
        super(context);
    }

    public View create(JsonObject object) {
        EditText editText = new EditText(context);
//        editText.setText("test");

        setup(editText, object);
        return editText;
    }
}
