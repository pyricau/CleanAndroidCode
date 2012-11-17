package info.piwai.cleanandroidcode.base;

import javax.inject.Singleton;

import android.util.Log;

import com.squareup.otto.Bus;

import dagger.Module;
import dagger.Provides;

@Module
public class BaseModule {

	@Provides
	@Singleton
	Bus provideBus() {
		/*
		 * TODO understand why the stacktrace goes through
		 * ReflectiveAtInjectBinding.injectMembers
		 */
		Log.e("BaseModule", "Compile time binding check", new Exception());
		return new Bus();
	}

}
