package warikan.domain.model

import warikan.domain.model.money.Money

class Nomikai(name: Name, heldData: HeldDate, members: Members, totalPrice: TotalPrice) {

  lazy val pricePerMember: Money = ???

  def warikan(paymentMap: PaymentMap): PaymentPrice = {
    new PaymentPrice(members.value.map { member =>
      val maybeRatio = paymentMap.get(member.paymentSection)
      val money = pricePerMember * maybeRatio.decimal
      member -> money
    }.toMap)
  }
}
