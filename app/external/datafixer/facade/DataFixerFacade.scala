package external.datafixer.facade

import external.datafixer.client.DataFixerClient
import javax.inject.Inject
import model.CurrencyRates

class DataFixerFacade @Inject()(dataFixerClient: DataFixerClient) {

  def getCurrencyRates(currency: String) = {
    dataFixerClient.getCurrencyRates(currency) match {
      case Right(response) if response.success => Right(CurrencyRates(currency, response.rates.getOrElse(Map.empty)))
      case Right(_) => Right(CurrencyRates(currency, Map.empty))
      case Left(error) => Left(error)
    }
  }
}
