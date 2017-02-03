package services.schedule.actors

import services.schedule.Tick
import akka.actor.Actor
import javax.inject.Inject
import services.invoices.backup.PdfLocalBackupService

class InvoicesBackupActor @Inject() (pdfLocalBackupService: PdfLocalBackupService) extends Actor {

	def receive = {
		case Tick => {
			pdfLocalBackupService.run
		}
	}

}