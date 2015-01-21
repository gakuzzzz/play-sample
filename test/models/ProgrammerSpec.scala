package models

import _root_.play.api.test._
import org.scalatest.{Matchers, FunSpec}
import scalikejdbc._
import _root_.play.api.test.Helpers._

class ProgrammerSpec extends FunSpec with Matchers {

  describe("Programmer") {
    it("find with skills") {
      running(FakeApplication()) {
        val alice = Programmer.findAllBy(sqls.eq(Programmer.p.name, "Alice")).head
        assert(alice.skills.size == 2)
      }
    }
    it("find no skill programmers") {
      running(FakeApplication()) {
        val noSkillProgrammers = Programmer.findNoSkillProgrammers()
        assert(noSkillProgrammers.size == 2)
      }
    }
    it("find by primary keys") {
      running(FakeApplication()) {
        val id = Programmer.findAll().head.id
        val maybeFound = Programmer.find(id)
        assert(maybeFound.isDefined)
      }
    }
    it("find all records") {
      running(FakeApplication()) {
        val allResults = Programmer.findAll()
        assert(allResults.size == 4)
      }
    }
    it("count all records") {
      running(FakeApplication()) {
        val count = Programmer.countAll()
        assert(count == 4L)
      }
    }
    it("find by where clauses") {
      running(FakeApplication()) {
        val results = Programmer.findAllBy(sqls.isNotNull(Programmer.p.companyId))
        assert(results.head.name == "Alice")
      }
    }
    it("count by where clauses") {
      running(FakeApplication()) {
        val count = Programmer.countBy(sqls.isNull(Programmer.p.companyId))
        assert(count == 1L)
      }
    }
    it("create new record") {
      running(FakeApplication()) {
        val martin = Programmer.create("Martin")
        assert(martin.id == 5)
      }
    }
    it("save a record") {
      running(FakeApplication()) {
        val entity = Programmer.findAll().head
        entity.copy(name = "Eric").save()
        val updated = Programmer.find(entity.id).get
        assert(updated.name == "Eric")
      }
    }
    it("destroy a record") {
      running(FakeApplication()) {
        val entity = Programmer.findAll().head
        entity.destroy()
        assert(Programmer.find(entity.id) == None)
        assert(Programmer.countAll == 3L)
      }
    }
  }

}

