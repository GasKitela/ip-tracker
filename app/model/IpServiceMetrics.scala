package model

import org.bson.types.ObjectId

case class IpServiceMetrics(_id: ObjectId,
                            ip: String,
                            country: String,
                            distance: Int)

object IpServiceMetrics {
  def apply(ip: String, country: String, distance: Int): IpServiceMetrics = IpServiceMetrics(new ObjectId(), ip, country, distance)
}