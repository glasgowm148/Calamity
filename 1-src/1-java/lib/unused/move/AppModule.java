/**package actors;

import akka.actor.ActorSystem;
import akka.actor.typed.ActorRef;
import akka.actor.typed.javadsl.Adapter;
import akka.cluster.typed.Cluster;
import akka.cluster.typed.ClusterSingleton;
import akka.cluster.typed.Join;
import akka.cluster.typed.SingletonActor;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import play.Environment;
//import services.CounterActor;
//import services.CounterActor.Command;

import javax.inject.Inject;
import javax.inject.Provider;


 *See the source code in modules/AppModule.java for the logic controlling how the ActorSystem of anode participates on the cluster formation. This example application demonstrates:

the Akka Cluster API for self-joining in Dev Mode, and
the setup of seed-node in configuration for Prod Mode
but it doesn't demonstrate the Akka Cluster Bootstrap

public class AppModule extends AbstractModule {

    //@Override
    protected void configure() {
        bind(new TypeLiteral<ActorRef<Command>>() {
        })
                .toProvider(HelloActorProvider.class)
                .asEagerSingleton();
    }

    public static class HelloActorProvider implements Provider<ActorRef<Command>> {
        private final akka.actor.typed.ActorSystem<Void> actorSystem;
        private final Environment environment;

        //@Inject
        public HelloActorProvider(ActorSystem actorSystem, Environment environment) {
            this.actorSystem = Adapter.toTyped(actorSystem);
            this.environment = environment;
        }

        //@Override
        public ActorRef<Command> get() {

            Cluster cluster = Cluster.get(actorSystem);

            if (!environment.isProd()) {
                // in Dev Mode and Test Mode we want a single-node cluster so we join ourself.
                cluster.manager().tell(new Join(cluster.selfMember().address()));
            } else {
                // In Prod mode, there's no need to do anything since
                // the default behavior will be to read the seed node list
                // from the configuration.
                // If you prefer use Akka Cluster Management, then set it up here.
            }

            // Initialize the ClusterSingleton Akka extension
            ClusterSingleton clusterSingleton = ClusterSingleton.get(actorSystem);

            SingletonActor<Command> singletonActor = SingletonActor.of(CounterActor.create(), "counter-actor");
            // Use the Cluster Singleton extension to get an ActorRef to
            // the Counter Actor
            return clusterSingleton.init(singletonActor);
        }
    }
}
 */
