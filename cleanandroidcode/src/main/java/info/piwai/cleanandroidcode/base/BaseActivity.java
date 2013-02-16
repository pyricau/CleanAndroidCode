package info.piwai.cleanandroidcode.base;

import android.os.Bundle;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.squareup.otto.Bus;

import javax.inject.Inject;

public class BaseActivity extends SherlockFragmentActivity {

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
