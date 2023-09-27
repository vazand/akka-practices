package example

import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.ActorRef

object Excersizes1 extends App {

  /** Excersize 1 Counter Actor Create a innner value and
    *   - Increment
    *   - Decrement
    *   - Print the value
    */
  class CounterActor extends Actor {
    import CounterActor._
    private var counter = 0
    def receive: Receive = {
      case Increment => {
        counter += 1
      }
      case Decrement => {
        counter -= 1
      }
      case Print => {
        println(
          s"`${context.self.path.name}` The current counter value => $counter"
        )
      }
    }
  }

  // DOMAIN of the counter
  object CounterActor {
    case object Increment
    case object Decrement
    case object Print

  }
  val system = ActorSystem("ActorSystemOne")
  val counterActor = system.actorOf(Props[CounterActor], "MyCounterActor")

  counterActor ! CounterActor.Increment
  counterActor ! CounterActor.Print
  counterActor ! CounterActor.Increment
  counterActor ! CounterActor.Print
  counterActor ! CounterActor.Decrement
  counterActor ! CounterActor.Decrement
  counterActor ! CounterActor.Decrement
  counterActor ! CounterActor.Print

  val counterActor2 = system.actorOf(Props[CounterActor], "MyCounterActorTwo")
  import CounterActor._
  (1.to(5)).foreach(_ => counterActor2 ! Increment)
  counterActor2 ! Print

  (1.to(3)).foreach(_ => counterActor2 ! Decrement)
  counterActor2 ! Print

}

object Excersizes2 extends App {

  class BankAccount extends Actor {
    import BankAccount._
    private var BALANCE: Int = 0
    def receive: Receive = {
      case Deposit(amount) => {
        if (amount < 0) {
          sender ! TransectionFailure(s"'Invalid deposit" )
        } else {
          BALANCE += amount;
          sender ! TransectionSuccess(
            s"Cash Deposited" 
          )
        }

      }
      case Withdraw(amount) => {
        if (amount < 0)
          sender ! TransectionFailure("Invalid withdraw amount" )
        else if (amount > BALANCE) {
          sender ! TransectionFailure("Insufficient funds in the account")
        } else {
          BALANCE -= amount;
          sender() ! TransectionSuccess("Cash withdrawal success" )
        }

      }
      case Statement => {
        sender ! (s"Your current Account Balance is $BALANCE " )
      }
    }
  }
  object BankAccount {
    case class Deposit(cash: Int)
    case class Withdraw(cash: Int)
    case object  Statement
    case class TransectionSuccess(message: String)
    case class TransectionFailure(message: String)

  }

  object Person {
    case class BankHandler(ref: ActorRef)
  }

  val system = ActorSystem("BANKING-SYSTEM")
  val bankHandler = system.actorOf(Props[BankAccount], "Bank-Handler")

  class Person extends Actor {
    import Person._
    import BankAccount._
    def receive: Receive = {
      case BankHandler(actorRef) => {
        //actorRef ! Deposit(10000)
        //actorRef ! Withdraw(90000)
        //actorRef ! Withdraw(700)
        actorRef ! Statement
      }
      case message => {
        println(message.toString())
      }
    }
  }
  import Person._
  val kowsigan = system.actorOf(Props[Person],"Actor-Kowsigan")
  kowsigan ! BankHandler(bankHandler)

}
