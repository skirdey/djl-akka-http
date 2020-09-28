
### An example of how to use Deep Java Library [DJL.ai](http://wwww.djl.ai) in Scala's Akka-Http framework


The endpoint of POST {"text":"whatever"} to /inferences which shoud compute text embedding and return embedding in string format


Install SBT, for macOS - https://www.scala-sbt.org/1.x/docs/Installing-sbt-on-Mac.html

Run service:
```bash
sbt run
```

You should see `Server online at http://127.0.0.1:8080/`

Run unit tests:
```bash
sbt test
```