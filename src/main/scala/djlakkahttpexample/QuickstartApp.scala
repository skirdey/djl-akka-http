package djlakkahttpexample

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route

import scala.util.Failure
import scala.util.Success



//#main-class
object QuickstartApp {
  //#start-http-server
  private def startHttpServer(routes: Route)(implicit system: ActorSystem[_]): Unit = {
    // Akka HTTP still needs a classic ActorSystem to start
    import system.executionContext


    val futureBinding = Http().newServerAt("localhost", 8080).bind(routes)
    futureBinding.onComplete {
      case Success(binding) =>
        val address = binding.localAddress
        system.log.info("Server online at http://{}:{}/", address.getHostString, address.getPort)
      case Failure(ex) =>
        system.log.error("Failed to bind HTTP endpoint, terminating system", ex)
        system.terminate()
    }
  }
  //#start-http-server
  def main(args: Array[String]): Unit = {
    System.setProperty("org.bytedeco.javacpp.maxphysicalbytes", "0") // override to avoid memory overflow
    System.setProperty("org.bytedeco.javacpp.maxbytes", "0")
    //#server-bootstrapping
    val rootBehavior = Behaviors.setup[Nothing] { context =>
      val djlInferenceActor = context.spawn(DjlInference(), "DjlInferenceActor")
      context.watch(djlInferenceActor)

      val routes = new DJLRoutes(djlInferenceActor)(context.system)
      startHttpServer(routes.djlRoutes)(context.system)

      Behaviors.empty
    }
    val system = ActorSystem[Nothing](rootBehavior, "DjlAkkaHttpServer")
    //#server-bootstrapping
  }
}
//#main-class
