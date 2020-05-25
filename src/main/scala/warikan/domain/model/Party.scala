package warikan.domain.model

import java.time.LocalDate

import warikan.domain.model.amount.{BillingAmount, PartyPaymentTypeRatios, PaymentRatio, WeightedSum}
import warikan.domain.model.member.{Member, MemberIds, Members}

/**
  * 飲み会名。
  *
  * @param value
  */
final case class PartyName(value: String) {
  require(value.nonEmpty)
}

/**
  * 飲み会。
  *
  * @param name
  * @param date
  * @param membersOpt
  * @param partyPaymentTypeRatios
  */
final case class Party(
    name: PartyName,
    date: LocalDate,
    membersOpt: Option[Members],
    partyPaymentTypeRatios: PartyPaymentTypeRatios
) {

  def withMembers(other: Members): Party =
    copy(membersOpt = membersOpt.map(_.combine(other)).orElse(Some(other)))

  def withPaymentTypeRatios(large: PaymentRatio, small: PaymentRatio): Party =
    withPaymentTypeRatios(PartyPaymentTypeRatios(small, large))

  def withPaymentTypeRatios(value: PartyPaymentTypeRatios): Party =
    copy(partyPaymentTypeRatios = value)

  def warikan(billingAmount: BillingAmount): Warikan = {
    membersOpt match {
      case None =>
        throw new IllegalStateException()
      case Some(members) =>
        // 普通の支払金額 = 請求金額 / 加重和
        val paymentBaseAmount = billingAmount.divide(weightedSum(partyPaymentTypeRatios))
        // 参加者ごとの支払金額を計算
        val result = members.paymentTypes.map{ case (member, paymentType) =>
          // 支払区分から支払割合を取得する
          val paymentRatio = partyPaymentTypeRatios.paymentTypeRatio(paymentType)
          // 普通の支払金額に支払割合を掛ける
          member -> paymentBaseAmount.times(paymentRatio)
        }
        Warikan(result)
    }
  }

  // 加重和を求める
  private def weightedSum(partyPaymentTypeRatios: PartyPaymentTypeRatios): WeightedSum = {
    membersOpt match {
      case None =>
        throw new IllegalStateException()
      case Some(members) =>
        // 全参加者が持つ支払割合のコレクションを作る
        val paymentRatios = members.map { member =>
          partyPaymentTypeRatios.paymentTypeRatio(member.paymentType)
        }
        // 支払割合のコレクションから加重和を求る
        WeightedSum.from(paymentRatios.head, paymentRatios.tail: _*)
    }
  }

}

object Party {

  def apply(name: PartyName, date: LocalDate): Party =
    new Party(name, date, None, PartyPaymentTypeRatios.default)

}
