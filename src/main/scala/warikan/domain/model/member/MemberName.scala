package warikan.domain.model.member

/**
  * 参加者名。
  *
  * @param value
  */
final case class MemberName(value: String) {
  require(value.nonEmpty)
}
