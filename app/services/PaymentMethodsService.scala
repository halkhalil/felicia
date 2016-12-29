package services

import models._
import javax.inject.Singleton
import scala.collection.JavaConverters._
import controllers.input.PaymentMethodInput

@Singleton
class PaymentMethodsService {
	def getAll(): List[PaymentMethod] = {
		PaymentMethod.finder.all().asScala.toList
	}
	
	def get(id: Int): Option[PaymentMethod] = {
		val paymentMethod: PaymentMethod = PaymentMethod.finder.where().eq("id", id).findUnique()
		
		if (paymentMethod != null) Some(paymentMethod) else None
	}
	
	def validationErrorOnUpdate(paymentMethodInput: PaymentMethodInput): Option[String] = {
		if (paymentMethodInput.name.trim().length() == 0) return Some("Name cannot be empty")
		if (paymentMethodInput.symbol.trim().length() == 0) return Some("Symbol cannot be empty")
		
		None
	}
	
	def update(paymentMethod: PaymentMethod, paymentMethodInput: PaymentMethodInput): PaymentMethod = {
		paymentMethod.name = paymentMethodInput.name
		paymentMethod.symbol = paymentMethodInput.symbol
		paymentMethod.save()
		
		paymentMethod
	}
	
	def validationErrorOnCreate(paymentMethodInput: PaymentMethodInput): Option[String] = {
		if (paymentMethodInput.name.trim().length() == 0) return Some("Name cannot be empty")
		if (paymentMethodInput.symbol.trim().length() == 0) return Some("Symbol cannot be empty")
		
		None
	}
	
	def create(paymentMethodInput: PaymentMethodInput): PaymentMethod = {
		val paymentMethod: PaymentMethod = new PaymentMethod()
		paymentMethod.symbol = paymentMethodInput.symbol
		paymentMethod.name = paymentMethodInput.name
		
		paymentMethod.save()
		
		paymentMethod
	}
	
	def delete(paymentMethod: PaymentMethod): PaymentMethod = {
		paymentMethod.delete()
		
		paymentMethod
	}
}