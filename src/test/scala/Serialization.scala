import game.net.Serializer
import org.scalatest.FlatSpec

class Serialization extends FlatSpec {

  "strings" should "be equal before and after serialization" in {
    val data = "afsdafafsasfd"
    val bytes = Serializer.serialize(data)
    println("bytes: " + bytes)
    val str = Serializer.deserialize(bytes).asInstanceOf[String]
    println("string again: " + str)
    assert(data == str)
  }

}
