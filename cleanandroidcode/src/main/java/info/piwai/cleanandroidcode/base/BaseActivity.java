package info.piwai.cleanandroidcode.base;


import javax.inject.Inject;

import android.app.Activity;
import android.os.Bundle;

import com.squareup.otto.Bus;

public class BaseActivity extends Activity {

	@Inject
	Bus bus;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GraphRetriever.from(this).inject(this);
		bus.register(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		bus.unregister(this);
	}

}
