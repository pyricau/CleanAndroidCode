package info.piwai.cleanandroidcode.base;

import javax.inject.Singleton;

import com.squareup.otto.Bus;

import dagger.Module;
import dagger.Provides;

@Module
public class BaseModule {

	@Provides
	@Singleton
	Bus provideBus() {
		return new Bus();
	}

}
