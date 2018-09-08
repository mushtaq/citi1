package citi1.server
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.{ActorMaterializer, Materializer}

import scala.concurrent.Future

class KVServer(kvRoutes: KVRoutes)(implicit actorSystem: ActorSystem) {
  implicit val mat: Materializer          = ActorMaterializer()
  def start(): Future[Http.ServerBinding] = Http().bindAndHandle(kvRoutes.routes, "0.0.0.0", 8080)
}
