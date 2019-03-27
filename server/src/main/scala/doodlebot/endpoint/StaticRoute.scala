package doodlebot
package endpoint

import cats.effect.IO
import org.http4s.{Header, Headers, HttpService, Response}
import org.http4s.dsl.Http4sDsl
import fs2.io.readInputStream

object StaticRoute extends Http4sDsl[IO] {

  private val resourceLoader = this.getClass()

  private val CHUNK_SIZE = 1024

  val route = HttpService[IO] {
    case GET -> Root / "index" => {
      IO {
        Response[IO](
          Ok,
          body    = readInputStream(IO(resourceLoader.getResourceAsStream("index.html")), chunkSize = CHUNK_SIZE),
          headers = Headers(Header("Content-Type", "application/javascript"))
        )
      }
    }
    case GET -> "ui" /: rest => {
      IO {
        val resourcePath = rest.toList.mkString("/", "/", "")
        println(s"Loading resource $resourcePath")
        Response[IO](
          Ok,
          body    = readInputStream(IO(resourceLoader.getResourceAsStream(resourcePath)), chunkSize = CHUNK_SIZE),
          headers = Headers(Header("Content-Type", "application/javascript"))
        )
      }
    }
  }

}
