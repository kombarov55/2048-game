package game.net

import java.net.InetSocketAddress

object GameObserverMessages {

  object ListAllRoomsRequest extends Serializable

  case class ListAllRoomsResponse(hostAddresses: Seq[InetSocketAddress]) extends Serializable

  case class Subscribe(hostAddress: InetSocketAddress) extends Serializable

  object Subscribed extends Serializable

  object FailedToSubscribe extends Serializable

}
