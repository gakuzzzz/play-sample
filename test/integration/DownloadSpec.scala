package integration

import org.scalatest.FunSpec
import play.api.test.{FakeRequest, FakeApplication}
import play.api.test.Helpers._
import helper.TestHelper._

class DownloadSpec extends FunSpec {

  describe("#index") {
    it("should contains value") {
      running(FakeApplication()) {
        val res = route(FakeRequest(GET, "/download")).get
        assert(status(res) == 200)
        assert(contentContains(res, ",name1234,"))
      }
    }

    it("should not contains value") {
      running(FakeApplication()) {
        val res = route(FakeRequest(GET, "/download")).get
        assert(status(res) == 200)
        assert(!contentContains(res, "1234567"))
      }
    }
  }

}