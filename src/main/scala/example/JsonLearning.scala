package example


import play.api.libs.json.Json._
import play.api.libs.json._

object JsonLearning extends App{
  val jsonString: String = """{"name": "John", "age": 30}"""
  val json: JsValue = Json.parse("""{"name": "John", "age": 30}""")
  val personOne = Person("Vazand",24)
  val jsonPersonOne = Json.toJson(personOne).toString()
  val asStr = Json.parse(jsonPersonOne)

  println(asStr)
  
  //println(jsonString)
  //println(json.toString)
}
case class Person(name: String, age: Int)
object Person{
  implicit val formatter: Format[Person] = Json.format[Person]

}

/**
  *   jsonPersonOne match {
    case JsObject(underlying) => {
      
    }
    case JsString(value) => println("JsString")
    case JsNull =>
    case _: JsBoolean =>
    case JsArray(value) =>
    case JsNumber(value) =>
  }
  */