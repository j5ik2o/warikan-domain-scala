package warikan.domain.model.amount

import warikan.domain.model.money.Money

/**
  * 支払金額。
  *
  * @param value
  */
final case class PaymentAmount(value: Money) {
  require(value.isPositive)

  def times(factor: PaymentRatio): PaymentAmount =
    copy(value.times(factor.value))

}
