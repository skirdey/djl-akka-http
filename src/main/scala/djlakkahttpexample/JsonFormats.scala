package djlakkahttpexample

import djlakkahttpexample.DjlInference.{ActionPerformed, InferenceRequest}

//#json-formats
import spray.json.DefaultJsonProtocol

object JsonFormats  {
  import DefaultJsonProtocol._

  implicit val actionPerformedJsonFormat = jsonFormat1(ActionPerformed)
  implicit val inferenceRequestJsonFormat = jsonFormat1(InferenceRequest)
}
//#json-formats
