package warikan.domain.model

import java.time.Instant

import org.scalatest.freespec.AnyFreeSpec
import warikan.domain.model.money.Money

class NomikaiSpec extends AnyFreeSpec {
  "Nomikai" - {
    "メンバーが3人で請求金額が6000円で多め1.2普通1少なめ0.8でワリカンする" in {
      val bounenkai = new Nomikai(
        Name("あだちーむ忘年会"),
        HeldDate(Instant.now),
        Members(
          Vector(
            Member(MemberId("sekine"), PaymentSection.多め),
            Member(MemberId("suzuki"), PaymentSection.普通),
            Member(MemberId("iimori"), PaymentSection.少なめ)
          )
        ),
        new TotalPrice(Money(6_000, Money.JPY))
      )

      val paymentMap = PaymentMap(Map(
        PaymentSection.多め -> PaymentRatio(1.2),
        PaymentSection.普通 -> PaymentRatio(1.0),
        PaymentSection.少なめ -> PaymentRatio(0.8),
      ))

      bounenkai.warikan(paymentMap)
      succeed
    }
  }
}
