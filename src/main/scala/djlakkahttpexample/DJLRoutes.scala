package djlakkahttpexample

import java.time.Duration

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route

import scala.concurrent.Future
import djlakkahttpexample.DjlInference._
import akka.actor.typed.ActorRef
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.AskPattern._
import akka.util.Timeout


//#import-json-formats
class DJLRoutes(djl: ActorRef[DjlInference.Command])(implicit val system: ActorSystem[_]) {

  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
  import JsonFormats._
  //#import-json-formats

  // If ask takes more time than this to complete the request is failed
  private implicit val timeout = Timeout.create(Duration.ofSeconds(60))


  def getInference(text: String): Future[ActionPerformed] = djl.ask(InferVector("text", _))

  //#all-routes
  val djlRoutes: Route =
    pathPrefix("inferences") {
      concat(
        pathEnd {
          concat(
            post {
              entity(as[String]) { user =>
                onSuccess(getInference(user)) { performed =>
                  complete((StatusCodes.OK, performed))
                }
              }
            })
        },
       )
    }
  //#all-routes
}
