package warikan.domain.model.amount

import warikan.domain.model.money.Money

case class PaymentTotalAmount(value: Money) {
  require(value.isPositive)
}
