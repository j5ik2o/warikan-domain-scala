package warikan.domain.model.money

import java.util.{ Currency, Locale }

import scala.math.BigDecimal
import scala.math.BigDecimal.RoundingMode
import scala.math.BigDecimal.RoundingMode.RoundingMode

/** お金。 */
class Money private (private[money] val amount: BigDecimal, private[money] val currency: Currency)
    extends Ordered[Money] {
  import Money._

  private def hasSameCurrencyAs(arg: Money): Boolean =
    arg.currency == currency

  private def checkHasSameCurrencyAs(o: Money): Unit =
    if (!hasSameCurrencyAs(o)) {
      throw new IllegalArgumentException(s"$o is not same currency as $this")
    }

  private def adjustBy(
      rawAmount: BigDecimal,
      currency: Currency,
      roundingMode: RoundingMode = DefaultRoundingMode
  ): Money = {
    val defaultFractionDigits = currency.getDefaultFractionDigits
    val amount                = rawAmount.setScale(defaultFractionDigits, roundingMode)
    new Money(amount, currency)
  }

  def add(money: Money): Money = {
    checkHasSameCurrencyAs(money)
    adjustBy(amount.+(money.amount), currency)
  }

  def times(factor: BigDecimal): Money =
    adjustBy(amount.*(factor), currency)

  override def compare(that: Money): Int =
    amount.compare(that.amount)

}

object Money {
  val DefaultRoundingMode: RoundingMode = RoundingMode.HALF_EVEN
  val DefaultCurrency: Currency         = Currency.getInstance(Locale.getDefault)
  val JPY: Currency                     = Currency.getInstance("JPY")
  val USD: Currency                     = Currency.getInstance("USD")

  def of(rawAmount: Int, currency: Currency): Money =
    new Money(rawAmount, currency)

  def zero(currency: Currency): Money =
    new Money(BigDecimal(0), currency)

}
