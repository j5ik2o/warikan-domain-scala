package warikan.domain.model.amount

import warikan.domain.model.money.Money

/**
 * 請求金額
 */
case class BillingAmount(value: Money) {
  require(!value.isNegative)

  def subtract(totalAmount: PaymentTotalAmount): BillingAmount =
    BillingAmount(value - totalAmount.value)

  def divide(weightedSum: WeightedSum): PaymentBaseAmount =
    PaymentBaseAmount(value.dividedBy(weightedSum.value, BigDecimal.RoundingMode.HALF_EVEN))

}
