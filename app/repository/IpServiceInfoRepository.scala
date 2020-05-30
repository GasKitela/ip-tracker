package repository

import javax.inject.Inject
import model.IpServiceMetrics
import org.mongodb.scala.model.Filters
import play.api.Configuration

import scala.concurrent.Future

class IpServiceInfoRepository @Inject()(config: Configuration,
                                        val mongoClientProvider: MongoClientProvider) extends MongoRepository[IpServiceMetrics] {

  def collectionName: String = config.get[String]("mongodb.ip-service-info-collection-name")

  def findByCountry(country: String): Future[Seq[IpServiceMetrics]] = {
    find(Filters.eq("country", country))
  }

  def insertMetrics(ip: String, countryName: String, distance: Int) = {
    val serviceMetrics = IpServiceMetrics(ip, countryName, distance)

    insert(serviceMetrics)
  }

  def findMinDistance = {
    findMinBy("distance")
  }

  def findMaxDistance = {
    findMaxBy("distance")
  }


}
