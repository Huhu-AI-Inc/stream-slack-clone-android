package io.getstream.slackclone.uichat.chatthread

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.openapitools.client.apis.ChatApi
import org.openapitools.client.models.ChatBody
import org.openapitools.client.models.ChatHistory
import java.util.UUID

class ChatRepository {

  suspend fun startChat(
    sessionId: String,
    requestId: String = UUID.randomUUID().toString().lowercase(),
    chatBody: ChatBody,
    accessToken: String
  ): Result<ChatHistory> = withContext(Dispatchers.IO) {
    try {
      val data = ChatApi().createChat(sessionId, requestId, chatBody, accessToken)
      Result.success(data)
    } catch (e: Exception) {
      Result.failure(e)
    }
  }

  suspend fun pullChatHistory(
    sessionId: String,
    accessToken: String,
    startPageId: String? = null,
    endPageId: String? = null
  ): Result<ChatHistory> = withContext(Dispatchers.IO) {
    try {
      val data = ChatApi().getChat(sessionId, startPageId, endPageId, accessToken)
      Result.success(data)
    } catch (e: Exception) {
      Result.failure(e)
    }
  }
}
