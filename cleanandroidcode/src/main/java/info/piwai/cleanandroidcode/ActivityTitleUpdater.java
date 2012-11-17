package info.piwai.cleanandroidcode;

import javax.inject.Inject;

import android.app.Activity;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

/**
 * This class is a bit weird. It's needed because AndroidAnnotations generates
 * activity subclasses, and Otto doesn't look through superclass for now.
 * 
 * TODO see how this can be fixed in Otto
 * 
 */
public class ActivityTitleUpdater {

	@Inject
	Bus bus;

	private Activity activity;

	void onCreate(Activity activity) {
		bus.register(this);
		this.activity = activity;
	}

	void onDestroy() {
		bus.unregister(this);
		activity = null;
	}

	@Subscribe
	public void onUpdateTitle(UpdateTitleEvent event) {
		activity.setTitle(event.title);
	}

}
