package warikan.domain.model

case class MemberName(value: String)

case class Member(id: MemberId, name: MemberName, paymentType: PaymentType) {

}
