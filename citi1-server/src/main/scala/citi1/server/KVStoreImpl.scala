package citi1.server

import java.util.concurrent.Executors

import akka.NotUsed
import akka.stream.{KillSwitch, KillSwitches, Materializer, OverflowStrategy}
import akka.stream.scaladsl.{Keep, Source, SourceQueueWithComplete}
import citi1.api.{KVStore, KeyUpdate}

import scala.concurrent.{ExecutionContext, Future}

class KVStoreImpl[K, V](implicit mat: Materializer) extends KVStore[K, V] {

  implicit val ec: ExecutionContext = ExecutionContext.fromExecutorService(Executors.newSingleThreadExecutor())

  private[this] var data: Map[K, V] = Map.empty

  val (queue, stream) = Source.queue[KeyUpdate[K, V]](256, OverflowStrategy.dropHead).preMaterialize()

  override def set(key: K, value: V): Future[Boolean] = Future {
    data = data + (key -> value)
    queue.offer(KeyUpdate(key, Some(value)))
    true
  }

  override def get(key: K): Future[Option[V]] = Future {
    data.get(key)
  }

  override def watch(key: K): Source[KeyUpdate[K, V], KillSwitch] = {
    stream.filter(_.key == key).viaMat(KillSwitches.single)(Keep.right)
  }
}
