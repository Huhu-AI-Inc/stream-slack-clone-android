package io.getstream.slackclone.chatcore.data

interface UiLayerChannels {
  data class SlackChannel(
    val name: String?,
    val isPrivate: Boolean?,
    val uuid: String?,
    val createdDate: Long?,
    val modifiedDate: Long?,
    val isMuted: Boolean?,
    val isOneToOne: Boolean?,
    val pictureUrl: String?
  )

  /**
   * Chat Designer Session Item
   */
  data class ChatDesignerSession(
    val sessionId: String,
    val sessionName: String,
    val createTime: Long,
    val lastModified: Long,
    val imgSize: String,
    val imgUrl: String
  )
}
