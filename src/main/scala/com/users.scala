package com

import org.joda.time.DateTime
import reactivemongo.bson._

case class User(
                 id: Option[BSONObjectID],
                 firstName: String,
                 lastName: String,
                 phone: String,
                 email: String,
                 points: Option[Int],
                 lastVisit: Option[DateTime],
                 purchaseHistory: Option[List[String]]
                 )

object User {

  implicit object UserBSONReader extends BSONDocumentReader[User] {
    def read(doc: BSONDocument): User =
      User(
        doc.getAs[BSONObjectID]("_id"),
        doc.getAs[String]("firstName").get,
        doc.getAs[String]("lastName").get,
        doc.getAs[String]("phone").get,
        doc.getAs[String]("email").get,
        doc.getAs[Double]("points").map(value => value.toInt),
        doc.getAs[BSONDateTime]("lastVisit").map(dt => new DateTime(dt.value)),
        doc.getAs[BSONDocument]("purchaseHistory").map(bson => bson.asInstanceOf)
      )
  }

  implicit object UserBSONWriter extends BSONDocumentWriter[User] {
    def write(user: User): BSONDocument =
      BSONDocument(
        "_id" -> user.id.getOrElse(BSONObjectID.generate),
        "firstName" -> user.firstName,
        "lastName" -> user.lastName,
        "phone" -> user.phone,
        "email" -> user.email,
        "points" -> user.points,
        "lastVisit" -> user.lastVisit.map(date => BSONDateTime(date.getMillis)),
        "purchaseHistory" -> BSONArray(user.purchaseHistory)
      )
  }
}
