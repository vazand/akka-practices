package example
import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
object ActorCapabilities extends App {

    class SimpleActor extends Actor{
        def receive: Receive = {
            case message: String => println(s"[SimpleActor] I have received this `$message`")
            case numbers: Int   => println(s"[SimpleActor] I've received a number `$numbers`")
            

            case SpeacialTypeMessage(aDataOrmessage) => println(s"[SimpleActor] I received this `$aDataOrmessage`")

        }
    }
        val system = ActorSystem("actorCapabilitiesDemo")
        val simpleActor = system.actorOf(Props[SimpleActor],"simpleActor")
        simpleActor ! "Unna thaan da?"

        //  Point 1-> message can be type of any.
        simpleActor ! 1


        simpleActor ! SpeacialTypeMessage("HoldOnAndMove,Don'tLookBack")
        case class SpeacialTypeMessage(aMessage: String)

  
}
