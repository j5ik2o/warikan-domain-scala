以下の記事内容のライセンスは [CC BY-SA 4.0](https://creativecommons.org/licenses/by-sa/4.0/deed.ja) です。

# ドメイン駆動設計をわかりやすく - ドメインのモデル設計を手を動かしながら学ぼう

ドメイン駆動設計（DDD）が近年関心を集めていますが、同時にこの設計思想は難しい、わかりにくい、という見方もあります。さまざまなプロジェクトでドメイン駆動設計を実践してきたかとじゅんさんが、サンプル課題をもとに、ユースケース分析、モデル設計といった基礎を解説します。

はじめまして、Chatworkでテックリードをしている、かとじゅん（ [@j5ik2o](https://twitter.com/j5ik2o) ）です。 僕は2010年ころより、大小さまざまなプロジェクトでドメイン駆動設計、いわゆるDDD（Domain Driven Design）を導入した開発を実践してきました。ドメイン駆動設計を主題としたワークショップなども主宰していますが、最近では加速度的にこの設計思想への関心が高まっていると感じます。本稿では、なにかと分かりにくいドメイン駆動設計の基本を、架空の課題とその解決を通じ、手を動かしながら学ぶことを目的としています。

サンプルコードは僕が得意とし、また数多くのドメイン駆動設計を取り入れたプロジェクトで使用してきたScalaで記述します。できるだけシンプルな構文での記述を心がけているので、多くの方に雰囲気はご理解いただけると思います。

## ソフトウェア要求は複雑化し迅速な変化が求められている

さて、ドメイン駆動設計が継続して注目を集めるのはなぜでしょうか。僕の家の隣にある、何十年も歴史があるCD屋が最近閉店しました。シャッターに貼られた閉店のお知らせを眺める年配の通行人の姿は、時代の移り変わりを感じさせます。一昔前は音楽といえばCDが主流でしたが、今ではストリーミングサービスの方が一般的でしょう。

音楽にかぎらず、キャッシュレス決済サービスや、メッセンジャーサービスなど、僕らは便利な“サービス”を日々利用しており、「サービスの継続的な利用」が当たり前になってきました。さらに、新型コロナウイルスの現下やそれ以降においても、ソフトウェアとしてのサービスの需要や必然性は増していくでしょう。こうした時代背景を踏まえると、ソフトウェアは社会変化やより複雑な要求への迅速な対応が求められ、その点において、ドメイン駆動設計に関心が集まっているのだと感じています。

## 【DDD前史】構造化プログラミングからオブジェクト指向へ

複雑な要求、課題に対応するには、それを解くためのうまいやり方を見つけ、ソフトウェアに反映する必要があります。その「うまいやり方」こそドメインモデルであり、ドメイン駆動設計はドメインモデル実現のための設計手法です。現在でこそ一般化した手法ですが、ドメイン駆動設計の前段階である、「構造化プログラミング」から「オブジェクト指向」へといたる歴史を振り返ると、より理解が深まるでしょう。

「構造化プログラミング」は実現する機能に注目し、手続きの流れやデータ構造に注目するアプローチ、いわゆる手続き型です。ここでは詳しく述べませんが、1980年代に人気を博し一世を風靡しましたが、同時に以下の問題を残しました。

- 手続き中心の考え方では、人間の優れた抽象化能力を使うことが難しい
- 手続きとデータの分離によって、データ構造変更時の影響を限定することが難しい
- データに対する手続きを重複して実装してしまうことがあり、それがバグの温床になった

これらの問題を解決するために、1980年代の終わりに「オブジェクト指向」が登場しました。オブジェクト指向は、「データ」と「手続き」をグルーピングした部品を最小単位とし、オブジェクト間でメッセージを交換するというものでした。

オブジェクトは一つの役割を持つため、実世界から抽象化したモデルをコンピュータ上に実現できます。このような考え方を利用してオブジェクト中心の分析・設計をすれば、問題を自然な形で把握できると考えられました。モデルを実装に紐付けやすくなり、機能はオブジェクトの組み合わせによって実現する方が人にとってより自然な発想になります。また、手続き型は抽象化を反映した構造を持たないため、機能変更の際に大きな影響を受けがちです。一方で、オブジェクト指向ではオブジェクトの基本的な構造を変えることなく、メソッドの追加や変更など小さな影響範囲に止めたまま機能変更を実現できるメリットがあったのです。

ドメイン駆動設計では、ドメインのうまいやり方をソフトウェアに反映する手段として、主に「オブジェクト指向言語」を利用します。オブジェクト指向は他のモデリングパラダイムと比較して広く普及していて実用的です。そして、コードはオブジェクトとしてまとめることが可能、という技術面のメリットもあります。しかし、それ以上に「コードで表現されたモデル」が言語としての性質を持つことで、ソフトウェアに関するさまざまな活動を行いやすくなります。このようなメリットから、ドメイン駆動設計においてはオブジェクト指向が基盤として重視されるのです。

## まずはドメインオブジェクトのサンプルをコードで理解しよう

前置きはこのぐらいにして、モデルを反映したドメインオブジェクトを単純な例をみていきましょう。例えば、ECサービスの「注文」における「注文数」という概念をコードで表現する方法は、以下のようにIntのようなプリミティブな整数型で表現することが一般的です。

```scala
// 注文
class Order(orderId: OrderId, 
            itemId: ItemId,
            quantity: Int, // 注文数
            orderDateTime: LocalDateTime) {
 // ...
}
```

しかし、このコードは本当に正しいでしょうか。注文数が負の数であったり、1000個や1億個でも問題は起きないでしょうか。バリデーションは完ぺきだから大丈夫？ 果たしてそうでしょうか。

仮に、「一度の注文で指定できる注文数は1～99個まで」というビジネス・ルール（不変条件とも表現されます）があるとしたら、この実装はビジネス・ルールを表現できていません。`new Order(..., .., quantity = 1000, ...)`といったルールを壊すような「注文」ができてしまうからです。

他に、`quantity: Int`をコンストラクタで表明するという以下のような方法も思いつきます。

```scala
// 注文
class Order(/** ... */
            quantity: Int, // 注文数
            /** ... */) {
  require(quantity >= 1, "The quantity is less than 1")
  require(quantity <= 99, "The quantity is greater than 99")
}

class OrderSpec {
  // 注文数のテスト
}

// 返金
class Refund(/** ... */
             quantity: Int, // 注文数
             /** ... */) {
  require(quantity >= 1, "The quantity is less than 1")
  require(quantity <= 99, "The quantity is greater than 99")
}

class RefundSpec {
  // 注文数のテスト
}
```

これはよいアイデアですが、返金でも注文数を扱う場合はDRY原則に反するので注意が必要です。またテストも複製が必要になる可能性があります。さらに、注文数に関するビジネス・ルールが変化した場合、それなりの変更コストを払うことになりますし、変更漏れのリスクも考えられます。

こうした課題に対処する方法はいくつかありますが、ドメイン駆動設計としてよい方法は、以下のように「注文数」を表現する型を定義することです。一般的にはクラスを利用します。そのクラスはビジネス・ルールを表明するため、誤った値は受け付けません。

上記のコード例のクラスでは、`require`を使ってビジネス・ルールを表明しています。第一引数の条件が成り立たない場合、第二引数のメッセージを持つ`IllegalArgumentException`がスローされ、インスタンスは生成されません。つまり、ビジネス・ルールに則していない、不正なオブジェクトは作られなくなります。

注文だけではなく、返金でも注文数を扱う場合は「注文数」クラスを利用するだけで済みます。重複して表明する必要はありませんし、注文数に関するテストも複製する必要がありません。

```scala
// 注文数
class OrderQuantity(value: Int) {
  require(value >= 1, "The quantity is less than 1")
  require(value <= 99, "The quantity is greater than 99")
  // ...
}

// 注文
class Order(/** ... */
            quantity: OrderQuantity, // 注文数
            /** ... */) {
  // ...
}

// 返金
class Refund(/** ... */
             quantity: OrderQuantity, // 注文数
             /** ... */) {
  // ...
}

class OrderQuantitySpec {
  // 注文数のテスト
}
```

この例では初期化に渡された値域を表明するだけですが、インスタンス化した後も注文数に対する操作を提供することで、誤った計算を排除できます。例えば、以下のように「注文数」を1個だけ増やすメソッドは、正しい数量計算を提供します。また、99個のときに注文数を増やそうとすると失敗すべきでしょう。失敗の表現を例外とするか、Eitherなどの型として表現するかはさておき、以下のようにビジネス・ルールを反映した正しい振る舞いを使えば、注文数を間違えることはありません。

```scala
case class OrderQuantity(value: Int) {
  require(value >= 1, "The quantity is less than 1")
  require(value <= 99, "The quantity is greater than 99")

  // 注文数を増やす
  def increment: OrderQuantity = {
    update(value + 1)
  }

  // 注文数をn個増やす
  def update(n: Int): OrderQuantity = {
    new OrderQuantity(n) 
  }
}
```

```scala
// 注文数99個のときに呼び出すと例外を送出する
val newQuantity = qauntity.incrementQuantity
```

「注文数」に関する計算・判断・加工などを一つの部品(クラス)にまとめることで、ドメインに関する知識やルールを凝集できます。プリミティブ型では、こういった知識やルールはむしろ値から離れて拡散する傾向にあります。注文数という概念を会話やドキュメントやコードに表現するとき、ひとかたまりに扱えるほうが実用的です。ビジネス・ルールが変化したときでも、概念と対応するオブジェクト群の構造が変化に柔軟に対応するでしょう。注文数を例にとって説明しましたが、他のビジネス上の概念においても同様に考えることができるでしょう。

参考: [ドメインロジックはドメインオブジェクトに凝集させる](https://qiita.com/j5ik2o/items/2ce5c1dafd2213ef4911)

## 例題で学ぶドメイン駆動設計

ここからは、ドメインオブジェクトのもう少し具体な例を考えてみましょう。今回は「飲み会のお勘定を割り勘する計算システム」を例に考えていきます。今回のシステムで想定するユースケースは、便宜上、以下とします。

- 幹事が **飲み会** を開催する（システムには含まない）
- 幹事が、システム上で、開催した **飲み会** の **名前**, **開催日時** などを設定する
- 幹事が、システム上で、開催した **飲み会** の **参加者** を追加/削除する
- 幹事が、システム上で、開催した **飲み会** の **支払区分** （多め,普通,少なめ）ごとに **支払割合** を設定する
- 幹事が、システム上で、開催した **飲み会** の **請求金額** を設定する
- 幹事が、システムを利用して **飲み会** の **参加者ごとの支払金額** を計算する

### ユースケースを分析し、ドメインを理解する

ドメイン駆動設計を適切に実行するには、ユースケースの分析が不可欠です。今回のユースケースの目的は、幹事から割り勘計算という負担を軽減することです。この目的をさらに具体化すると、「飲み会のお勘定から、参加者それぞれが支払うべき金額を計算する」と設定できます。この計算を「割り勘計算」とわかりやすく呼ぶことにします。

次に必要なのは、割り勘計算の理解です。設計というとついついデータベースのテーブルやERDの話が先行しがちですが、まずは対象ドメインの理解を深めることが重要です。

さて、今回の要求で注目すべきは、参加者数で均等に割るような単純な割り勘ではない、ということです。飲み会に最初から参加していた人なら多めに払い、後から参加した人なら少なめ、といった調整ができる割り勘計算が求められています。

ユースケースに示したとおり、`多め,普通,少なめ`の支払区分に応じて支払割合が設定さねばなりません。たとえば、`普通`の支払金額を1とした場合、多めは1.2倍、少なめは0.8倍とする想定で考えてみましょう。

```scala
普通の支払金額 Ma = Ma * 1
多めの支払金額 La = Ma * 1.2
少なめの支払金額 Sa = Ma * 0.8
```

となるので、

```scala
La = Ma * 多めの支払割合 Lr // (式1)
Sa = Ma * 少なめの支払割合 Sr // (式2)
```

と考えることができます。普通の支払金額が分かればすべてを解決できそうです。 さらに、割り勘は以下の式が成り立つはずです。

```scala
// (式3)
請求金額 Ta = (普通の支払金額 Ma * 普通の人数 Mn) + (多めの支払金額 La * 多めの人数 Ln) + (少なめの支払金額 Sa * 少なめの人数 Sn) + おつり C
```

おつりが生じないケースならば計算は簡単ですが、参加者ごとの支払金額が1円以下の端数を持つ場合、端数の扱いによっておつりなどの過不足が生じる可能性があります。日本円では一般的に1円以下は扱わないので、参加者ごとの支払金額は1円単位に丸める必要があります。ただ、丸め方によっては、集めたお金が数円足りななくなってしまう場合があります。今回は幹事が計算する手間を減らす、という目的に照らし、端数は「1円に切り上げ」とします。

それでは、式1・2・3の連立方程式から`普通`の支払金額であるMaを求めてみましょう。変形の途中で括りだした項 `(1 * Mn + Lr * Ln + Sr * Sn)` は、割合と人数を掛けたものなので、加重和(WeightedSum)と呼ぶことにします。

```scala
La = Ma * Lr
Sa = Ma * Sr

Ta = (Ma * Mn) + (La * Ln) + (Sa * Sn)
   = (Ma * Mn) + (Lr * Ma * Ln) + (Sr * Ma * Sn)
   = Ma * (1 * Mn + Lr * Ln + Sr * Sn) // (1 * Mn + Lr * Ln + Sr * Sn) = 加重和
Ma = Ta / (1 * Mn + Lr * Ln + Sr * Sn)
```

普通の支払金額Maは以下の式で求めることができます。

```scala
加重和 Ws　= 普通の支払割合 1 * 普通の人数 Mn + 多めの支払割合 Lr * 多めの人数 Ln + 少なめの支払割合 Sr * 少なめの人数 Sn
普通の支払金額 Ma = 請求金額 Ta / 加重和 Ws
```

実際の例で計算結果を確かめてみましょう。

```scala
# 30000円を、普通1人、多め1人、少なめ2人で割り勘する場合
# 多めの支払割合 = 1.2, 少なめの支払割合 = 0.8
普通の支払金額 Ma = 請求金額 30000円 / (1 * 2 + 1.2 * 1 + 0.8 * 2)
普通の支払金額 Ma = 6250円
多めの支払金額 La = 6250円 * 1.2 = 7500円
少なめの支払金額 Sa = 6250円 * 0.8 = 5000円
おつり = 30000円 - (6250円 * 2 + 7500円 * 1 + 5000円 * 2) = 0円
```

上記はおつりが生じないシンプルな計算ですが、下記のように1円以下の端数が生じる場合は1円に切り上げて、少し余分にお金を集め、生じたおつりの配分はグループの判断に任せることにします。

```scala
普通の支払金額 = 35000円 / (1 * 2 + 1.2 * 1 + 0.8 * 2) = 7291.66667円 → 7292円
多めの支払金額 La = 7291.66667円 * 1.2 = 8750円
少なめの支払金額 Sa = 7291.66667円 * 0.8 = 5833.333333円 → 5834円
おつり = 35000円 - (7292円 * 2 + 8750円 * 1 + 5834円 * 2) = 2円
```

### ドメインモデルを考えながら設計のアウトラインをつくる

これで計算の全貌が明らかになったので、私なりのモデルを作っていきましょう。「私なりの」と前置きしたのは、それもそのはずモデラーが100人いれば100通りのモデルができるからです。皆さんも自分なりのモデルをイメージしながら読んでみてください。

前述したユースーケース分析から考えたモデルのラフな案は以下です。

![https://eh-career.com/image/contents_hub/17/17/17_02.jpg](https://eh-career.com/image/contents_hub/17/17/17_02.jpg)

- Party（飲み会）
- Member（参加者）
- PaymentType（支払区分）
- PaymentRatio（支払割合）
- PaymentTypeRatios（支払割合設定）
- WeightedSum（加重和）
- BillingAmount（請求金額）
- PaymentAmount（支払金額）

特に請求金額（BillingAmount）と加重和（WeightedSum）から支払金額を計算する部分は割り勘計算に不可欠な部分です。

それでは、モデル案を基に対応する型を定義していきましょう。この段階では実装の詳細にこだわりすぎず、全体像や抽象を捉えるようなイメージで進めます。

最初は飲み会（Party）とその周辺の型から考えます。できるだけ抽象的に型を表現をするために、以下のコードは便宜上`trait`で示します。テストケースやアプリケーションサービス（ユースケースクラス）などでの、最終的な使い方としては以下のような形を想定しています。

```scala
// 飲み会の作成
val party = Party(PartyName("ABC"), LocalDate.now())
  .withMembers(
    Member(MemberId(1L), MemberName("KATO"), PaymentType.LARGE),
    Member(MemberId(2L), MemberName("KATSUNO"), PaymentType.LARGE),
    Member(MemberId(3L), MemberName("FUJII"), PaymentType.MEDIUM),
    Member(MemberId(4L), MemberName("HAYASHI"), PaymentType.SMALL),
    Member(MemberId(5L), MemberName("SHAKA"), PaymentType.SMALL)
  ).withPaymentTypeRatios( // 普通の支払割合は常に1とするので、多め・少なめだけの割合を指定
    small = PaymentTypeRatio(0.3),
    large = PaymentTypeRatio(1.2)
  )

// 割り勘計算の実行 
val warikan = party.warikan(BillingAmount(Money(30000, Money.JPY)))

// 各参加者の支払金額の取得
assert(warikan.paymentAmountBy(MemberId(1L)) == Some(Money(9000, Money.JPY)))
assert(warikan.paymentAmountBy(MemberId(2L)) == Some(Money(9000, Money.JPY)))
assert(warikan.paymentAmountBy(MemberId(3L)) == Some(Money(7500, Money.JPY)))
assert(warikan.paymentAmountBy(MemberId(4L)) == Some(Money(2250, Money.JPY)))
assert(warikan.paymentAmountBy(MemberId(5L)) == Some(Money(2250, Money.JPY)))
```

飲み会（Party）では、参加者（Member）は支払区分（PaymentType）を保持し、飲み会（Party）に複数登録でき、支払区分に対して支払割合を設定（PaymentTypeRatios）できます。それらが揃うと割り勘計算メソッドwarikanに請求金額（BillingAmount）を渡すと割り勘結果（Warikan）を返します。

```scala
// 飲会
trait Party {
  // 抽象タイプメンバー。実装側で具体的なEitherなどの型を指定できる
  type Result[A]

  // 後から追跡が必要ならエンティティ化してもよいかもしれないが、今は不要
  // id: PartyId

  // 飲み会名
  def name: PartyName

  // 開催日
  def date: LocalDate

  // 参加者
  def members: Seq[Member]
  
  // 参加者の追加
  def withMembers(members: Seq[Member]): Result[Party]

  // 支払割合設定
  def paymentTypeRatios: PaymentTypeRatios

  // 支払割合の設定 
  def withPaymentTypeRatios(large: PaymentRatio, medium: PaymentRatio, small: PaymentRatio): Result[Party]
  
  // 割り勘計算
  def warikan(billingAmount: BillingAmount): Result[Warikan]

}

// 参加者
trait Member {
  def id: MemberId
  def name: MemberName
  def paymentType: PaymentType
}

// 支払区分
sealed trait PaymentType
object PaymentType {
  // 多め
  case object Large extends PaymentType
  // 普通
  case object Medium extends PaymentType
  // 少なめ
  case object Small extends PaymentType
}

// 支払割合
trait PaymentRatio {
  def value: Double
}

// 支払割合の設定
trait PaymentTypeRatios {
  def paymentRatio(pt: PaymentType): PaymentRatio
}

// 加重和
trait WeightedSum {
  def value: BigDecimal
}

// 請求金額
tarit BillingAmount {
  def value: Money
}

// 支払金額
trait PaymentAmount {
  def value: Money
}

// 割り勘結果
trait Warikan {
  // メンバー毎の支払金額を返す
  def paymentAmountBy(memberId: MemberId): Option[PaymentAmount]
  // 割り勘結果の合計金額
  def totalAmount: BillingAmount
}
```

### 振る舞いの従属先を再考する

飲み会（Party）が割り勘計算を行うことに違和感を感じるかもしれません。確かに、割り勘計算は飲み会の外でやる、以下のような割り勘サービス（ドメインサービス）も想定できます。

```scala
trait WarikanService {

  def warikan(billingAmount: BillingAmount, members: Seq[Member], paymentTypeRatios: PaymentTypeRatios): Result[Warikan]

}
```

ただ、データと手続きを分離するアプローチは、データに関連するロジックがどこにあるか見通しが悪くなったり、同じようなロジックが重複して書かれてしまう弊害もあります。こういった振る舞いをエンティティや値オブジェクトに配置するか、ドメインサービスとして独立させるかは難しい議論です。いずれにしても振る舞いを短絡的にオブジェクトに押しつけることは望ましいことではありません。

ドメインサービスに頼る前に考えられるのは、飲み会と割り勘計算の関心を分けることです。以下の例では割り勘オブジェクト側に計算のすべてが移動しています。

```scala
// 飲み会
trait Party {
  // ...

  // 飲み会名
  def name: PartyName

  // 開催日
  def date: LocalDate

  // 参加者
  def members: Seq[Member]

  // 参加者の追加
  def withMembers(members: Seq[Member]): Result[Part]

}

object Warikan {

  def apply(billingAmount: BillingAmount, partyPaymentTypeRatios: PartyPaymentTypeRatios): Result[Warikan] = {
    warikan(billingAmount, partyPaymentTypeRatios)
  }

  private def warikan(
      billingAmount: BillingAmount,
      partyPaymentTypeRatios: PartyPaymentTypeRatios
  ): Warikan = ???

}

class Warikan(values: Map[Member, PaymentAmount]) {
  require(values.nonEmpty)

  // メンバー毎の支払金額を返す
  def paymentAmountBy(member: Member): Option[PaymentAmount] = values.get(member)
  def paymentAmountBy(memberId: MemberId): Option[PaymentAmount] = values.find(_._1.id == memberId).map(_._2)

  def totalAmount: PaymentTotalAmount = ???
}
```

飲み会と割り勘を一緒に扱う必要がなければ、こちらでもよいかもしれません。ただ、今回の場合は「割り勘しない飲み会はなさそう」という想定なので、よほど複雑にならない限り一緒でもよさそうです。また、過去の割り勘結果を参照する際、任意の飲み会を特定できないのは困るので、PartyIdをなどを割り勘オブジェクトに持たせる必要があるかもしれません。

また、別のアプローチとして計算などの処理能力をロールとして捉える考え方もあります。割り勘計算は飲み会が持つ計算能力ではあるが、必要な場面でのみ行使できると仮定すると、以下のような異なるモデリングもあり得そうです。

```scala
class Party {
  // ...
}

// 割り勘ロール
trait WarikanRole { this: Party =>
  
  // 支払区分の設定
  def withMemberIdWithPaymentTypes(memberIdWithPaymentTypes: (MemberId, PaymentType)*): Result[Party]

  // 支払割合の設定 
  def withPaymentTypeRatios(large: PaymentRatio, medium: PaymentRatio, small: PaymentRatio): Result[Party]
  
  // 割り勘計算
  def warikan(billingAmount: BillingAmount): Result[Warikan]

}

// 割り勘計算はWarikanRoleを持つPartyでなければできない
def warikan(party: Party with WarikanRole, billingAmount: BillingAmount): Result[Warikan] = {
  party.withMemberIdWithPaymentTypes(
    MemberId(1L) -> PaymentType.LARGE,
    MemberId(2L) -> PaymentType.LARGE,
    MemberId(3L) -> PaymentType.MEDIUM,
    MemberId(4L) -> PaymentType.SMALL,
    MemberId(5L) -> PaymentType.SMALL
  ).withPaymentTypeRatios(
    small = PaymentTypeRatio(0.3),
    large = PaymentTypeRatio(1.2)
  ).warikan(billingAmount)
}

// 飲み会に割り勘ロールを付与する
warikan(new Party(...) with WarikanRole, BillingAmount(...))
```

肥大化する飲み会の関心を分離するにはよい方法です。ただ、割り勘計算はクラスに静的に従属するより動的なものとして扱われるわけですが、実行時に振る舞いが合成されるとしても飲み会（Party）であることには変わりがありません。このようなテクニックを使うとしても、飲み会と割り勘の関心をどう扱うか、あらかじめ考えておく必要があります。

### 値を部品として構成する

振る舞いは関心次第でどこに従属させるかは変わりますが、ここでは最初のプランのまま実装します。

ここからは値に注目して値を表現する型（値オブジェクト）から実装を進めていきます。これまで整理してきた型を中心に実装を考えていきましょう。

支払区分のような区分値は列挙型で定義します。列挙型の場合は値の範囲が明確になるからです。支払区分の場合は3種類しかありませんが、 `1 = 少なめ,2 = 普通,3 = 多め`のように整数型を利用した場合、ビジネスとコード上の意図が厳密には合いません。列挙型がサポートされている言語では区分値はできる限り列挙型を利用したほうがよいでしょう。

```scala
// 支払区分
object PaymentType extends Enumeration {
  val Large, Medium, Small = Value
}
```

前節で示したとおり、支払割合にはDouble型を利用しません。不正な値を指定されると割り勘計算ができなくなってしまうので、ドメイン固有の知識を含む型を定義します。ここでは正の値を表明します。また、Orderedによって比較演算子で比較できるようになっています。

```scala
// 支払割合
final case class PaymentRatio(value: Double) extends Ordered[PaymentRatio] {
  require(value > 0)

  override def compare(that: PaymentRatio): Int =
    value compare that.value
}
```

支払区分に対する支払割合を設定する支払割合設定（PartyPaymentTypeRatios）は、少なめと多めの支払割合を設定できます。普通の支払割合は常に割合が1倍なので、コンストラクタでは支払割合を受け取りません。また、`PaymentRatio`の`Ordered`を利用して比較演算を行い、多めは少なめより必ず大きいことを表明します。

`paymentTypeRatio`メソッドは支払区分を渡すと設定されている支払割合を返します。内部に`values`というMapデータを保持し必要に応じて返すだけです。このようなMapデータをそのまま使うと、多めと少なめの大小関係が崩れたりしがちです。独自型が部品単体としてのルールを維持できるほうがよいでしょう。

```scala
// 支払割合設定
final case class PartyPaymentTypeRatios(small: PaymentRatio,  large: PaymentRatio) {
  require(large >= small)

  private val values = Map(PaymentType.Large -> large, PaymentType.Medium -> PaymentRatio(1), PaymentType.Small -> small)

  def paymentTypeRatio(t: PaymentType.Value): PaymentRatio = values(t)
}
```

加重和（WeightedSum）は支払割合を合計することで得られます。fromメソッドの引数には、参加者人数分の支払割合が渡され合計が求められます。

```scala
// 加重和
object WeightedSum {

  def from(head: PaymentRatio, tail: PaymentRatio*): WeightedSum = {
    tail.foldLeft(WeightedSum(head.value)) { (weightedSum, paymentRatio) =>
      weightedSum.add(paymentRatio)
    }
  }

}

final case class WeightedSum(value: Double) {
  require(value >= 0)

  def add(ratio: PaymentRatio): WeightedSum =
    copy(value = value + ratio.value)

}
```

```scala
// 請求金額
final case class BillingAmount(value: Money) {
  require(!value.isNegative)

  def subtract(totalAmount: PaymentTotalAmount): BillingAmount =
    BillingAmount(value - totalAmount.value)

  def divide(weightedSum: WeightedSum): PaymentAmount =
    PaymentAmount(value.dividedBy(weightedSum.value, BigDecimal.RoundingMode.HALF_EVEN))

}

// 支払金額
final case class PaymentAmount(value: Money) {
  require(value.isPositive)

  def times(factor: PaymentRatio): PaymentAmount =
    copy(value.times(factor.value))

}
```

最後は飲み会（Party）と割り勘結果（Warikan）です。請求金額を加重和（WeightedSum）で割れば普通の支払金額が計算されるので、参加者ごとの支払割合を掛ければ支払金額が計算できます。設計がドメインモデルに基づいているので、違和感なくコードが読めるのではないでしょうか。

```scala
final case class Party(
    name: PartyName,
    date: LocalDate,
    members: Members,
    partyPaymentTypeRatios: PartyPaymentTypeRatios
) {

  // ...

  // 割り勘計算
  def warikan(billingAmount: BillingAmount): Warikan = {
    // 普通の支払金額 = 請求金額 / 加重和
    val paymentBaseAmount = billingAmount.divide(weightedSum(partyPaymentTypeRatios))
    // 参加者ごとの支払金額を計算
    val result = members.paymentTypes.map{ case (member, paymentType) =>
      // 支払区分から支払割合を取得する
      val paymentRatio = partyPaymentTypeRatios.paymentTypeRatio(paymentType)
      // 普通の支払金額に支払割合を掛ける
      member -> paymentBaseAmount.times(paymentRatio)
    }
    Warikan(result)
  }
  
  // 加重和を求める
  private def weightedSum(partyPaymentTypeRatios: PartyPaymentTypeRatios): WeightedSum = {
    // 全参加者が持つ支払割合のコレクションを作る
    val paymentRatios = members.map { member => 
      partyPaymentTypeRatios.paymentTypeRatio(member.paymentType)
    }
    // 支払割合のコレクションから加重和を求る
    WeightedSum.from(paymentRatios.head, paymentRatios.tail: _*)
  }

}

// 割り勘計算結果
final case class Warikan(result: Map[Member, PaymentAmount]) {
  require(result.nonEmpty)

  def paymentAmountBy(member: Member): Option[PaymentAmount] = result.get(member)

  def paymentAmountBy(memberId: MemberId): Option[PaymentAmount] = result.find(_._1.id == memberId).map(_._2)

  def totalAmount: PaymentTotalAmount = {
    val ta = result.values.foldLeft(Money.zero(Money.DefaultCurrency)) { (result, elem) =>
      result + elem.value
    }
    PaymentTotalAmount(ta)
  }

}
```

## まとめ

今回紹介したのは、ユースケース分析時点で注目したドメインモデルに基づき、実装段階では対応する型を積み上げていく、ボトムアップのスタイルです。多くの現場では、この方法とは反対にアプリケーションサービスやユースケースクラスなどの実装から始めるトップダウンのスタイルを採用していると思います。

しかしながら、トップダウンの場合、処理ステップの羅列を生み出す傾向が強く、要求が変化したときに処理ステップ全域に影響が波及しやすいと考えています。今回のボトムアップのスタイルは、部品による構造が変更の影響をできるだけ小さくする効果も見込めます。また、ドメインモデルに基づくことで、ソフトウェア設計をわかりやすくするだけではなく、言語としての使い勝手のよさがチームのあらゆる活動の支えになります。これらの考え方が、現場での複雑なソフトウェア開発において一助になれば幸いです。

なお、今回の課題である割り勘計算に関する具体的なコードは、[こちらのリポジトリ](https://github.com/j5ik2o/warikan-domain-scala)も参照してください。
