package warikan.domain.model.amount

import warikan.domain.model.money.Money

/**
 * 支払基準金額
 */
case class PaymentBaseAmount(value: Money) {
  def times(ratio: PaymentTypeRatio): PaymentAmount =
    PaymentAmount(value.times(ratio.value))

}

object PaymentBaseAmount {
  def apply(value: Money): PaymentBaseAmount =
    new PaymentBaseAmount(value)
}


