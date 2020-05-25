package warikan.domain.model.member

import warikan.domain.model.payment.PaymentType

/**
 * 参加者リスト。
 *
 * @param head
 * @param tail
 */
final case class Members(head: Member, tail: Member*) {

  private val values = head :: tail.toList

  def removeMembers(memberIds: MemberIds): Members = {
    val head :: tail = values.filterNot(e => memberIds.values.contains(e.id))
    Members(head, tail: _*)
  }

  def combine(other: Members): Members = {
    val head :: tail = values ++ other.values
    Members(head, tail: _*)
  }

  def paymentTypes: Map[Member, PaymentType.Value] = values.map { m => (m, m.paymentType) }.toMap

  def map[A](f: Member => A): Seq[A] = values.map(f)
}
