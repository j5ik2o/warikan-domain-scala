package warikan.domain.model.member

/**
  * 参会者IDリスト。
  *
  * @param values
  */
final case class MemberIds(values: Seq[MemberId]) {
  require(values.nonEmpty)
}
