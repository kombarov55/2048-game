package game.net.model

import java.io.Serializable
import java.net.InetSocketAddress

import game.model.Cell

object GameObserverMessages {

  case class ListAllRoomsRequest(callback: (Seq[InetSocketAddress]) => Unit) extends Serializable

  case class ListAllRoomsResponse(hostAddresses: Seq[InetSocketAddress]) extends Serializable

  case class Subscribe(hostAddress: InetSocketAddress, onTurnMade: (Seq[Cell], Int) => Unit = null) extends Serializable

  object Subscribed extends Serializable

  object FailedToSubscribe extends Serializable

}
