package warikan.domain.model.amount

/**
  * 支払割合。
  *
  * @param value
  */
final case class PaymentRatio(value: Double) extends Ordered[PaymentRatio] {
  require(value > 0)

  override def compare(that: PaymentRatio): Int =
    value compare that.value
}