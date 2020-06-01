package repository

import javax.inject.Inject
import model.IpServiceMetrics
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.mongodb.scala.MongoClient.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.{MongoClient, MongoClientSettings, ServerAddress}
import play.api.Configuration

import scala.collection.JavaConverters._

class MongoClientProvider @Inject()(config: Configuration) {

  val settings = MongoClientSettings.builder().applyToClusterSettings {
    b => b.hosts(config.get[Seq[String]]("mongodb.hosts").map(new ServerAddress(_)).asJava)
  }.build()

  val mongoClient = MongoClient(settings)

  val codecRegistry = fromRegistries(fromProviders(classOf[IpServiceMetrics]), DEFAULT_CODEC_REGISTRY )

  val database = mongoClient.getDatabase(config.get[String]("mongodb.database-name")).withCodecRegistry(codecRegistry)

}