package warikan.domain.model

import warikan.domain.model.amount.{ PaymentAmount, PaymentTotalAmount }
import warikan.domain.model.member.{ Member, MemberId }
import warikan.domain.model.money.Money

/**
  * 割り勘。
  *
  * 参加者ごとの支払金額。
  *
  * @param result
  */
final case class Warikan(result: Map[Member, PaymentAmount]) {
  require(result.nonEmpty)

  def paymentAmountBy(member: Member): Option[PaymentAmount] = result.get(member)

  def paymentAmountBy(memberId: MemberId): Option[PaymentAmount] = result.find(_._1.id == memberId).map(_._2)

  def totalAmount: PaymentTotalAmount = {
    val ta = result.values.foldLeft(Money.zero(Money.DefaultCurrency)) { (result, elem) =>
      result + elem.value
    }
    PaymentTotalAmount(ta)
  }

}