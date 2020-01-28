package warikan.domain.model

import warikan.domain.model.money.Money

case class BillingAmount(value: Money) {
  def divide(weightedSum: Double): Money = {
    value.divide(weightedSum, BigDecimal.RoundingMode.HALF_EVEN)
  }
}
