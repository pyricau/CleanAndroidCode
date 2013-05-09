package info.piwai.cleanandroidcode;

import info.piwai.cleanandroidcode.base.BaseActivity;

import com.googlecode.androidannotations.annotations.EActivity;
import com.squareup.otto.Subscribe;

@EActivity(R.layout.hello_activity)
public class HelloAndroidActivity extends BaseActivity {

    @Subscribe
    public void onUpdateTitle(UpdateTitleEvent event) {
        setTitle(event.title);
    }

}
