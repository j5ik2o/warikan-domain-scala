package warikan.domain.model.amount

object WeightedSum {

  def from(head: PaymentRatio, tail: PaymentRatio*): WeightedSum = {
    tail.foldLeft(WeightedSum(head.value)) { (weightedSum, paymentRatio) =>
      weightedSum.add(paymentRatio)
    }
  }

}

final case class WeightedSum(value: Double) {
  require(value >= 0)

  def add(ratio: PaymentRatio): WeightedSum =
    copy(value = value + ratio.value)

}