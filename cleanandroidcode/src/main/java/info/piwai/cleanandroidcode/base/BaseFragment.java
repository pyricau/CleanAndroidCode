package info.piwai.cleanandroidcode.base;

import android.os.Bundle;
import com.actionbarsherlock.app.SherlockFragment;

public abstract class BaseFragment extends SherlockFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GraphRetriever.from(getActivity()).inject(this);
	}

}
