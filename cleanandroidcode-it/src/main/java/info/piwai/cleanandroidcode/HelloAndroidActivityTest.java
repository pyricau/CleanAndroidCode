package info.piwai.cleanandroidcode;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.view.View;

public class HelloAndroidActivityTest extends ActivityInstrumentationTestCase2<HelloAndroidActivity_> {

	public HelloAndroidActivityTest() {
		super(HelloAndroidActivity_.class);
	}

	@UiThreadTest
	public void test_click_on_fragment_changes_title() {
		FragmentActivity activity = getActivity();
		Fragment fragment = activity.getSupportFragmentManager().findFragmentById(R.id.hello_fragment);
		View fragmentButton = fragment.getView().findViewById(R.id.fragment_button);

		fragmentButton.performClick();

		String title = activity.getTitle().toString();

		assertTrue("Title should start with [Button clicked]. Title is [" + title + "]", title.startsWith("Button clicked"));
	}

}
