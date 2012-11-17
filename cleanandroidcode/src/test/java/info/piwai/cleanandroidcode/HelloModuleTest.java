package info.piwai.cleanandroidcode;

import javax.inject.Inject;

import com.squareup.otto.Bus;

import dagger.Module;
import dagger.ObjectGraph;

/**
 * TODO This test can't run within Eclipse, only within Maven We should remove
 * it anyway, it tests Dagger rather then the code itself
 */
public class HelloModuleTest {

	// @Test
	public void module_provides_singleton_bus() {

		class EntryPoint1 {
			@Inject
			Bus bus;
		}

		class EntryPoint2 {
			@Inject
			Bus bus;
		}

		@Module(//
		includes = HelloModule.class, //
		entryPoints = { EntryPoint1.class, EntryPoint2.class })
		class TestModule {
		}

		ObjectGraph graph = ObjectGraph.create(new TestModule());

		EntryPoint1 entryPoint1 = new EntryPoint1();
		EntryPoint2 entryPoint2 = new EntryPoint2();

		graph.inject(entryPoint1);
		graph.inject(entryPoint2);

		// assertNotNull(entryPoint1.bus);

		// assertSame(entryPoint1.bus, entryPoint2.bus);
	}
}
