package services.supporting

import javax.inject.Singleton
import java.util.Date
import java.util.Calendar

@Singleton
class DatesService {

	def date(year: Int, month: Int, day: Int): Date = {
		val calendar: Calendar = Calendar.getInstance()
		calendar.set(Calendar.YEAR, year)
		calendar.set(Calendar.MONTH, month - 1)
		calendar.set(Calendar.DAY_OF_MONTH, day)
		calendar.set(Calendar.HOUR_OF_DAY, 0)
		calendar.set(Calendar.MINUTE, 0)
		calendar.set(Calendar.SECOND, 0)
		calendar.set(Calendar.MILLISECOND, 0)
		
		calendar.getTime()
	}
	
	def firstDayOfYear(year: Int): Date = {
		val calendarStart: Calendar = Calendar.getInstance()
		calendarStart.set(Calendar.YEAR, year)
		calendarStart.set(Calendar.MONTH, 0)
		calendarStart.set(Calendar.DAY_OF_MONTH, 1)
		calendarStart.set(Calendar.HOUR_OF_DAY, 0)
		calendarStart.set(Calendar.MINUTE, 0)
		calendarStart.set(Calendar.SECOND, 0)
		calendarStart.set(Calendar.MILLISECOND, 0)

		calendarStart.getTime()
	}

	def firstDayOfTheNextMonth(date: Date): Date = {
		val calendar: Calendar = Calendar.getInstance()
		calendar.setTime(date)
		calendar.set(Calendar.DAY_OF_MONTH, 1)
		calendar.set(Calendar.HOUR_OF_DAY, 0)
		calendar.set(Calendar.MINUTE, 0)
		calendar.set(Calendar.SECOND, 0)
		calendar.set(Calendar.MILLISECOND, 0)
		calendar.add(Calendar.MONTH, 1)

		calendar.getTime()
	}

	def firstDayOfTheMonth(year: Int, month: Int): Date = {
		val calendar: Calendar = Calendar.getInstance()
		calendar.set(Calendar.YEAR, year)
		calendar.set(Calendar.MONTH, month - 1)
		calendar.set(Calendar.DAY_OF_MONTH, 1)
		calendar.set(Calendar.HOUR_OF_DAY, 0)
		calendar.set(Calendar.MINUTE, 0)
		calendar.set(Calendar.SECOND, 0)
		calendar.set(Calendar.MILLISECOND, 0)

		calendar.getTime()
	}

	def firstDayOfTheNextMonth(year: Int, month: Int): Date = {
		val calendar: Calendar = Calendar.getInstance()
		calendar.setTime(firstDayOfTheMonth(year, month))
		calendar.add(Calendar.MONTH, 1)

		calendar.getTime()
	}

}