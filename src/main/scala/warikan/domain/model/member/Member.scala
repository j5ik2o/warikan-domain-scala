package warikan.domain.model.member

import warikan.domain.model.payment.PaymentType

case class Member(id: MemberId, name: MemberName, paymentType: PaymentType)

