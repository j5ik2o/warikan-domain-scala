package warikan.domain.model.member

import warikan.domain.model.payment.PaymentType

/**
  * 参加者。
  *
  * @param id
  * @param name
  * @param paymentType
  */
final case class Member(id: MemberId, name: MemberName, paymentType: PaymentType)
