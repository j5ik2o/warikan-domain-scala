package warikan.domain.model.amount

object WeightedSum {
  val zero: WeightedSum = WeightedSum(0)
}

/**
 * 合計比率
 */
case class WeightedSum(value: Double) {
  require(value >= 0)

  def add(ratio: PaymentTypeRatio): WeightedSum = copy(value = value + ratio.value)

  def combine(other: WeightedSum): WeightedSum =
    copy(value = value + other.value)
}

