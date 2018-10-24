package com.allantl.jira4s.v2.domain.enums

import cats.Show

sealed trait SearchExpand

object SearchExpand {

  case object RenderedFields extends SearchExpand
  case object Names extends SearchExpand
  case object Schema extends SearchExpand
  case object Transitions extends SearchExpand
  case object Operations extends SearchExpand
  case object EditMeta extends SearchExpand
  case object Changelog extends SearchExpand
  case object VersionedRepresentations extends SearchExpand

  implicit val show: Show[SearchExpand] = Show.show {
    case RenderedFields => "renderedFields"
    case Names => "names"
    case Schema => "schema"
    case Transitions => "transitions"
    case Operations => "operations"
    case EditMeta => "editmeta"
    case Changelog => "changelog"
    case VersionedRepresentations => "versionedRepresentations"
  }
}
