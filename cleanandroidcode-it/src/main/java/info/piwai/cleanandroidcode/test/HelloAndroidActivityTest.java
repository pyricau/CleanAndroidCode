package info.piwai.cleanandroidcode.test;

import info.piwai.cleanandroidcode.HelloAndroidActivity;
import android.test.ActivityInstrumentationTestCase2;

public class HelloAndroidActivityTest extends ActivityInstrumentationTestCase2<HelloAndroidActivity> {

	public HelloAndroidActivityTest() {
		super(HelloAndroidActivity.class);
	}

	public void testActivity() {
		HelloAndroidActivity activity = getActivity();
		assertNotNull(activity);
	}
}
