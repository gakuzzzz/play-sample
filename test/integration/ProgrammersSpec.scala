package integration

import play.api.test.FakeRequest
import org.scalatestplus.play._
import play.api.test.Helpers._

class ProgrammersSpec extends PlaySpec with OneAppPerSuite {

  "Programmers" should {
    "render index" in {
      val res = route(FakeRequest(GET, "/programmers")).get
      assert(status(res) == 200)
      val content = contentAsString(res)
      assert(content.contains("Alice"))
      assert(content.contains("Bob"))
      assert(content.contains("Chris"))
      assert(content.contains("David"))
    }

    "render show" in {
      val res = route(FakeRequest(GET, "/programmers/1")).get
      assert(status(res) == 200)
      val content = contentAsString(res)
      assert(content.contains("Alice"))
      assert(!content.contains("Bob"))
    }

    "create" in {
      val res = route(FakeRequest(POST, "/programmers").withFormUrlEncodedBody("name" -> "name1")).get
      assert(status(res) == 204)
      val top = route(FakeRequest(GET, "/programmers")).get
      val content = contentAsString(top)
      assert(content.contains("Alice"))
      assert(content.contains("name1"))
    }

    "delete" in {
      val res = route(FakeRequest(DELETE, "/programmers/1")).get
      assert(status(res) == 204)
      val top = route(FakeRequest(GET, "/programmers")).get
      val content = contentAsString(top)
      assert(!content.contains("Alice"))
    }

    "add skill" in {
      // TODO:
      assert(true)
    }

    "delete skill" in {
      // TODO:
      assert(true)
    }

    "join company" in {
      // TODO:
      assert(true)
    }

    "join organization" in {
      // TODO:
      assert(true)
    }

  }

}
