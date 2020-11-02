
package startup;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import modules.MLLibModule;
import play.ApplicationLoader;
import play.inject.guice.GuiceApplicationBuilder;
import play.inject.guice.GuiceApplicationLoader;

public class AppLoader extends GuiceApplicationLoader {

    private final Config config;

    /**
     * Default constructor
     */
    public AppLoader() { this.config = ConfigFactory.load(); }

    @Override
    public GuiceApplicationBuilder builder(ApplicationLoader.Context context) {
        return initialBuilder
                .in(context.environment())
                .loadConfig(config)
                .bindings(new MLLibModule());
    }
}