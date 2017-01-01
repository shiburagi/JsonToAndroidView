package com.app.infideap.jsontoview.builder;

import android.view.Gravity;
import android.view.View;

import com.app.infideap.jsontoview.JsonView;
import com.app.infideap.stylishwidget.view.AButton;
import com.google.gson.JsonObject;

/**
 * Created by Shiburagi on 31/12/2016.
 */
public class ButtonLayoutBuilder extends LayoutBuilder {


    public ButtonLayoutBuilder(JsonView.ViewGroupParser context) {
        super(context);
    }

    public View create(JsonObject object) {
        AButton button = new AButton(context);
//        editText.setText("test");
        button.setGravity(getGravity(object, Gravity.CENTER));

        setup(button, object);
        return button;
    }
}
