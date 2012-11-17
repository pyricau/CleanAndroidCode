package info.piwai.cleanandroidcode;

import info.piwai.cleanandroidcode.base.BaseFragment;

import javax.inject.Inject;

import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EFragment;
import com.squareup.otto.Bus;

@EFragment(R.layout.hello_fragment)
public class HelloFragment extends BaseFragment {

	@Inject
	Bus bus;

	@Click
	void fragmentButtonClicked() {
		bus.post(new UpdateTitleEvent("Button clicked at " + System.currentTimeMillis()));
	}
}
