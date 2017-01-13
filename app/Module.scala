import com.google.inject.AbstractModule
import java.time.Clock

import services.{ApplicationService}
import services.application.SetupService
import services.schedule.ScheduleService
import play.api.libs.concurrent.AkkaGuiceSupport
import services.schedule.actors.CurrenciesActor

/**
 * This class is a Guice module that tells Guice how to bind several
 * different types. This Guice module is created when the Play
 * application starts.

 * Play will automatically use any class called `Module` that is in
 * the root package. You can create modules in other locations by
 * adding `play.modules.enabled` settings to the `application.conf`
 * configuration file.
 */
class Module extends AbstractModule with AkkaGuiceSupport {

	override def configure() = {
		bind(classOf[ApplicationService]).asEagerSingleton()
		bind(classOf[SetupService]).asEagerSingleton()
		bind(classOf[ScheduleService]).asEagerSingleton()
		
		// actors:
		bindActor[CurrenciesActor]("currenciesActor")
	}

}
