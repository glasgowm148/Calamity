package modules;


import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import controllers.HomeController;
import ml.ModelPrediction;

import javax.inject.Singleton;

public class MLLibModule  extends AbstractModule {

/**
 * Configure the module
 */
@Override
protected void configure() {

requestStaticInjection(HomeController.class);
}

@Singleton
@Provides
ModelPrediction provideLogger() {
return new ModelPrediction();
}
}