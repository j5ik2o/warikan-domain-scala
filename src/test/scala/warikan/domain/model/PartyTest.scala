package warikan.domain.model

import java.time.LocalDate

import org.scalatest.freespec.AnyFreeSpec
import warikan.domain.model.amount.{BillingAmount, PartyPaymentTypeRatios, PaymentTypeRatio}
import warikan.domain.model.member.{Member, MemberId, MemberName}
import warikan.domain.model.money.Money
import warikan.domain.model.payment.PaymentType

class PartyTest extends AnyFreeSpec {

  "Party" - {
    "warikan" in {
      val party = Party(PartyName("ABC"), LocalDate.now())
        .addMembers(
          Members(
            Member(MemberId(1L), MemberName("KATO"), PaymentType.LARGE),
            Member(MemberId(2L), MemberName("KATSUNO"), PaymentType.LARGE),
            Member(MemberId(3L), MemberName("FUJII"), PaymentType.MEDIUM),
            Member(MemberId(4L), MemberName("HAYASHI"), PaymentType.SMALL),
            Member(MemberId(5L), MemberName("SHAKA"), PaymentType.SMALL)
          )
        )
        .withPaymentTypeRatios(
          medium = PaymentTypeRatio(1.0),
          large = PaymentTypeRatio(1.2),
          small = PaymentTypeRatio(0.8)
        )

      val memberPaymentAmounts = party.warikan(BillingAmount(Money(30000, Money.JPY)))

      assert(memberPaymentAmounts.paymentAmountBy(MemberId(1L)).get.value == Money(7200, Money.JPY))
      assert(memberPaymentAmounts.paymentAmountBy(MemberId(2L)).get.value == Money(7200, Money.JPY))
      assert(memberPaymentAmounts.paymentAmountBy(MemberId(3L)).get.value == Money(6000, Money.JPY))
      assert(memberPaymentAmounts.paymentAmountBy(MemberId(4L)).get.value == Money(4800, Money.JPY))
      assert(memberPaymentAmounts.paymentAmountBy(MemberId(5L)).get.value == Money(4800, Money.JPY))
    }
  }

}
