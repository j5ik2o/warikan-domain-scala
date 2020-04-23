package warikan.domain.model

import warikan.domain.model.amount.{ PaymentAmount, PaymentTotalAmount }
import warikan.domain.model.member.{ Member, MemberId }
import warikan.domain.model.money.Money

/**
  * 割り勘。
  *
  * 参加者ごとの支払金額。
  *
  * @param values
  */
final case class Warikan(values: Map[Member, PaymentAmount]) {
  require(values.nonEmpty)

  def paymentAmountBy(member: Member): Option[PaymentAmount] = values.get(member)

  def paymentAmountBy(memberId: MemberId): Option[PaymentAmount] = values.find(_._1.id == memberId).map(_._2)

  def totalAmount: PaymentTotalAmount =
    PaymentTotalAmount(values.values.foldLeft(Money.zero(Money.DefaultCurrency)) { (result, elem) =>
      result + elem.value
    })

}
