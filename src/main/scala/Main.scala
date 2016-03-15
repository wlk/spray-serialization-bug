import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.http.scaladsl.server.Directives._
import spray.json.DefaultJsonProtocol
import akka.http.scaladsl.model.StatusCodes._
import org.scalatest.{FlatSpec, Matchers}


trait Api extends DefaultJsonProtocol {

  val routes = {
    path("test") {
      get {
        complete {
          val myMap: Map[Int, String] = Map(1 -> "A", 2 -> "B")
          myMap
        }
      }
    }
  }
}

class ASpec extends FlatSpec with ScalatestRouteTest with Matchers with Api {
  "Map" should "be serialized correctly" in {
    Get("/test") ~> routes ~> check {
      //Getting:
      // [ERROR] [03/15/2016 08:33:37.832] [ScalaTest-run-running-ASpec] [akka.actor.ActorSystemImpl(ASpec)] Error during processing of request HttpRequest(HttpMethod(GET),http://example.com/test,List(),HttpEntity.Strict(none/none,ByteString()),HttpProtocol(HTTP/1.1))
      // spray.json.SerializationException: Map key must be formatted as JsString, not '1'
      // And this test fails due to an error
      status shouldBe OK
      val result = responseAs[Map[Int, String]]
      result(1) shouldBe "A"
    }
  }
}