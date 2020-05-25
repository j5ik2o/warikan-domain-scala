package warikan.domain.model

import java.time.LocalDate

import org.scalatest.freespec.AnyFreeSpec
import warikan.domain.model.amount.{ BillingAmount, PaymentRatio }
import warikan.domain.model.member.{ Member, MemberId, MemberName, Members }
import warikan.domain.model.money.Money
import warikan.domain.model.payment.PaymentType

class PartyTest extends AnyFreeSpec {

  "Party" - {
    "warikan" in {
      val party = Party(PartyName("ABC"), LocalDate.now())
        .withMembers(
          Members(
            Member(MemberId(1L), MemberName("KATO"), PaymentType.Large),
            Member(MemberId(2L), MemberName("KATSUNO"), PaymentType.Large),
            Member(MemberId(3L), MemberName("FUJII"), PaymentType.Medium),
            Member(MemberId(4L), MemberName("HAYASHI"), PaymentType.Small),
            Member(MemberId(5L), MemberName("SHAKA"), PaymentType.Small)
          )
        )
        .withPaymentTypeRatios(
          small = PaymentRatio(0.3),
          large = PaymentRatio(1.2)
        )
      val billingAmount = BillingAmount(Money(30000, Money.JPY))
      val warikan       = party.warikan(billingAmount)

      assert(warikan.paymentAmountBy(MemberId(1L)).get.value == Money(9000, Money.JPY))
      assert(warikan.paymentAmountBy(MemberId(2L)).get.value == Money(9000, Money.JPY))
      assert(warikan.paymentAmountBy(MemberId(3L)).get.value == Money(7500, Money.JPY))
      assert(warikan.paymentAmountBy(MemberId(4L)).get.value == Money(2250, Money.JPY))
      assert(warikan.paymentAmountBy(MemberId(5L)).get.value == Money(2250, Money.JPY))

      val result = billingAmount subtract warikan.totalAmount
      println("余り = " + result)

    }
  }

}
