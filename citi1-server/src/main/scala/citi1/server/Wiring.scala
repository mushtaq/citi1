package citi1.server
import akka.actor.ActorSystem
import citi1.api.KVStore
import play.api.libs.json.JsValue

class Wiring {
  lazy implicit val actorSystem: ActorSystem  = ActorSystem("kvstore")
  lazy val kvStore: KVStore[JsValue, JsValue] = new KVStoreImpl[JsValue, JsValue]
  lazy val routes                             = new KVRoutes(kvStore)
  lazy val server                             = new KVServer(routes)
}
