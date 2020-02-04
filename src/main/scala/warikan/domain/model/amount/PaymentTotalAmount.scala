package warikan.domain.model.amount

import warikan.domain.model.money.Money

/**
 * 支払トータル金額
 */
case class PaymentTotalAmount(value: Money) {
  require(value.isPositive)
}
