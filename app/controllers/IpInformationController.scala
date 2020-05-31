package controllers

import javax.inject.{Inject, Singleton}
import jsonsupport.JSONWriteReadSupport
import play.api.libs.json.Json
import play.api.mvc._
import services.IpInformationService
import utils.MyExecutionContext

@Singleton
class IpInformationController @Inject()(cc: ControllerComponents,
                                        ipInformationService: IpInformationService)
                                       (implicit ec: MyExecutionContext) extends AbstractController(cc) with JSONWriteReadSupport {


  def getIpInformation(ip: String) = Action { implicit req =>
    val response = ipInformationService.getAllByIp(ip)//, req.body)
    response match {
      case Right(r) =>
        Ok(Json.toJson(r))
      case Left(r) => BadRequest(Json.toJson(r))
    }
  }

  def getMaxDist: Action[AnyContent] = Action.async { _ =>
    ipInformationService.getMaxRequestDistance.map(r => Ok(Json.toJson(r)))
  }

  def getMinDist: Action[AnyContent] = Action.async { _ =>
    ipInformationService.getMinRequestDistance.map(r => Ok(Json.toJson(r)))
  }

  def getAvgDist: Action[AnyContent] = Action.async { _ =>
    ipInformationService.getAverageRequestDistance.map(r => Ok(Json.toJson(r)))
  }

}
