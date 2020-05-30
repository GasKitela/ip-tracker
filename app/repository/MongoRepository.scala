package repository

import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.{Completed, MongoCollection}
import org.mongodb.scala.model.Sorts._

import scala.concurrent.Future
import scala.reflect.ClassTag

abstract class MongoRepository[A: ClassTag] {

  def collectionName: String
  def mongoClientProvider: MongoClientProvider

  val collection: MongoCollection[A] = mongoClientProvider.database.getCollection[A](collectionName)

  def find(query: Bson): Future[Seq[A]] = {
    collection.find(query).toFuture
  }

  def findAll: Future[Seq[A]] = {
    collection.find().toFuture
  }

  def findMinBy(field: String): Future[A] = {
    collection.find().sort(ascending(field)).first().toFuture
  }

  def findMaxBy(field: String): Future[A] = {
    collection.find().sort(descending(field)).first().toFuture
  }

  def findOne(query: Bson): Future[A] = {
    collection.find(query).first().toFuture
  }

  def insert(elem: A): Future[Completed] = {
    collection.insertOne(elem).toFuture
  }

}
