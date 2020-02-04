package warikan.domain.model.member

/**
 * 参加者名
 */
case class MemberName(value: String) {
  require(value.nonEmpty)
}
