package services.schedule.actors

import services.schedule.Tick
import akka.actor.Actor
import services.currencies.CurrenciesService
import javax.inject.Inject

class CurrenciesActor @Inject() (currencieService: CurrenciesService) extends Actor {

	def receive = {
		case Tick => {
			currencieService.load
		}
	}
	
}