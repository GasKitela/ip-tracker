package controllers

import akka.actor.ActorSystem
import javax.inject.{Inject, Singleton}
import jsonsupport.JSONWriteReadSupport
import play.api.libs.json.Json
import play.api.mvc._
import services.IpInformationService

import scala.concurrent.ExecutionContext

@Singleton
class IpInformationController @Inject()(cc: ControllerComponents,
                                        ipInformationService: IpInformationService,
                                        system: ActorSystem) extends AbstractController(cc) with JSONWriteReadSupport {

  implicit val myExecutionContext: ExecutionContext = system.dispatchers.lookup("incoming-requests-dispatcher")

  def index = Action {
    Ok(views.html.index("App is ready to go!"))
  }

  def getIpInfoViewData(ip: String) = Action {
    val response = ipInformationService.getAllByIp(ip)//, req.body)
    response match {
      case Right(r) =>
        Ok(views.html.iptracker(Nil, Some(r)))
      case Left(r) => BadRequest(Json.toJson(r))
    }
  }

  def getIpInfoView = Action {
    Ok(views.html.iptracker(Nil, None))
  }

  def getIpInformation(ip: String) = Action { implicit req =>
    val response = ipInformationService.getAllByIp(ip)//, req.body)
    response match {
      case Right(r) =>
        Ok(Json.toJson(r))
      case Left(r) => BadRequest(Json.toJson(r))
    }
  }

  def getMaxSearchDistance: Action[AnyContent] = Action.async { _ =>
    ipInformationService.getMaxRequestDistance.map(r => Ok(Json.toJson(r)))
  }

  def getMinSearchDistance: Action[AnyContent] = Action.async { _ =>
    ipInformationService.getMinRequestDistance.map(r => Ok(Json.toJson(r)))
  }

  def getAvgSearchDistance: Action[AnyContent] = Action.async { _ =>
    ipInformationService.getAverageRequestDistance.map(r => Ok(Json.toJson(r)))
  }

}
