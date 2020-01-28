package warikan.domain.model

object Members {
  val empty = Members(Seq.empty)
}

case class Members(values: Seq[Member]) {

  def weightedSum(partyPaymentTypeRatioMap: PartyPaymentTypeRatioMap): Double = {
    values.map{ v =>
      partyPaymentTypeRatioMap.getRatioFromType(v.paymentType).value
    }.sum
  }

}
