package warikan.domain.model.member

object Members {
  val empty: Members = new Members(Seq.empty: _*)
}

/**
 * 参加者のファーストクラスコレクション
 */
case class Members(values: Member*) {
  def removeMembers(memberIds: MemberIds): Members = Members(values.filterNot(e => memberIds.values.contains(e.id)): _*)

  def combine(other: Members): Members = Members(values ++ other.values: _*)



}
