package info.piwai.cleanandroidcode;

import info.piwai.cleanandroidcode.base.BaseActivity;

import javax.inject.Inject;

import android.os.Bundle;

import com.googlecode.androidannotations.annotations.EActivity;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

@EActivity(R.layout.hello_activity)
public class HelloAndroidActivity extends BaseActivity {

	/*
	 * Needed because Otto doesn't look through superclasses
	 */
	class TitleUpdater {
		@Subscribe
		public void onUpdateTitle(UpdateTitleEvent event) {
			setTitle(event.title);
		}
	}

	@Inject
	Bus bus;

	private TitleUpdater titleUpdater;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		titleUpdater = new TitleUpdater();
		bus.register(titleUpdater);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		bus.unregister(titleUpdater);
	}

}
