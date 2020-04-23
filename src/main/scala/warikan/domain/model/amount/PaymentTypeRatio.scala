package warikan.domain.model.amount

/**
  * 支払区分比重。
  *
  * @param value
  */
final case class PaymentTypeRatio(value: Double) extends Ordered[PaymentTypeRatio] {
  require(value > 0)

  override def compare(that: PaymentTypeRatio): Int =
    value compare that.value
}
