
### An example of how to use Deep Java Library [DJL.ai](http://wwww.djl.ai) in Scala's Akka-Http framework


The endpoint of POST /inferences
```json
{"text":"whatever"} 
```
which shoud compute text embedding and return embedding in string format
```json
{
    "vector": "Array(-0.026074253, -0.08460002, ...,"
}
```

or cURL
```shell script
curl --location --request POST 'http://127.0.0.1:8080/inferences' \
--header 'Content-Type: application/json' \
--data-raw '{"text": "whatever"}'
```


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

For TensorFlow to optimize on performance:

```
export OMP_NUM_THREADS=1
export TF_NUM_INTEROP_THREADS=1
export TF_NUM_INTRAOP_THREADS=1
```

For more information on optimization, you can check [here](
).
