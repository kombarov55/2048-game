package game.net

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, ObjectInputStream, ObjectOutputStream}

//TODO: Это медленно. Нужно подумать над другим вариантом сериализации

object Serializer {

  def serialize(data: Any): Array[Byte] = {
    val stream = new ByteArrayOutputStream()
    val writer = new ObjectOutputStream(stream)
    writer.writeObject(data)
    val result = stream.toByteArray
    writer.close()

    result
  }

  def deserialize(bytes: Array[Byte]): AnyRef = {
    val stream = new ByteArrayInputStream(bytes)
    val reader = new ObjectInputStream(stream)
    val result = reader.readObject()
    reader.close()

    result
  }

}
