package info.piwai.cleanandroidcode;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;

/*
 * TODO use support fragment
 */
@SuppressLint("NewApi")
public class HelloAndroidActivityTest extends ActivityInstrumentationTestCase2<HelloAndroidActivity_> {

	public HelloAndroidActivityTest() {
		super(HelloAndroidActivity_.class);
	}

	public void testActivity() {
		HelloAndroidActivity activity = getActivity();
		assertNotNull(activity);
	}

	@UiThreadTest
	public void testClickEventPropagated() {
		Activity activity = getActivity();
		String beforeClick = activity.getTitle().toString();
		HelloFragment fragment = (HelloFragment) activity.getFragmentManager().findFragmentById(R.id.hello_fragment);
		fragment.fragmentButton.performClick();
		String afterClick = activity.getTitle().toString();
		assertFalse(beforeClick.equals(afterClick));
		fragment.fragmentButton.performClick();
		String afterClick2 = activity.getTitle().toString();
		assertFalse(afterClick.equals(afterClick2));
	}

}
