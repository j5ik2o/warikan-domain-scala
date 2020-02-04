package warikan.domain.model.amount

import warikan.domain.model.payment.PaymentType

/**
 * 飲み会支払タイプ比率集合
 */
case class PartyPaymentTypeRatios(small: PaymentTypeRatio, medium: PaymentTypeRatio, large: PaymentTypeRatio) {
  require(large >= medium)
  require(medium >= small)

  private lazy val values = Map(PaymentType.LARGE -> large, PaymentType.MEDIUM -> medium, PaymentType.SMALL -> small)

  def paymentTypeRatio(t: PaymentType): PaymentTypeRatio = values(t)
}

object PartyPaymentTypeRatios {
  val default: PartyPaymentTypeRatios = PartyPaymentTypeRatios(PaymentTypeRatio(1.0), PaymentTypeRatio(1.0), PaymentTypeRatio(1.0))
}

