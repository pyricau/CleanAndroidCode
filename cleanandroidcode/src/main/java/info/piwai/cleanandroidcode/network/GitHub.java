package info.piwai.cleanandroidcode.network;

import retrofit.http.GET;
import retrofit.http.Name;

interface GitHub {
    @GET("/repos/{owner}/{repo}/contributors")
    ListContributor contributors(@Name("owner") String owner, @Name("repo") String repo);
}
