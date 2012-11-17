package info.piwai.cleanandroidcode;

import info.piwai.cleanandroidcode.base.BaseModule;
import info.piwai.cleanandroidcode.base.GraphRetriever.GraphApplication;
import android.app.Application;
import dagger.ObjectGraph;

public class HelloApplication extends Application implements GraphApplication {

	private ObjectGraph graph;

	@Override
	public void onCreate() {
		super.onCreate();
		graph = ObjectGraph.create(new BaseModule(), new EntryPointsModule());
	}

	@Override
	public ObjectGraph getGraph() {
		return graph;
	}

}
