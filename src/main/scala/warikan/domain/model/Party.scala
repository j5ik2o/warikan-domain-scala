package warikan.domain.model

import java.time.LocalDate

import warikan.domain.model.amount.{BillingAmount, PartyPaymentTypeRatios, PaymentTypeRatio}

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
    members.memberPaymentAmounts(billingAmount, partyPaymentTypeRatios)

}

object Party {

  def apply(name: PartyName, date: LocalDate): Party =
    new Party(name, date, Members.empty, PartyPaymentTypeRatios.default)

}
