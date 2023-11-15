package io.getstream.slackclone.uichat.chatthread

import kotlinx.serialization.*

@Serializable
data class ChatContent(
  val role: String,
  val content: String?, // Not being used at last
  val text: String?,
  val images: List<ImageContent>?,
  val request_time: Long?,
  val request_id: String?,
  val is_first_page: Boolean?,
  val is_regenerable: Int?,
  val num_of_images: Int?
)

@Serializable
data class ImageContent(
  val image_id: String?,
  val image_url: String?
)