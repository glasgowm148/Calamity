resolvers in ThisBuild += "lightbend-commercial-mvn" at
  "https://repo.lightbend.com/pass/jdDf7aPr31SE5L8dk7JQgsbIWsR9ydsBP1XT1u9hnIJCiTrL/commercial-releases"
resolvers in ThisBuild += Resolver.url("lightbend-commercial-ivy",
  url("https://repo.lightbend.com/pass/jdDf7aPr31SE5L8dk7JQgsbIWsR9ydsBP1XT1u9hnIJCiTrL/commercial-releases"))(Resolver.ivyStylePatterns)