package djlakkahttpexample

import java.util.Collections

import akka.actor.typed.ActorRef
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import djlakkahttpexample.UseExample.predict


object DjlInference {
  // actor protocol
  sealed trait Command
  final case class InferVector(text: InferenceRequest, replyTo: ActorRef[ActionPerformed]) extends Command

  final case class ActionPerformed(vector: String)
  final case class InferenceRequest(text: String)

  def apply(): Behavior[Command] = inferences()

  private def inferences(): Behavior[Command] =
    Behaviors.receiveMessage {
      case InferVector(text, replyTo) =>
        val prediciton = predict(Collections.singletonList(text.text))(0).mkString("Array(", ", ", ")")
        replyTo ! ActionPerformed(prediciton)
        Behaviors.same
    }
}
