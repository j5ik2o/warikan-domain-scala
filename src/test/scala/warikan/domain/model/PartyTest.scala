package warikan.domain.model

import java.time.LocalDate

import org.scalatest.freespec.AnyFreeSpec
import warikan.domain.model.money.Money

class PartyTest extends AnyFreeSpec {
  "Party" - {
    "warikan" in {
      val party = Party(PartyName("ABC"), LocalDate.now())
      val party2 = party.addMembers(Members(
        Member(MemberId(1L), MemberName("KATO"), PaymentType.LARGE),
        Member(MemberId(2L), MemberName("KATSUNO"), PaymentType.LARGE),
        Member(MemberId(3L), MemberName("FUJII"), PaymentType.MEDIUM),
        Member(MemberId(4L), MemberName("HAYASHI"), PaymentType.SMALL),
        Member(MemberId(5L), MemberName("SHAKA"), PaymentType.SMALL)
      )).withPaymentTypeRatioMap(PartyPaymentTypeRatioMap(PaymentTypeRatio(1.0), PaymentTypeRatio(1.2), PaymentTypeRatio(0.8)))
      val result = party2.warikan(BillingAmount(Money.of(30000, Money.JPY)))
      println(result)
    }
  }
}
