package citi1
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, RequestEntity, StatusCodes}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import citi1.api.KVStore
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import play.api.libs.json.Format

import scala.async.Async._
import scala.concurrent.Future

class KVClient[K: Format, V: Format](baseUri: String)(implicit actorSystem: ActorSystem)
    extends KVStore[K, V]
    with PlayJsonSupport {
  import actorSystem.dispatcher
  implicit val mat: ActorMaterializer = ActorMaterializer()

  override def set(key: K, value: V): Future[Boolean] = async {
    val payload = Payload(key, value)
    val request = HttpRequest()
      .withMethod(HttpMethods.POST)
      .withUri(s"$baseUri/kvstore/set")
      //TODO: create extension method with payload
      .withEntity(await(Marshal(payload).to[RequestEntity]))

    val response = await(Http().singleRequest(request))

    await(Unmarshal(response.entity).to[Boolean])
  }

  override def get(key: K): Future[Option[V]] = async {
    val request = HttpRequest()
      .withMethod(HttpMethods.POST)
      .withUri(s"$baseUri/kvstore/get")
      .withEntity(await(Marshal(key).to[RequestEntity]))

    val response = await(Http().singleRequest(request))

    response.status match {
      case StatusCodes.OK       => Some(await(Unmarshal(response.entity).to[V]))
      case StatusCodes.NotFound => None
      case _                    => throw new RuntimeException(response.toString)
    }
  }
}
