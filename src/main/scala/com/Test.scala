package com

import play.api.libs.iteratee.Iteratee
import reactivemongo.api._
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.bson.BSONDocument

import scala.concurrent.ExecutionContext.Implicits.global

object Test {
  val driver = new MongoDriver

  val connection = driver.connection(List("localhost:27017"))

  // Gets a reference to the database "plugin"
  val db = connection("database")

  // Gets a reference to the collection "acoll"
  // By default, you get a BSONCollection.
  val collection = db[BSONCollection]("users")

  val query = BSONDocument(
    "$query" -> BSONDocument())

  val cursor = collection.find(query).cursor[User]

  def listDocs() = {
    // Select only the documents which field 'firstName' equals 'Jack'
    val query = BSONDocument("firstName" -> "Taylor")
    // select only the field 'lastName'
    val filter = BSONDocument(
      "lastName" -> 1)

    // Get a cursor of BSONDocuments
    val cursor = collection.find(BSONDocument()).cursor[BSONDocument]
    // Let's enumerate this cursor and print a readable representation of each document in the response
    cursor.enumerate().apply(Iteratee.foreach { doc =>
      println("found document: " + BSONDocument.pretty(doc))
    })

    // Or, the same with getting a list
    val cursor2 = collection.find(query, filter).cursor[BSONDocument]
    val futureList = cursor.collect[List]()
    futureList.map { list =>
      list.foreach { doc =>
        println("found document: " + BSONDocument.pretty(doc))
      }
    }
  }

  def main(args: Array[String]) {
    while(true) {
//      cursor.collect[List]().map { list => list.foreach { doc => print(doc.firstName)}}
      listDocs()
    }
  }
}
