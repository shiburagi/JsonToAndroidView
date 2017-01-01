package com.app.infideap.jsontoview;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

public class JsonActivity extends AppCompatActivity {

    private static final String TAG = JsonActivity.class.getSimpleName();
    public static final String PAGE = "PAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout frameLayout = new FrameLayout(this);
        setContentView(frameLayout);

        String url = getIntent().getStringExtra(PAGE);


        if (url == null){
            try {
                ApplicationInfo ai = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
                Bundle bundle = ai.metaData;
                url = bundle.getString("json_url");
                Log.e(TAG, "Name " + url);
            } catch (PackageManager.NameNotFoundException e) {
                Log.e(TAG, "Failed to load meta-data, NameNotFound: " + e.getMessage());
            } catch (NullPointerException e) {
                Log.e(TAG, "Failed to load meta-data, NullPointer: " + e.getMessage());
            }
        }

        if (url!=null){
            JsonView.parse(this, frameLayout, url);
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}