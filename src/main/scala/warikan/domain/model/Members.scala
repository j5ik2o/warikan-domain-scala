package warikan.domain.model

import warikan.domain.model.amount.{BillingAmount, PartyPaymentTypeRatios, WeightedSum}
import warikan.domain.model.member.Member

object Members {
  val empty: Members = new Members(Seq.empty: _*)
}

/**
 * 参加者のファーストクラスコレクション
 */
case class Members(values: Member*) {
  def removeMembers(memberIds: MemberIds): Members = Members(values.filterNot(e => memberIds.values.contains(e.id)): _*)

  def combine(other: Members): Members = Members(values ++ other.values: _*)

  private def weightedSum(partyPaymentTypeRatios: PartyPaymentTypeRatios): WeightedSum = {
    values.foldLeft(WeightedSum.zero){ (weightedSum, member) =>
      weightedSum.add(partyPaymentTypeRatios.paymentTypeRatio(member.paymentType))
    }
  }

  def memberPaymentAmounts(billingAmount: BillingAmount, partyPaymentTypeRatios: PartyPaymentTypeRatios): MemberPaymentAmounts = {
    val paymentBaseAmount = billingAmount.divide(weightedSum(partyPaymentTypeRatios))
    val result = values.map { member =>
      val ratio = partyPaymentTypeRatios.paymentTypeRatio(member.paymentType)
      member -> paymentBaseAmount.times(ratio)
    }.toMap
    MemberPaymentAmounts(result)
  }

}
