package djlakkahttpexample

//#test-top
import akka.actor.testkit.typed.scaladsl.ActorTestKit
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import akka.http.scaladsl.testkit.{RouteTestTimeout, ScalatestRouteTest}
import akka.testkit.TestDuration
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.duration.DurationInt

//#set-up
class DJLRoutesSpec extends WordSpec with Matchers with ScalaFutures with ScalatestRouteTest {
  //#test-top


  implicit val timeout = RouteTestTimeout(60.seconds.dilated)

  // the Akka HTTP route testkit does not yet support a typed actor system (https://github.com/akka/akka-http/issues/2036)
  // so we have to adapt for now
  lazy val testKit = ActorTestKit()
  implicit def typedSystem = testKit.system
  override def createActorSystem(): akka.actor.ActorSystem =
    testKit.system.classicSystem

  val inference = testKit.spawn(DjlInference())
  lazy val routes = new DJLRoutes(inference).djlRoutes

  // use the json formats to marshal and unmarshall objects in the test
  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
  import JsonFormats._
  //#set-up

  //#actual-test
  "DJLRoutes" should {
    //#actual-test

    //#testing-post
    "be able to perform djl inference (POST /inferences)" in {
      val text = "Hello World"
      val textEntity = Marshal(text).to[MessageEntity].futureValue // futureValue is from ScalaFutures

      // using the RequestBuilding DSL:
      val request = Post("/inferences").withEntity(textEntity)

      request ~> routes ~> check {
        status should ===(StatusCodes.OK)

        // we expect the response to be json:
        contentType should ===(ContentTypes.`application/json`)

        // and we know what message we're expecting back:
        entityAs[String] should ===("""{"vector":"-0.06273459"}""")
      }
    }
    //#testing-post

  }
  //#actual-test

  //#set-up
}
//#set-up
