package warikan.domain.model.amount

import warikan.domain.model.payment.PaymentType

/**
  * 支払金額比重リスト。
  *
  * @param small
  * @param large
  */
final case class PartyPaymentTypeRatios(small: PaymentRatio,  large: PaymentRatio) {
  require(large >= small)

  private val values = Map(PaymentType.Large -> large, PaymentType.Medium -> PaymentRatio(1), PaymentType.Small -> small)

  def paymentTypeRatio(t: PaymentType.Value): PaymentRatio = values(t)
}

object PartyPaymentTypeRatios {
  val default: PartyPaymentTypeRatios =
    PartyPaymentTypeRatios(small = PaymentRatio(0.8), large = PaymentRatio(1.2))
}
