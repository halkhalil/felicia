package services.schedule

import javax.inject.Singleton
import play.Logger
import akka.actor._
import scala.concurrent.duration._
import play.libs.Akka
import javax.inject.Inject
import scala.concurrent.ExecutionContext
import services.schedule.actors.CurrenciesActor
import javax.inject.Named

case object Tick

@Singleton
class ScheduleService @Inject() (system: ActorSystem, @Named("currenciesActor") currenciesActor: ActorRef) (implicit ec: ExecutionContext) {
	
	// schedule currencies actor:
	system.scheduler.schedule(0.seconds, 12.hours, currenciesActor, Tick)
	
}