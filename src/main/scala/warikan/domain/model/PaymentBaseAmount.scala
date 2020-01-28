package warikan.domain.model

import warikan.domain.model.money.Money

case class PaymentBaseAmount(value: Money) {
  def times(ratio: PaymentTypeRatio): Money =
    value.times(ratio.value)

}

object PaymentBaseAmount {
  def of(value: Money): PaymentBaseAmount =
    new PaymentBaseAmount(value)
}


