package citi1.server

import akka.NotUsed
import akka.http.scaladsl.model.sse.ServerSentEvent
import akka.http.scaladsl.server.{Directives, Route}
import citi1.api.{KVStore, Payload}
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import play.api.libs.json.{JsValue, Json}
import akka.http.scaladsl.marshalling.sse.EventStreamMarshalling._

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
      } ~
      path("watch") {
        entity(as[JsValue]) { key =>
          rejectEmptyResponse {
            val value = kvStore.watch(key).map(x => ServerSentEvent(Json.toJson(x).toString()))
            complete(value)
          }
        }
      }
    }
  }
}
