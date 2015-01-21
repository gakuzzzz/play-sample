package models

import org.scalatest.FunSpec
import org.scalatest.Matchers
import scalikejdbc._
import _root_.play.api.test._
import _root_.play.api.test.Helpers._
import models.aggregates.company.Company

class CompanySpec extends FunSpec with Matchers {

  describe("Company") {
    it("find by primary keys") {
      running(FakeApplication()) {
        val id = Company.findAll().head.id
        val maybeFound = Company.find(id)
        assert(maybeFound.isDefined)
      }
    }
    it("find all records") {
      running(FakeApplication()) {
        val allResults = Company.findAll()
        assert(allResults.size == 4)
      }
    }
    it("count all records") {
      running(FakeApplication()) {
        val count = Company.countAll()
        assert(count == 4L)
      }
    }
    it("find by where clauses") {
      running(FakeApplication()) {
        val results = Company.findAllBy(sqls.eq(Company.column.name, "Google"))
        assert(results.size == 1)
      }
    }
    it("count by where clauses") {
      running(FakeApplication()) {
        val count = Company.countBy(sqls.ne(Company.column.name, "Google"))
        assert(count == 3L)
      }
    }
    it("create new record") {
      running(FakeApplication()) {
        val newCompany = Company.create("Microsoft")
        assert(newCompany.id == 5)
      }
    }
    it("save a record") {
      running(FakeApplication()) {
        val entity = Company.findAll().head
        entity.copy(name = "Apple").save()
        val updated = Company.find(entity.id).get
        assert(updated.name == "Apple")
      }
    }
    it("destroy a record") {
      running(FakeApplication()) {
        val entity = Company.findAll().head
        entity.destroy()
        assert(Company.find(entity.id) == None)
        assert(Company.countAll == 3L)
      }
    }
  }

}

