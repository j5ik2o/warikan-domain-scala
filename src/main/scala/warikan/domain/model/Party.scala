package warikan.domain.model

import java.time.LocalDate

import warikan.domain.model.amount.{BillingAmount, PartyPaymentTypeRatios, PaymentTypeRatio, WeightedSum}
import warikan.domain.model.member.{MemberIds, Members}

/**
 * 飲み会名
 */
case class PartyName(value: String)

/**
 * 飲み会
 */
case class Party(name: PartyName, date: LocalDate, members: Members, partyPaymentTypeRatios: PartyPaymentTypeRatios) {

  def addMembers(other: Members): Party =
    copy(members = members.combine(other))

  def removeMembers(memberIds: MemberIds): Party =
    copy(members = members.removeMembers(memberIds))

  def withPaymentTypeRatios(large: PaymentTypeRatio, medium: PaymentTypeRatio, small: PaymentTypeRatio): Party =
    withPaymentTypeRatios(PartyPaymentTypeRatios(medium, large, small))

  def withPaymentTypeRatios(value: PartyPaymentTypeRatios): Party =
    copy(partyPaymentTypeRatios = value)

  def warikan(billingAmount: BillingAmount): MemberPaymentAmounts =
    memberPaymentAmounts(billingAmount, partyPaymentTypeRatios)

  private def weightedSum(partyPaymentTypeRatios: PartyPaymentTypeRatios): WeightedSum = {
    val ratios = members.values.map(member => partyPaymentTypeRatios.paymentTypeRatio(member.paymentType))
    WeightedSum.zero.add(ratios.head, ratios.tail: _*)
  }

  private def memberPaymentAmounts(billingAmount: BillingAmount, partyPaymentTypeRatios: PartyPaymentTypeRatios): MemberPaymentAmounts = {
    val paymentBaseAmount = billingAmount.divide(weightedSum(partyPaymentTypeRatios))
    val result = members.values.map { member =>
      val ratio = partyPaymentTypeRatios.paymentTypeRatio(member.paymentType)
      member -> paymentBaseAmount.times(ratio)
    }.toMap
    MemberPaymentAmounts(result)
  }
}

object Party {

  def apply(name: PartyName, date: LocalDate): Party =
    new Party(name, date, Members.empty, PartyPaymentTypeRatios.default)

}
