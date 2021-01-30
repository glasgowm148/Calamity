
```
app                      → Application sources
└ assets                → Compiled asset sources
└ stylesheets        → Typically LESS CSS sources
└ javascripts        → Typically CoffeeScript sources
└ controllers           → Application controllers
└ models                → Application business layer
└ views                 → Templates
build.sbt                → Application build script
conf                     → Configurations files and other non-compiled resources (on classpath)
└ application.conf      → Main configuration file
└ routes                → Routes definition
dist                     → Arbitrary files to be included in your projects distribution
public                   → Public assets
└ stylesheets           → CSS files
└ javascripts           → Javascript files
└ images                → Image files
project                  → sbt configuration files
└ build.properties      → Marker for sbt project
└ plugins.sbt           → sbt plugins including the declaration for Play itself
lib                      → Unmanaged libraries dependencies
logs                     → Logs folder
└ application.log       → Default log file
target                   → Generated stuff
└ resolution-cache      → Info about dependencies
└ scala-2.13
└ api                → Generated API docs
└ classes            → Compiled class files
└ routes             → Sources generated from routes
└ twirl              → Sources generated from templates
└ universal             → Application packaging
└ web                   → Compiled web assets
test                     → source folder for unit or functional tests
```
# Settings for Stanford CoreNLP
export CORENLP_ROOT="/Users/pseudo/Documents/GitHub/HelpMe/corenlp"
export CLASSPATH="$CORENLP_ROOT/javanlp-core.jar"
export CLASSPATH="$CLASSPATH:$CORENLP_ROOT/stanford-corenlp-models-current.jar"
for file in `find $CORENLP_ROOT -name "*.jar"`
do
    export CLASSPATH="$CLASSPATH:`$file`"
done
alias java_ls='/usr/libexec/java_home -V 2>&1 | grep -E "\d.\d.\d_\d\d" | cut -d , -f 1 | colrm 1 4 | grep -v Home'

function java_use() {
    export JAVA_HOME=$(/usr/libexec/java_home -v $1)
    export PATH=$JAVA_HOME/bin:$PATH
    java -version
}
for file in `find '/Users/pseudo/Documents/GitHub/HelpMe/corenlp' -name "*.jar"`; do export
CLASSPATH="$CLASSPATH:`realpath $file`"; done
"~/.zshenv" 18L, 729C



# notes
plugins.sbt has deleted itself twice now
```Bash
// The Play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.8.2")

// Defines scaffolding (found under .g8 folder)
// http://www.foundweekends.org/giter8/scaffolding.html
// sbt "g8Scaffold form"
addSbtPlugin("org.foundweekends.giter8" % "sbt-giter8-scaffold" % "0.11.0")

// https://github.com/saksdirect/sbt-dependency-graph-sugar
addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.10.0-RC1")
```
# Play Java Akka Cluster Example

This example demonstrates how to setup the Play-provided `ActorSystem` to build a [cluster](https://doc.akka.io/docs/akka/current/typed/cluster
.html).

The example is a very simple Play application with a counter in an [actor](app/services/CounterActor.java).

When creating a counter in a system with multiple nodes running we have two options:

1. Keep the count on a database and block on each concurrent access
2. Keep the count in memory with eventual copies on the database and have that memory copy be a persistent singleton across our cluster.

This application demonstrates how to use an [Akka Cluster Singleton](https://doc.akka.io/docs/akka/current/typed/cluster-singleton.html#example) in
 Play to implement the counter.
 
To turn a non-clustered Play application to a clustered one you need to do the following:

1. include the dependency to `"com.typesafe.akka" %% "akka-cluster-typed" % PlayVersion.akkaVersion` (see [build.sbt](build.sbt)).
2. use one of the available methods to form a cluster [provided by Akka Cluster](https://doc.akka.io/docs/akka/current/typed/cluster.html#joining).

### Considerations for a clustered Play application in Dev Mode

But when you try to run a clustered Play application in Dev Mode there is a problem. In most common cases, clustering an application should require
 at least three nodes. So if so many nodes are required, how can a clustered Play application run in Dev Mode if there's only one node? The answer
  is "forming a one-node cluster". In Dev Mode, then, we will use a special mechanism to join the cluster in which the Application's `ActorSystem
  ` joins itself.
  
See the source code in [`modules/AppModule.java`](modules/AppModule.java) for the logic controlling how the `ActorSystem` of anode participates on
 the cluster formation. This example application demonstrates:
 
1. the [Akka Cluster API](https://doc.akka.io/docs/akka/current/typed/cluster.html#joining-programmatically-to-seed-nodes) for self-joining in Dev Mode, and 
2. the setup of [`seed-node` in configuration](https://doc.akka.io/docs/akka/current/typed/cluster.html#joining-configured-seed-nodes) for Prod Mode
 
but it doesn't demonstrate the [`Akka Cluster Bootstrap`](https://doc.akka.io/docs/akka/current/typed/cluster.html#joining-automatically-to-seed-nodes-with-cluster-bootstrap)

## Running this sample in Dev Mode

To run this sample in `Dev Mode` use the regular 

`sbt run` 

and open the URL http://localhost:9000/.

## Running this sample in Production Mode

You can run this sample in `Production Mode` on your local machine. This section will guide you on starting 3 different nodes in your local machine
 and forming a cluster between them. The following steps will package the application and prepare folder with necessary artifacts for the execution: 
 
1. open a terminal and change directory to the `play-java-akka-cluster-example` folder.
2. package the application using `sbt dist` from the 
3. locate the file `play-java-akka-cluster-example/target/universal/play-java-akka-cluster-example-1.0-SNAPSHOT.zip`, copy it to a folder of your
 choice and `unzip` it.

> NOTE: This sample application ships with extra config files that make it very easy to run multiple nodes on a single machine. If you want to
> deploy a Play application with Akka Cluster you should refer to the Akka and Play documentation for extra considerations regarding port biding
>, secret management, [cluster bootstrapping[(https://doc.akka.io/docs/akka/current/typed/cluster.html#joining-automatically-to-seed-nodes-with
>-cluster-bootstrap), etc...


Open three separate terminals on the folder where you unzipped the file and run the following commands (one on each terminal):
 
`bin/play-java-akka-cluster-example  -Dconfig.file=local1.conf`

`bin/play-java-akka-cluster-example  -Dconfig.file=local2.conf`

`bin/play-java-akka-cluster-example  -Dconfig.file=local3.conf`

In the terminals you should see activity like:


```
2020-09-01 11:40:45 INFO  akka.cluster.Cluster Cluster(akka://application) Cluster Node [akka://application@127.0.0.1:25521] - Node [akka://application@127.0.0.1:25521] is JOINING itself (with roles [dc-default]) and forming new cluster
2020-09-01 11:40:45 INFO  akka.cluster.Cluster Cluster(akka://application) Cluster Node [akka://application@127.0.0.1:25521] - is the new leader among reachable nodes (more leaders may exist)
2020-09-01 11:40:45 INFO  akka.cluster.Cluster Cluster(akka://application) Cluster Node [akka://application@127.0.0.1:25521] - Leader is moving node [akka://application@127.0.0.1:25521] to [Up]
```

The logs above indicate node 1 (identified as `akka://application@127.0.0.1:25521`) and node 2 (identified as `akka://application@127.0.0.1:25522
`) have seen each other and established a connection, then `akka://application@127.0.0.1:25521` became the leader and that leader decided to mark
 `akka://application@127.0.0.1:25521`'s status as `Up`. Finally, the singleton `counter-actor` that we use on this sample app is available. You
  should also see, in the logs, how the node 3 (identified as `akka://application@127.0.0.1:25523`) also joins the cluster.

Finally, open three browser tabs to the URLs http://localhost:9001/, http://localhost:9002/, and http://localhost:9003/ (each points to a different
 Play instance) and interact with the UI. Note how all three increment a single counter in the singleton.

