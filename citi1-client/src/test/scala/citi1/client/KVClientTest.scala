package citi1.client
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import citi1.api.KVStore
import org.scalatest.{FunSuite, Matchers}

import scala.concurrent.duration.DurationLong
import scala.concurrent.{Await, Future}

class KVClientTest extends FunSuite with Matchers with DomainJsonSupport {

  test("set-get") {
    implicit val actorSystem: ActorSystem = ActorSystem("test")
    val store: KVStore[Id, Person]        = new KVClient[Id, Person]("http://localhost:8080")

    val id     = Id("1")
    val person = Person("mushtaq", 20)

    store.get(id).block shouldBe None
    store.set(id, person).block shouldBe true
    store.get(id).block.get shouldBe person
  }

  implicit class BlockingFuture[T](f: Future[T]) {
    def block: T = Await.result(f, 5.seconds)
  }

  test("watch") {
    implicit val actorSystem: ActorSystem        = ActorSystem("test")
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    val store: KVStore[Id, Person]               = new KVClient[Id, Person]("http://localhost:8080")

    val id = Id("1")
    store.watch(id).runForeach(println)

    val person = Person("mushtaq", 20)
    store.set(id, person)
    store.set(id, person.copy("abc"))

    Thread.sleep(5000)

  }
}
