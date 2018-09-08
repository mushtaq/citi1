package citi1.server

import java.util.concurrent.Executors

import citi1.api.KVStore

import scala.concurrent.{ExecutionContext, Future}

class KVStoreImpl[K, V] extends KVStore[K, V] {

  implicit val ec: ExecutionContext = ExecutionContext.fromExecutorService(Executors.newSingleThreadExecutor())

  private[this] var data: Map[K, V] = Map.empty

  override def set(key: K, value: V): Future[Boolean] = Future {
    data = data + (key -> value)
    true
  }

  override def get(key: K): Future[Option[V]] = Future {
    data.get(key)
  }
}
