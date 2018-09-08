package citi1.server
import akka.http.scaladsl.server.{Directives, Route}
import citi1.api.{KVStore, Payload}
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import play.api.libs.json.JsValue

class KVRoutes(kvStore: KVStore[JsValue, JsValue]) extends Directives with PlayJsonSupport {

  val routes: Route = pathPrefix("kvstore") {
    post {
      path("get") {
        entity(as[JsValue]) { key =>
          rejectEmptyResponse {
            complete(kvStore.get(key))
          }
        }
      } ~
      path("set") {
        entity(as[Payload[JsValue, JsValue]]) { payload =>
          complete(kvStore.set(payload.key, payload.value))
        }
      }
    }
  }
}