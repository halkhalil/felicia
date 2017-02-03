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
class ScheduleService @Inject() (
	system: ActorSystem,
	@Named("currenciesActor") currenciesActor: ActorRef,
	@Named("invoicesBackupActor") invoicesBackupActor: ActorRef
) (implicit ec: ExecutionContext) {
	
	// schedule currencies actor:
	system.scheduler.schedule(10.seconds, 12.hours, currenciesActor, Tick)
	
	// schedule invoices backup actor:
	system.scheduler.schedule(300.seconds, 24.hours, invoicesBackupActor, Tick)
}