package warikan.domain.model.member

import warikan.domain.model.payment.PaymentType

/**
 * 参加者
 */
case class Member(id: MemberId, name: MemberName, paymentType: PaymentType)

