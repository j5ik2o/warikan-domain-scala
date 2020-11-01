package warikan.domain.model

case class PaymentMap(value: Map[PaymentSection, PaymentRatio]) {

  //
  def get(paymentSection: PaymentSection): PaymentRatio = ???

}
