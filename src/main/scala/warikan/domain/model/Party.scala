package warikan.domain.model

import java.time.LocalDate

case class PartyName(value: String)

case class Party(name: PartyName, date: LocalDate, members: Members, partyPaymentTypeRatioMap: PartyPaymentTypeRatioMap) {
  // def addMembers(members: Members): Party = ???
  // def removeMembers(memberIds: MemberIds): Party = ???

  // def withPaymentTypeRatioMap(value: PartyPaymentTypeRatioMap): Party = ???
  def warikan(billingAmount: BillingAmount): Map[Member, PaymentAmount] = {
    val paymentBaseAmount = new PaymentBaseAmount(billingAmount.value.divide(members.weightedSum(partyPaymentTypeRatioMap), BigDecimal.RoundingMode.HALF_EVEN))
    members.values.map{ v =>
      v -> PaymentAmount(paymentBaseAmount.value.times(partyPaymentTypeRatioMap.getRatioFromType(v.paymentType).value))
    }.toMap
  }
}

object Party {

 def of(name: PartyName, date: LocalDate): Party =
   new Party(name, date, Members.empty, PartyPaymentTypeRatioMap.default)


}
