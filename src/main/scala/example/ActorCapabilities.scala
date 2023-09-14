package example
import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.ActorRef
object ActorCapabilities extends App {

  class SimpleActor extends Actor {
    def receive: Receive = {
    case "Hello" => sender() ! s"Hello There From ${sender().path.name} and I am ${context.self.path.name}"
      case message: String =>
        println(s"${context.self.path.name} I have received this `$message`")
      case numbers: Int => println(s"${self} I've received a number `$numbers`")

      case SpeacialTypeMessage(aDataOrmessage) =>
        println(s"${context.self.path} I received this `$aDataOrmessage`")

      case SendMessageToMySelf(content) => {
        // It's first get data when passed to it
        // and it also forward the data to self
        // so the data will be sent again to self
        // and the data is also a string so it handled by the first case
        self ! content
      }

      case SayHello(actorRef)=> {
        actorRef ! "Hello"
        actorRef ! 43
        self ! "Hello"
        // we're sending message to an ActorRef so it'll go to the ActorRef 
        // and the data is a message so it handled by message
        // if the data is another type then it will be handled by the respective cases
      }

      case WirelessPhoneMessage(data, actorRef) =>{
        actorRef forward(data+" Hehe")
      }

    }
  }
  val system = ActorSystem("actorCapabilitiesDemo")
  val simpleActor = system.actorOf(Props[SimpleActor], "MySimpleActor")
  simpleActor ! "Unna thaan da?"

  //  Point 1 -> message can be type of any.
  // a message must be Immutable
  // need to be serializable
  // use case object and case class when practices
  simpleActor ! 1

  simpleActor ! SpeacialTypeMessage("HoldOnAndMove,Don'tLookBack")
  case class SpeacialTypeMessage(aMessage: String)

  // Point 2 -> actor has informations about their context and themselves
  // context.self === `this` in OOP

  case class SendMessageToMySelf(myInfo: String)

  simpleActor ! SendMessageToMySelf("I'm gonna be a master of programming")

  // Point 3 -> actor can reply messages!

  val alice = system.actorOf(Props[SimpleActor],"alice")
  val bob = system.actorOf(Props[SimpleActor],"bob")
  case class SayHello(ref: ActorRef)
  alice ! SayHello(bob)


  // Point 4 -> dead letters
  // this will receive DeadLetter 
  // Because If there is no one to send and it trigger the deadletter 
  // deadletter is like a garbage collector in akka actors
  alice ! "Hello" // will receive DeadLetter


  // Point 5 -> Forwarding a message
  // someone -> alice -> bob 
  case class WirelessPhoneMessage(content:String, ref:ActorRef)
  alice ! WirelessPhoneMessage("Hello",bob)







}
