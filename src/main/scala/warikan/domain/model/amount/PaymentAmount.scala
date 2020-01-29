package warikan.domain.model.amount

import warikan.domain.model.money.Money

case class PaymentAmount(value: Money) {
  require(value.isPositive)

  def times(factor: PaymentTypeRatio): Money =
    value.times(factor.value)

}
