package info.piwai.cleanandroidcode;

import info.piwai.cleanandroidcode.base.BaseActivity;

import javax.inject.Inject;

import android.os.Bundle;

import com.googlecode.androidannotations.annotations.EActivity;

@EActivity(R.layout.hello_activity)
public class HelloAndroidActivity extends BaseActivity {

	@Inject
	ActivityTitleUpdater titleUpdater;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		titleUpdater.onCreate(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		titleUpdater.onDestroy();
	}

}
