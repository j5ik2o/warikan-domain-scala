package warikan.domain.model.amount

import warikan.domain.model.money.Money

/**
  * 合計支払金額。
  *
  * @param value
  */
final case class PaymentTotalAmount(value: Money) {
  require(value.isPositive)
}
