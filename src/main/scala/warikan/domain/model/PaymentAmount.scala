package warikan.domain.model

import warikan.domain.model.money.Money


case class PaymentAmount(value: Money) {
  def times(factor: PaymentTypeRatio): Money =
    value.times(factor.value)

}
