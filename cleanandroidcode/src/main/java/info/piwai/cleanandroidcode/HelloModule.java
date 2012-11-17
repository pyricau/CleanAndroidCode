package info.piwai.cleanandroidcode;

import info.piwai.cleanandroidcode.base.BaseModule;
import dagger.Module;

@Module(

/*
 * TODO can't use AA because it can't reference entry point that doesn't exists
 * yet => should create an annotation processor for that purpose, as a separate
 * maven module.
 */
entryPoints = { HelloAndroidActivity.class, HelloFragment.class },

includes = { BaseModule.class }

)
public class HelloModule {

}
