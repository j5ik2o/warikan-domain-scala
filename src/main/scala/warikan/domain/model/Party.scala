package warikan.domain.model

import java.time.LocalDate

case class PartyName(value: String)

case class Party(name: PartyName, date: LocalDate, members: Members, partyPaymentTypeRatioMap: PartyPaymentTypeRatioMap) {
  def addMembers(other: Members): Party =
    copy(members = members.combine(other))
  def removeMembers(memberIds: MemberIds): Party =
    copy(members = members.removeMembers(memberIds))

  def withPaymentTypeRatioMap(value: PartyPaymentTypeRatioMap): Party =
    copy(partyPaymentTypeRatioMap = value)

  def warikan(billingAmount: BillingAmount): MemberPaymentAmountMap = {
    val paymentBaseAmount = PaymentBaseAmount.of(billingAmount.divide(members.weightedSum(partyPaymentTypeRatioMap)))
    members.memberPaymentAmountMap(paymentBaseAmount, partyPaymentTypeRatioMap)
  }
}

object Party {

 def apply(name: PartyName, date: LocalDate): Party =
   new Party(name, date, Members.empty, PartyPaymentTypeRatioMap.default)

}
