package warikan.domain.model

case class PartyPaymentTypeRatioMap(medium: PaymentTypeRatio, large: PaymentTypeRatio, small: PaymentTypeRatio) {
  private lazy val values = Map(PaymentType.LARGE -> large, PaymentType.MEDIUM -> medium, PaymentType.SMALL -> small)

  def getRatioFromType(t: PaymentType): PaymentTypeRatio = values(t)
}

object PartyPaymentTypeRatioMap {
  val default: PartyPaymentTypeRatioMap = PartyPaymentTypeRatioMap(PaymentTypeRatio(1.0), PaymentTypeRatio(1.0), PaymentTypeRatio(1.0))
}

