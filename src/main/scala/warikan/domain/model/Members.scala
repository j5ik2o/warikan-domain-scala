package warikan.domain.model

object Members {
  val empty: Members = new Members(Seq.empty: _*)
}

case class Members(values: Member*) {
  def removeMembers(memberIds: MemberIds): Members = Members(values.filterNot(e => memberIds.values.contains(e.id)): _*)

  def combine(other: Members): Members = Members(values ++ other.values: _*)

  def weightedSum(partyPaymentTypeRatioMap: PartyPaymentTypeRatioMap): Double = {
    values.map { v =>
      partyPaymentTypeRatioMap.getRatioFromType(v.paymentType).value
    }.sum
  }

  def memberPaymentAmountMap(paymentBaseAmount: PaymentBaseAmount, partyPaymentTypeRatioMap: PartyPaymentTypeRatioMap): MemberPaymentAmountMap = {
    MemberPaymentAmountMap(values.map { v =>
      v -> PaymentAmount(paymentBaseAmount.times(partyPaymentTypeRatioMap.getRatioFromType(v.paymentType)))
    }.toMap)
  }

}
