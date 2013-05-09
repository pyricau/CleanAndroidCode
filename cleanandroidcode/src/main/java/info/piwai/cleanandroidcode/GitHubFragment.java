package info.piwai.cleanandroidcode;

import info.piwai.cleanandroidcode.base.BaseFragment;
import info.piwai.cleanandroidcode.network.GitHubRetrofitSpiceRequest;
import info.piwai.cleanandroidcode.network.ListContributor;

import javax.inject.Inject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

@EFragment(R.layout.hello_github_fragment)
public class GitHubFragment extends BaseFragment {

    @Inject
    Bus bus;

    @ViewById(R.id.my_label)
    TextView mylabel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bus.register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        spiceManager.addListenerIfPending(ListContributor.class, "square", new GitHubRequestListenerRequestListener());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Subscribe
    public void onUpdateTitle(UpdateTitleEvent event) {
        spiceManager.execute(new GitHubRetrofitSpiceRequest(), "square", DurationInMillis.ALWAYS_RETURNED, new GitHubRequestListenerRequestListener());
        mylabel.setText("Requesting contributors on github...");
    }

    private final class GitHubRequestListenerRequestListener implements RequestListener<ListContributor> {
        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(getActivity(), "failure", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRequestSuccess(ListContributor result) {
            mylabel.setText("Square contributors: " + result.size());
        }
    }
}
