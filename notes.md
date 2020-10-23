https://github.com/playframework/play-samples/tree/2.8.x/play-java-akka-cluster-example

https://blog.contactsunny.com/data-science/removing-stop-words-in-java-as-part-of-data-cleaning-in-artificial-intelligence

https://stackoverflow.com/questions/21085187/reading-files-from-directory-in-play-framework-on-heroku

### See java versions

/usr/libexec/java_home -V
export JAVA_HOME=`/usr/libexec/java_home -v 1.8`
javac -version

## Akka

http://www.reactive-streams.org
https://github.com/JDoIt/akka-stream-twitter-sentiment
https://dzone.com/articles/streaming-twitter-api-akka-and


http://felipeforbeck.com/posts/2015/01/tweet-sentiment-analyst/#.X5KfwC9Q1hE
>Java; Play Framework; Spring; Spring Data; Hibernate; WebSockets; Akka; Twitter4J Search & Stream API; Redis; HPVerticaDB; HPIdol OnDemand API; Twitter Bootstrap 3; JQuery 1.11; Highcharts.

### When should developers consider starting with Lagom Framework?

Lagom Framework helps developers build microservices as systems — Reactive systems, to be precise — so that your microservices are elastic and resilient from within. Lagom is built on top of Play Framework and Akka Cluster, with a programming model based on an RPC style to simplify communication between microservices as well as a persistence layer based on the concept of Entities as described by Domain Driven Design.

Lagom is designed to support RPC-style communication between microservices, including Command Query Responsibility Segregation (CQRS) and Event Sourcing (ES). Lagom features integration with Apache Kafka and Apache Cassandra, plus abstractions for Service Location for integrating with DNS, Kubernetes, Zookeeper etc.

We often recommend that our clients adopt Lagom Framework if their use cases are specifically focused on:

RPC-style, backend microservices
CQRS/ES persistence strategies

https://github.com/piyushknoldus/lagom-scala-wordcount.g8/wiki/Working-ScreenShots
[Lagom Framework Java JPA CRUD example](https://github.com/taymyr/lagom-samples/tree/master/jpa-crud/java-sbt)