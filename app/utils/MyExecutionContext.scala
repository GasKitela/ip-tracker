package utils

import akka.actor.ActorSystem
import javax.inject.Inject
import play.api.libs.concurrent.CustomExecutionContext

class MyExecutionContext @Inject()(system: ActorSystem) extends CustomExecutionContext(system, "incoming-requests-dispatcher")
