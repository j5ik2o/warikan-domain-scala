package warikan.domain.model

import warikan.domain.model.amount.{PaymentAmount, PaymentTotalAmount}
import warikan.domain.model.member.{Member, MemberId}
import warikan.domain.model.money.Money

/**
 * 参加者毎の支払金額のファーストクラスコレクション
 */
case class MemberPaymentAmounts(values: Map[Member, PaymentAmount]) {

  def paymentAmountBy(member: Member): Option[PaymentAmount] = values.get(member)

  def paymentAmountBy(memberId: MemberId): Option[PaymentAmount] = values.find(_._1.id == memberId).map(_._2)

  def totalAmount: PaymentTotalAmount =
    PaymentTotalAmount(values.values.foldLeft(Money.zero(Money.DefaultCurrency)) { (result, elem) =>
      result + elem.value
    })

}

