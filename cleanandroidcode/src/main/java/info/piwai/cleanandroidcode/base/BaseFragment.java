package info.piwai.cleanandroidcode.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public abstract class BaseFragment extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GraphRetriever.from(getActivity()).inject(this);
	}

}
