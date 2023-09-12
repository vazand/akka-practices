package example

import akka.actor.ActorSystem
import akka.actor.Actor
import akka.actor.Props

object Hello extends App {
  // part 1 -> actorsystem
  val actorSystem = ActorSystem("firstActorSystem")
  println(actorSystem.name)

  // part 2 -> create actors
  class WordCounter extends Actor {
    // internal data
    var total_words = 0

    // behavior
    def receive: PartialFunction[Any, Unit] = { // use Receive from now on.
      case message: String => {
        total_words = message.split(" ").length
        println(
          s"${this.getClass().getName()} received this: \"${message}\"and the word count for this is : ${total_words}"
        )
      }
      case msg => {
        println(
          s"${this.getClass().getName()} I can't understand this: ${msg.toString()}"
        )
      }
    }
  }

  // part 3 -> Instantiate an actor
  val wordCounter = actorSystem.actorOf(Props[WordCounter], "wordCounter")

  // part 4 -> communicating
  // wordCounter.!("I'm learning Akka and It's looks easy like interacting with actors") // can use also this to communicate
  wordCounter ! "I'm learning Akka and It's looks easy like interacting with actors"
  wordCounter ! 11 // "tell"

  // ascynchronous

  val anotherWordCounter =
    actorSystem.actorOf(props = Props[WordCounter], name = "anotherWordCounter")
  anotherWordCounter ! "Another data sending"

  /*   class Person(name: String) extends Actor{
      def receive: Receive = {
        case hi: String => println(s"my name is ${name}")
        case _ =>
      }
  } */

  // not a good way to use props for a class that has argument supplied
  // val person = actorSystem.actorOf(Props(new Person("vazand"))) 

  class Person(name: String) extends Actor {
    def receive: Receive = {
      case "hi"  => println(s"my name is ${name}")
      case _          =>
    }
  }

  // use like this:
  object Person {
    def props(name: String) = Props(new Person(name))
  }

  val person = actorSystem.actorOf(Person.props("Vazand"))
  person ! "hi"

  System.exit(0)

}
