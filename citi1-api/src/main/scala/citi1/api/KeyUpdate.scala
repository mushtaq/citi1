package citi1.api
import play.api.libs.json.{Format, Json, OFormat}

case class KeyUpdate[K, V](key: K, value: Option[V])

object KeyUpdate {
  implicit def format[K: Format, V: Format]: OFormat[KeyUpdate[K, V]] = Json.format[KeyUpdate[K, V]]
}
