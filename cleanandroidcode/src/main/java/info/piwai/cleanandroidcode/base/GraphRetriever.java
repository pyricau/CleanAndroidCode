package info.piwai.cleanandroidcode.base;

import android.app.Activity;
import dagger.ObjectGraph;

public class GraphRetriever {

	public interface GraphApplication {
		ObjectGraph getGraph();
	}

	public static ObjectGraph from(Activity activity) {
		GraphApplication application = (GraphApplication) activity.getApplication();
		return application.getGraph();
	}

}
