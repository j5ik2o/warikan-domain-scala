package warikan.domain.model.amount

import warikan.domain.model.money.Money

/**
 * 支払金額
 */
case class PaymentAmount(value: Money) {
  require(value.isPositive)

  def times(factor: PaymentTypeRatio): Money =
    value.times(factor.value)

}
