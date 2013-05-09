package info.piwai.cleanandroidcode.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.retrofit.RetrofitGsonSpiceService;

public abstract class BaseFragment extends Fragment {

    protected SpiceManager spiceManager = new SpiceManager(RetrofitGsonSpiceService.class);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GraphRetriever.from(getActivity()).inject(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        spiceManager.start(getActivity());
    }

    @Override
    public void onStop() {
        spiceManager.shouldStop();
        super.onStop();
    }

}
