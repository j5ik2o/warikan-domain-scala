package warikan.domain.model.amount

import warikan.domain.model.money.Money

case class BillingAmount(value: Money) {
  require(value.isPositive)

  def divide(weightedSum: WeightedSum): PaymentBaseAmount = {
    PaymentBaseAmount(value.divide(weightedSum.value, BigDecimal.RoundingMode.HALF_EVEN))
  }

}
