package citi1.server

object Main {
  def main(args: Array[String]): Unit = {
    val wiring = new Wiring
    wiring.server.start()
  }
}
