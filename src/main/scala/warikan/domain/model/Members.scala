package warikan.domain.model

import java.net.http.WebSocket

import warikan.domain.model.amount.{BillingAmount, PartyPaymentTypeRatios, PaymentAmount, PaymentBaseAmount, WeightedSum}
import warikan.domain.model.member.Member

object Members {
  val empty: Members = new Members(Seq.empty: _*)
}

case class Members(values: Member*) {
  def removeMembers(memberIds: MemberIds): Members = Members(values.filterNot(e => memberIds.values.contains(e.id)): _*)

  def combine(other: Members): Members = Members(values ++ other.values: _*)

  private def weightedSum(partyPaymentTypeRatioMap: PartyPaymentTypeRatios): WeightedSum = {
    values.foldLeft(WeightedSum.zero){ (weightedSum, member) =>
      weightedSum.add(partyPaymentTypeRatioMap.paymentTypeRatio(member.paymentType))
    }
  }

  def memberPaymentAmounts(billingAmount: BillingAmount, partyPaymentTypeRatioMap: PartyPaymentTypeRatios): MemberPaymentAmounts = {
    val paymentBaseAmount = billingAmount.divide(weightedSum(partyPaymentTypeRatioMap))
    val result = values.map { member =>
      val ratio = partyPaymentTypeRatioMap.paymentTypeRatio(member.paymentType)
      member -> paymentBaseAmount.times(ratio)
    }.toMap
    MemberPaymentAmounts(result)
  }

}
