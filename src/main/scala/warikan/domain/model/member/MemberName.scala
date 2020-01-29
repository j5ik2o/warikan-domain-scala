package warikan.domain.model.member

case class MemberName(value: String) {
  require(value.nonEmpty)
}
