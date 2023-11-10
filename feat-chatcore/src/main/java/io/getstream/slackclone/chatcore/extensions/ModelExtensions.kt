package io.getstream.slackclone.chatcore.extensions

import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.Channel
import io.getstream.chat.android.client.models.Member
import io.getstream.chat.android.client.models.User
import io.getstream.slackclone.chatcore.data.UiLayerChannels
import io.getstream.slackclone.domain.model.message.DomainLayerMessages
import java.util.Date
import kotlin.random.Random

fun UiLayerChannels.SlackChannel.toStreamChannel(): Channel {
  return Channel(
    id = uuid ?: "",
    cid = "messaging:$uuid",
    type = "messaging",
    name = name ?: "",
    image = pictureUrl ?: "",
    hidden = isPrivate ?: false,
    members = listOf(
      Member(User(name = name ?: "")),
      Member(ChatClient.instance().getCurrentUser() ?: User())
    ),
    createdAt = Date(createdDate ?: System.currentTimeMillis()),
    updatedAt = Date(modifiedDate ?: System.currentTimeMillis()),
    memberCount = if (isOneToOne == true) 2 else Random.nextInt(100) + 2,
  )
}

/**
 *  Chat Designer
 */
fun UiLayerChannels.SlackSession.toStreamChannel(): Channel {
  return Channel(
    id = sessionId,
    cid = "session:$sessionId", // Assuming 'session' is a valid type for your use case
    type = "session",
    name = sessionName,
    image = imgUrl,
    // Other properties can be set according to your requirements
    createdAt = Date(createTime),
    updatedAt = Date(lastModified),
    memberCount = 2,
  )
}

fun Channel.toSlackUIChannel(): UiLayerChannels.SlackChannel {
  return UiLayerChannels.SlackChannel(
    name = name,
    isPrivate = hidden,
    uuid = id,
    createdDate = createdAt?.time ?: System.currentTimeMillis(),
    modifiedDate = updatedAt?.time ?: System.currentTimeMillis(),
    isMuted = false,
    isOneToOne = true,
    pictureUrl = image
  )
}

fun Channel.toSlackDomainChannelMessage(): DomainLayerMessages.SlackMessage {
  return DomainLayerMessages.SlackMessage(
    uuid = id,
    channelId = cid,
    message = messages.firstOrNull()?.text ?: "Direct message",
    userId = members.firstOrNull()?.user?.id ?: "",
    createdBy = members.firstOrNull()?.user?.name ?: "",
    createdDate = createdAt?.time ?: System.currentTimeMillis(),
    modifiedDate = updatedAt?.time ?: System.currentTimeMillis(),
  )
}
