# ALL YOUR LIB ARE BELONG TO US

TODO: image code smell

## Introduction

This project is a proof of concept to show how to integrate [Dagger](http://square.github.com/dagger/), [Otto](http://square.github.com/otto/) and [AndroidAnnotations](http://androidannotations.org).

We want to benefit from all those librairies to write Android apps that are both easy to maintain and lightning fast.

Here is what the result:

```java
@EActivity(R.layout.hello_activity)
public class HelloAndroidActivity extends BaseActivity {

	@Subscribe
	public void onUpdateTitle(UpdateTitleEvent event) {
		setTitle(event.title);
	}

}
```

```java
@EFragment(R.layout.hello_fragment)
public class HelloFragment extends BaseFragment {

	@Inject
	Bus bus;

	@Click
	void fragmentButtonClicked() {
		bus.post(new UpdateTitleEvent("Button clicked"));
	}
}
```

This project contains only a few classes, go and read the source!

## Building and running

### Command line

Here is how to build this project from the command line. 

* First, make sure you have a device plugged in so that the integration tests can run.
* Then you just need to run the following commands:

```bash
git clone git@github.com:pyricau/CleanAndroidCode.git
cd CleanAndroidCode
git submodule init
# We use a custom version of Otto, see below
cd otto
mvn clean install -DskipTests
cd ..
# Build, install on device and run the integration tests
mvn clean install
```

### Eclipse

This projects compiles and runs fine on Eclipse Juno. I have commited all the configuration files to make sure you do not spend time configuring it. You should be able to import everything "as maven projects".

> I haven't tried with IntelliJ, but since everybody keeps telling me how good this IDE is, I assume everything will work perfectly fine :) .

## Gotchas

### Dagger & AndroidAnnotations

Dagger requires the definition of entry points on an `@Module` annotation. These entry points are our activities and fragments, created by the system.

When using both Dagger & AndroidAnnotations, one obvious configuration would be:

```java
@Module(
  entryPoints = {
    HelloFragment_.class,
   	HelloAndroidActivity_.class,
  },
  complete = false
)
public class EntryPointsModule {
}
```

However, this cannot work directly when doing a full build. Even when you take care of the processor order so that you run AndroidAnnotations first and then Dagger, they run both in the same round, so the classes generated by AndroidAnnotations are not compiled yet when Dagger runs. The `@Module` annotation therefore references an unknown class.

This cannot be solved by having an extra annotation processor that generates this module at compile time. The module is picked up by Dagger on a second round, where the generated classes are compiled and therefore available.

This extra annotation processor is the `cleanandroidcode-processor` project.

Since this processor creates one class out of several annotations, it needs a caching mechanism to handle Eclipse incremental compiler.

Check out the [DaggerAAIntegrationProcessor]() TODO LINK to learn more about this processor.

### Otto & AndroidAnnotations

Unlike its Guava sister, Otto doesn't look for `@Produce` and `@Subscribe` annotations through the class hierarchy for performance reasons.

However, there is a custom version of Otto that replaces reflection with annotation processing and code generation.
It is a proof of concept from [Jake Wharton](https://github.com/JakeWharton), available on [this branch](https://github.com/square/otto/tree/code-gen) on the Square Otto repo.

## Questions?

Any question? Create a new issue (TODO link) or ask [@Piwai](http://twitter/piwai).