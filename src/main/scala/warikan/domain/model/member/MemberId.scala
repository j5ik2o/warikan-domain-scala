package warikan.domain.model.member

/**
  * 参加者ID。
  *
  * @param value
  */
final case class MemberId(value: Long) {
  require(value > 0)
}
