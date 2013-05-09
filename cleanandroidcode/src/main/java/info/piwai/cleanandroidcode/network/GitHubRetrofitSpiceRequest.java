package info.piwai.cleanandroidcode.network;

import retrofit.http.RestAdapter;
import roboguice.util.temp.Ln;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

public class GitHubRetrofitSpiceRequest extends RetrofitSpiceRequest<ListContributor> {

    private static final String BASE_URL = "https://api.github.com";

    public GitHubRetrofitSpiceRequest() {
        super(ListContributor.class);
    }

    @Override
    public ListContributor loadDataFromNetwork() {
        Ln.d("Call web service " + BASE_URL);

        // Create a very simple REST adapter which points the GitHub API endpoint.
        RestAdapter restAdapter = new RestAdapter.Builder().setServer(BASE_URL).build();

        // Create an instance of our GitHub API interface.
        GitHub github = restAdapter.create(GitHub.class);

        return github.contributors("square", "retrofit");
    }

}
