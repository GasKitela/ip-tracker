package external.distance24.facade

import external.distance24.client.Distance24Client
import javax.inject.Inject
import model.Distance

class Distance24Facade @Inject()(distance24Client: Distance24Client) {

  def getDistanceToBsAs(city: String) = {
    distance24Client.getDistanceToBsAs(city: String)map(r => Distance(r.distance))
  }

}
