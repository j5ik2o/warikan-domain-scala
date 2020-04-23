package warikan.domain.model.money

import java.util.{ Currency, Locale }

import scala.math.BigDecimal

/**
  * 金額を表すクラス。
  *
  * ある一定の「量」と「通貨単位」から成るクラスである。
  *
  * @author j5ik2o
  * @param amount 量 [[scala.math.BigDecimal]]
  * @param currency 通貨単位 [[java.util.Currency]]
  */
final class Money(private val amount: BigDecimal, private val currency: Currency)
    extends Ordered[Money]
    with Serializable {

  require(
    amount.scale == currency.getDefaultFractionDigits,
    "Scale of amount does not match currency"
  )

  override def equals(obj: Any): Boolean = obj match {
    case that: Money => amount == that.amount && currency == that.currency
    //    case bd: BigDecimal => amount == bd
    //    case n: Int => amount == n
    //    case f: Float => amount == f
    //    case d: Double => amount == d
    case _ => false
  }

  override def hashCode: Int = 31 * (amount.hashCode + currency.hashCode)

  /**
    * Returns a [[Money]] whose amount is
    * the absolute amount of this [[Money]], and whose scale is this.scale().
    *
    * @return 絶対金額
    */
  lazy val abs: Money = Money(amount.abs, currency)

  /**
    * 金額同士の比較を行う。
    *
    * 相対的に量が小さい方を「小さい」と判断する。通貨単位が異なる場合は [[java.lang.ClassCastException]] を
    * スローするが、どちらか片方の量が`0`である場合は例外をスローしない。
    *
    * 例えば`10 USD`と`0 JPY`は、後者の方が小さい。
    * また、`0 USD`と`0 JPY`は同じである。
    *
    * @param that 比較対象
    * @return `Comparable.compareTo(Object)`に準じる
    */
  override def compare(that: Money): Int = {
    require(currency == that.currency)
    amount compare that.amount
  }

  def /(divisor: Double): Money = dividedBy(divisor)

  def *(other: BigDecimal): Money = times(other)

  def +(other: Money): Money = {
    require(currency == other.currency)
    add(other)
  }

  def -(other: Money): Money = {
    require(currency == other.currency)
    subtract(other)
  }

  //  /**
  //   * この金額に対して、指定した`ratio`の割合の金額を返す。
  //   *
  //   * @param ratio 割合
  //   * @param scale スケール
  //   * @param roundingMode 丸めモード
  //   * @return 指定した割合の金額
  //   */
  //  def applying(ratio: Ratio, scale: Int, roundingMode: BigDecimal.RoundingMode.Value): Money = {
  //    val newAmount = ratio.times(amount).decimalValue(scale, roundingMode)
  //    Money.adjustBy(newAmount, currency)
  //  }

  //  /**
  //   * この金額に対して、指定した`ratio`の割合の金額を返す。
  //   *
  //   * @param ratio 割合
  //   * @param roundingMode 丸めモード
  //   * @return 指定した割合の金額
  //   */
  //  def applying(ratio: Ratio, roundingMode: BigDecimal.RoundingMode.Value): Money =
  //    applying(ratio, currency.getDefaultFractionDigits, roundingMode)

  /**
    * このオブジェクトの`amount`フィールド（量）を返す。
    *
    * CAUTION: このメソッドは、このオブジェクトがカプセル化する要素を外部に暴露する。取り扱いには充分注意のこと。
    *
    * How best to handle access to the internals? It is needed for
    * database mapping, UI presentation, and perhaps a few other
    * uses. Yet giving public access invites people to do the
    * real work of the Money object elsewhere.
    * Here is an experimental approach, giving access with a
    * warning label of sorts. Let us know how you like it.
    *
    * @return 量
    */
  val breachEncapsulationOfAmount: BigDecimal = amount

  /**
    * このオブジェクトの`currency`フィールド（通貨単位）を返す。
    *
    * CAUTION: このメソッドは、このオブジェクトがカプセル化する要素を外部に暴露する。取り扱いには充分注意のこと。
    *
    * @return 通貨単位
    */
  val breachEncapsulationOfCurrency: Currency = currency

  /**
    * この金額を、`divisor`個に均等に分割した場合の金額を返す。
    *
    * 丸めモードは `RoundingMode#HALF_EVEN` を適用する。
    *
    * @param divisor 除数
    * @return 金額
    */
  def dividedBy(divisor: Double): Money =
    dividedBy(divisor, Money.DefaultRoundingMode)

  /**
    * この金額を、`divisor`個に均等に分割した場合の金額を返す。
    *
    * @param divisor 除数
    * @param roundingMode 丸めモード
    * @return 金額
    */
  def dividedBy(divisor: BigDecimal, roundingMode: BigDecimal.RoundingMode.Value): Money = {
    val newAmount =
      amount.bigDecimal.divide(divisor.bigDecimal, roundingMode.id)
    Money(BigDecimal(newAmount), currency)
  }

  /**
    * この金額を、`divisor`個に均等に分割した場合の金額を返す。
    *
    * @param divisor 除数
    * @param roundingMode 丸めモード
    * @return 金額
    */
  def dividedBy(divisor: Double, roundingMode: BigDecimal.RoundingMode.Value): Money =
    dividedBy(BigDecimal(divisor), roundingMode)

  //  /**
  //   * この金額の、`divisor`に対する割合を返す。
  //   *
  //   * @param divisor 除数
  //   * @return 割合
  //   * @throws ClassCastException 引数の通貨単位がこのインスタンスの通貨単位と異なる場合
  //   * @throws ArithmeticException 引数`divisor`の量が0だった場合
  //   */
  //  def dividedBy(divisor: Money): Ratio = {
  //    checkHasSameCurrencyAs(divisor)
  //    Ratio(amount, divisor.amount)
  //  }

  /**
    * このインスタンがあらわす金額が、`other`よりも大きいかどうか調べる。
    *
    * 等価の場合は`false`とする。
    *
    * @param other 基準金額
    * @return 大きい場合は`true`、そうでない場合は`false`
    * @throws ClassCastException 引数の通貨単位がこのインスタンスの通貨単位と異なる場合
    */
  def isGreaterThan(other: Money): Boolean =
    this > other

  /**
    * このインスタンがあらわす金額が、`other`よりも小さいかどうか調べる。
    *
    * 等価の場合は`false`とする。
    *
    * @param other 基準金額
    * @return 小さい場合は`true`、そうでない場合は`false`
    * @throws ClassCastException 引数の通貨単位がこのインスタンスの通貨単位と異なる場合
    */
  def isLessThan(other: Money): Boolean = this < other

  /**
    * このインスタンがあらわす金額が、負の金額かどうか調べる。
    *
    * ゼロの場合は`false`とする。
    *
    * @return 負の金額である場合は`true`、そうでない場合は`false`
    */
  lazy val isNegative: Boolean = amount < BigDecimal(0)

  /**
    * このインスタンがあらわす金額が、正の金額かどうか調べる。
    *
    * ゼロの場合は`false`とする。
    *
    * @return 正の金額である場合は`true`、そうでない場合は`false`
    */
  lazy val isPositive: Boolean = amount > BigDecimal(0)

  /**
    * このインスタンがあらわす金額が、ゼロかどうか調べる。
    *
    * @return ゼロである場合は`true`、そうでない場合は`false`
    */
  lazy val isZero: Boolean =
    equals(Money.adjustBy(0.0, currency))

  /**
    * この金額から`other`を差し引いた金額を返す。
    *
    * @param other 金額
    * @return 差し引き金額
    * @throws ClassCastException 引数の通貨単位がこのインスタンスの通貨単位と異なる場合
    */
  def subtract(other: Money): Money =
    add(other.negated)

  /**
    * Returns a `Money` whose amount is (-amount), and whose scale is this.scale().
    *
    * @return 金額
    */
  lazy val negated: Money =
    Money(BigDecimal(amount.bigDecimal.negate), currency)

  //  /**
  //   * 指定した時間量に対する、この金額の割合を返す。
  //   *
  //   * @param duration 時間量
  //   * @return 割合
  //   */
  //  def per(duration: Duration): MoneyTimeRate =
  //    new MoneyTimeRate(this, duration)

  /**
    * この金額に`other`を足した金額を返す。
    *
    * @param other 金額
    * @return 足した金額
    * @throws ClassCastException 引数の通貨単位がこのインスタンスの通貨単位と異なる場合
    */
  def add(other: Money): Money = {
    checkHasSameCurrencyAs(other)
    Money.adjustBy(amount + other.amount, currency)
  }

  /**
    * この金額に`factor`を掛けた金額を返す。
    *
    * 丸めモードは `RoundingMode#HALF_EVEN` を適用する。
    *
    * TODO: Many apps require carrying extra precision in intermediate
    * calculations. The use of Ratio is a beginning, but need a comprehensive
    * solution. Currently, an invariant of Money is that the scale is the
    * currencies standard scale, but this will probably have to be suspended or
    * elaborated in intermediate calcs, or handled with defered calculations
    * like Ratio.
    *
    * @param factor 係数
    * @return 掛けた金額
    */
  def times(factor: BigDecimal): Money =
    times(factor, Money.DefaultRoundingMode)

  /**
    * この金額に`factor`を掛けた金額を返す。
    *
    * TODO: BigDecimal.multiply() scale is sum of scales of two multiplied
    * numbers. So what is scale of times?
    *
    * @param factor 係数
    * @param roundingMode 丸めモード
    * @return 掛けた金額
    */
  def times(factor: BigDecimal, roundingMode: BigDecimal.RoundingMode.Value): Money =
    Money.adjustBy(amount * factor, currency, roundingMode)

  /**
    * この金額に`amount`を掛けた金額を返す。
    *
    * 丸めモードは `RoundingMode#HALF_EVEN` を適用する。
    *
    * @param amount 係数
    * @return 掛けた金額
    */
  def times(amount: Double): Money =
    times(BigDecimal(amount))

  /**
    * この金額に`amount`を掛けた金額を返す。
    *
    * @param amount 係数
    * @param roundingMode 丸めモード
    * @return 掛けた金額
    */
  def times(amount: Double, roundingMode: BigDecimal.RoundingMode.Value): Money =
    times(BigDecimal(amount), roundingMode)

  /**
    * この金額に`amount`を掛けた金額を返す。
    *
    * 丸めモードは `RoundingMode#HALF_EVEN` を適用する。
    *
    * @param amount 係数
    * @return 掛けた金額
    */
  def times(amount: Int): Money =
    times(BigDecimal(amount))

  override def toString: String =
    currency.getSymbol + " " + amount

  /**
    * 指定したロケールにおける、単位つきの金額表現の文字列を返す。
    *
    * @param localeOption ロケールの`Option`。`None`の場合は `Locale#getDefault()` を利用する。
    * @return 金額の文字列表現
    */
  def toString(localeOption: Option[Locale]): String = {
    def createStrng(_locale: Locale) =
      currency.getSymbol(_locale) + " " + amount
    localeOption match {
      case Some(locale) => createStrng(locale)
      case None         => createStrng(Locale.getDefault)
    }
  }

  private[money] def hasSameCurrencyAs(arg: Money): Boolean =
    currency.equals(arg.currency) || arg.amount.equals(BigDecimal(0)) || amount
      .equals(BigDecimal(0))

  /**
    * この金額に、最小の単位金額を足した金額、つまりこの金額よりも1ステップ分大きな金額を返す。
    *
    * @return この金額よりも1ステップ分大きな金額
    */
  private[money] lazy val incremented: Money = add(minimumIncrement)

  /**
    * 最小の単位金額を返す。
    *
    * 例えば、日本円は1円であり、US$は1セント（つまり0.01ドル）である。
    *
    * This probably should be Currency responsibility. Even then, it may need
    * to be customized for specialty apps because there are other cases, where
    * the smallest increment is not the smallest unit.
    *
    * @return 最小の単位金額
    */
  private[money] lazy val minimumIncrement: Money = {
    val increment =
      BigDecimal(1).bigDecimal.movePointLeft(currency.getDefaultFractionDigits)
    Money(BigDecimal(increment), currency)
  }

  private def checkHasSameCurrencyAs(aMoney: Money): Unit = {
    if (!hasSameCurrencyAs(aMoney)) {
      throw new ClassCastException(
        aMoney.toString() + " is not same currency as " + this.toString()
      )
    }
  }

}

/**
  * `Money`コンパニオンオブジェクト。
  *
  * @author j5ik2o
  */
object Money {

  val DefaultCurrency: Currency = Currency.getInstance(Locale.getDefault)

  //implicit def bigDecimalToMoney(amount: Int) = apply(amount)

  val USD = Currency.getInstance("USD")

  val EUR = Currency.getInstance("EUR")

  val JPY = Currency.getInstance("JPY")

  val DefaultRoundingMode: BigDecimal.RoundingMode.Value =
    BigDecimal.RoundingMode.HALF_EVEN

  def apply(amount: BigDecimal, currency: Currency): Money =
    new Money(amount, currency)

  def unapply(money: Money): Option[(BigDecimal, Currency)] =
    Some(money.amount, money.currency)

  /**
    * `amount`で表す量のドルを表すインスタンスを返す。
    *
    * This creation method is safe to use. It will adjust scale, but will not
    * round off the amount.
    *
    * @param amount 量
    * @return `amount`で表す量のドルを表すインスタンス
    */
  def dollars(amount: BigDecimal): Money = adjustBy(amount, USD)

  /**
    * `amount`で表す量のドルを表すインスタンスを返す。
    *
    * WARNING: Because of the indefinite precision of double, this method must
    * round off the value.
    *
    * @param amount 量
    * @return `amount`で表す量のドルを表すインスタンス
    */
  def dollars(amount: Double): Money = adjustBy(amount, USD)

  /**
    * This creation method is safe to use. It will adjust scale, but will not
    * round off the amount.
    * @param amount 量
    * @return `amount`で表す量のユーロを表すインスタンス
    */
  def euros(amount: BigDecimal): Money = adjustBy(amount, EUR)

  /**
    * WARNING: Because of the indefinite precision of double, this method must
    * round off the value.
    * @param amount 量
    * @return `amount`で表す量のユーロを表すインスタンス
    */
  def euros(amount: Double): Money = adjustBy(amount, EUR)

  /**
    * [[scala.Iterable]]に含む全ての金額の合計金額を返す。
    *
    * 合計金額の通貨単位は、 `monies`の要素の（共通した）通貨単位となるが、
    * `Collection`が空の場合は、現在のデフォルトロケールにおける通貨単位で、量が0のインスタンスを返す。
    *
    * @param monies 金額の集合
    * @return 合計金額
    * @throws ClassCastException 引数の通貨単位の中に通貨単位が異なるものを含む場合。
    * 				ただし、量が0の金額については通貨単位を考慮しないので例外は発生しない。
    */
  def sum(monies: Iterable[Money]): Money = {
    if (monies.isEmpty) {
      Money.zero(Currency.getInstance(Locale.getDefault))
    } else {
      monies.reduceLeft(_ + _)
    }
  }

  /**
    * This creation method is safe to use. It will adjust scale, but will not
    * round off the amount.
    *
    * @param amount 量
    * @param currency 通貨単位
    * @return 金額
    */
  def adjustBy(amount: BigDecimal, currency: Currency): Money =
    adjustBy(amount, currency, BigDecimal.RoundingMode.UNNECESSARY)

  /**
    * For convenience, an amount can be rounded to create a Money.
    *
    * @param rawAmount 量
    * @param currency 通貨単位
    * @param roundingMode 丸めモード
    * @return 金額
    */
  def adjustBy(rawAmount: BigDecimal, currency: Currency, roundingMode: BigDecimal.RoundingMode.Value): Money = {
    val amount =
      rawAmount.setScale(currency.getDefaultFractionDigits, roundingMode)
    new Money(amount, currency)
  }

  /**
    * WARNING: Because of the indefinite precision of double, this method must
    * round off the value.
    *
    * @param dblAmount 量
    * @param currency 通貨単位
    * @return 金額
    */
  def adjustBy(dblAmount: Double, currency: Currency): Money =
    adjustBy(dblAmount, currency, DefaultRoundingMode)

  /**
    * Because of the indefinite precision of double, this method must round off
    * the value. This method gives the client control of the rounding mode.
    *
    * @param dblAmount 量
    * @param currency 通貨単位
    * @param roundingMode 丸めモード
    * @return 金額
    */
  def adjustRound(dblAmount: Double, currency: Currency, roundingMode: BigDecimal.RoundingMode.Value): Money = {
    val rawAmount = BigDecimal(dblAmount)
    adjustBy(rawAmount, currency, roundingMode)
  }

  /**
    * This creation method is safe to use. It will adjust scale, but will not
    * round off the amount.
    *
    * @param amount 量
    * @return `amount`で表す量の円を表すインスタンス
    */
  def yens(amount: BigDecimal): Money = adjustBy(amount, JPY)

  /**
    * WARNING: Because of the indefinite precision of double, this method must
    * round off the value.
    *
    * @param amount 量
    * @return `amount`で表す量の円を表すインスタンス
    */
  def yens(amount: Double): Money = adjustBy(amount, JPY)

  /**
    * 指定した通貨単位を持つ、量が0の金額を返す。
    *
    * @param currency 通貨単位
    * @return 金額
    */
  def zero(currency: Currency): Money = adjustBy(0.0, currency)

}
