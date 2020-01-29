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

  def divide(divisor: BigDecimal, roundingMode: BigDecimal.RoundingMode.Value): Money = {
    val newAmount = amount.bigDecimal.divide(divisor.bigDecimal, roundingMode.id)
    new Money(newAmount, currency)
  }

  def isPositive: Boolean = amount > 0

  def isNegative: Boolean = !isPositive

  override def compare(that: Money): Int =
    amount.compare(that.amount)

  override def toString = s"Money($amount, $currency)"

  def canEqual(other: Any): Boolean = other.isInstanceOf[Money]

  override def equals(other: Any): Boolean = other match {
    case that: Money =>
      (that canEqual this) &&
        amount == that.amount &&
        currency == that.currency
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(amount, currency)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
}

object Money {
  val DefaultRoundingMode: RoundingMode = RoundingMode.HALF_EVEN
  val DefaultCurrency: Currency         = Currency.getInstance(Locale.getDefault)
  val JPY: Currency                     = Currency.getInstance("JPY")
  val USD: Currency                     = Currency.getInstance("USD")

  def apply(rawAmount: Int, currency: Currency): Money =
    new Money(rawAmount, currency)

  def zero(currency: Currency): Money =
    new Money(BigDecimal(0), currency)

}
