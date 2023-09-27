package example

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props

object ChangingActorBehaviour extends App{

    val system =  ActorSystem("ChangingActorBehaviour")
    val mom = system.actorOf(Props[Mom],"mom1")
    //val fussyKid = system.actorOf(Props[FussyKid],"FussyKid1")
    //mom ! Mom.MomStart(fussyKid)
    val statelessFussyKid = system.actorOf(Props[StatelessFussyKid],"StateLessFussyKid1")
    mom ! Mom.MomStart(statelessFussyKid)


    object FussyKid{
        case object KidAccept
        case object KidReject
        val HAPPY = "happy"
        val SAD  = "sad"
    }
    class FussyKid extends Actor{
        import FussyKid._
        import Mom._
        var state = HAPPY
        override def receive: Receive = {
            case Food(VEGETABLE) => {
                state = SAD
            }
            case Food(CHOCOLATE) => {
                state = HAPPY
            
            }
            case Ask(_) => {
                if(state == HAPPY){
                    sender() ! KidAccept
                }else{
                    sender() ! KidReject
                }
            }
        }
    }
    object Mom{
        case class MomStart(kidRef: ActorRef)
        case class Ask(msg:String)   
        case class Food(food:String) 
        val VEGETABLE = "Veggies"
        val CHOCOLATE = "Chocolate"  
    }
    class Mom extends Actor{
        import Mom._
        import FussyKid._
        override def receive: Receive = {
            case MomStart(kidRef) => {
                kidRef ! Food(VEGETABLE)
                kidRef ! Food(VEGETABLE)
                //kidRef ! Ask("Do you want to play?")
                kidRef ! Food(CHOCOLATE)
                kidRef ! Ask("Do you want to play?")

            }
            case KidAccept => println("My Kid is happy")
            case KidReject => println("My Kid is sad, but as healthy!")
        }
    }


    // stateless fussy kid
    
    class StatelessFussyKid extends Actor{
        import FussyKid._
        import Mom._
        override def receive: Receive = {
            happyReceive
        }
        def happyReceive: Receive = {
            case Food(VEGETABLE)=> context.become(sadReceive)
            case Food(CHOCOLATE) =>
            case Ask(_) => {
                sender() ! KidAccept
            } 
        }
        def sadReceive : Receive = {
            case Food(VEGETABLE)=> 
            case Food(CHOCOLATE)=> context.become(happyReceive)
            case Ask(_) => sender() ! KidReject
        }
    }
}