package info.piwai.cleanandroidcode.base;


import javax.inject.Inject;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;

import com.squareup.otto.Bus;

/**
 * TODO use support fragments
 */
@SuppressLint("NewApi")
public abstract class BaseFragment extends Fragment {

	@Inject
	Bus bus;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GraphRetriever.from(getActivity()).inject(this);
		bus.register(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		bus.unregister(this);
	}

}
