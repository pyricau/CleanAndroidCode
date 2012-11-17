package info.piwai.cleanandroidcode.base;

import javax.inject.Inject;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.squareup.otto.Bus;

public class BaseActivity extends FragmentActivity {

	@Inject
	Bus bus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
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
