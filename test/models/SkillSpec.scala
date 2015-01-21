package models

import org.scalatest.FunSpec
import org.scalatest.Matchers
import scalikejdbc._
import _root_.play.api.test._
import _root_.play.api.test.Helpers._
import models.aggregates.skill.Skill

class SkillSpec extends FunSpec with Matchers {

  describe("Skill") {
    it("find by primary keys") {
      running(FakeApplication()) {
        val id = Skill.findAll().head.id
        val maybeFound = Skill.find(id)
        assert(maybeFound.isDefined)
      }
    }
    it("find all records") {
      running(FakeApplication()) {
        val allResults = Skill.findAll()
        assert(allResults.size == 5)
      }
    }
    it("count all records") {
      running(FakeApplication()) {
        val count = Skill.countAll()
        assert(count == 5L)
      }
    }
    it("find by where clauses") {
      running(FakeApplication()) {
        val results = Skill.findAllBy(sqls.in(Skill.column.name, Seq("Ruby", "Scala")))
        assert(results.size == 2)
      }
    }
    it("count by where clauses") {
      running(FakeApplication()) {
        val count = Skill.countBy(sqls"${Skill.column.name} like ${"P%"}")
        assert(count == 1L)
      }
    }
    it("create new record") {
      running(FakeApplication()) {
        val newSkill = Skill.create(name = "ScalikeJDBC")
        assert(newSkill.id == 6)
      }
    }
    it("save a record") {
      running(FakeApplication()) {
        val entity = Skill.findAll().head
        entity.copy(name = "Erlang").save()
        val updated = Skill.find(entity.id).get
        assert(updated.name == "Erlang")
      }
    }
    it("destroy a record") {
      running(FakeApplication()) {
        val entity = Skill.findAll().head
        entity.destroy()
        assert(Skill.find(entity.id) == None)
        assert(Skill.countAll() == 4L)
      }
    }
  }

}

