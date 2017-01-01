package com.app.infideap.jsontoview.util;

import android.os.Build;
import android.view.View;

import java.util.HashMap;

/**
 * Created by Shiburagi on 02/01/2017.
 */
public class IdManager {
    private static IdManager instance;

    private HashMap<String, Integer> map = new HashMap<>();

    static {
        instance = new IdManager();
    }

    public static IdManager getInstance() {
        return instance;
    }

    public void create(View view, String id) {
        if (!map.containsKey(id)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                map.put(id, View.generateViewId());
            } else {
                map.put(id, Utils.generateViewId());
            }
        }
        view.setId(map.get(id));

    }

    public int getId(String id) {
        return map.get(id);
    }
}
