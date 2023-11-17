package io.getstream.slackclone.uichat.chatthread

import android.util.Log
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.getstream.chat.android.client.models.Attachment
import io.getstream.chat.android.client.models.Message
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.compose.state.messages.MessagesState
import io.getstream.chat.android.compose.state.messages.list.MessageItemState
import io.getstream.slackclone.uichannels.SessionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.openapitools.client.apis.ChatApi
import org.openapitools.client.apis.SessionApi
import org.openapitools.client.infrastructure.ClientException
import org.openapitools.client.infrastructure.ServerException
import org.openapitools.client.models.ChatBody
import org.openapitools.client.models.ChatHistory
import org.openapitools.client.models.SessionBody
import java.util.Date
import java.util.UUID

class ChatDesignerChatVM : ViewModel(){
  val accessToken: kotlin.String = "eyJraWQiOiJJd0dXTzdaRU5GVEZYS3ZLUUJIRUkyN3Z6d1JGOGNMeXFtVnR3dzVTZ0FRPSIsImFsZyI6IlJTMjU2In0.eyJzdWIiOiI4OTQ0MzIzNS04OTliLTQ5NDAtYTM2Ny03OWIwZjk1Mjg2ZTciLCJjb2duaXRvOmdyb3VwcyI6WyJ1cy13ZXN0LTJfZHgwdEhkYmF2X0dvb2dsZSJdLCJpc3MiOiJodHRwczpcL1wvY29nbml0by1pZHAudXMtd2VzdC0yLmFtYXpvbmF3cy5jb21cL3VzLXdlc3QtMl9keDB0SGRiYXYiLCJ2ZXJzaW9uIjoyLCJjbGllbnRfaWQiOiIzYW5ldTdlMm9tbXRpYzFuMGIyaWJhN2hsdSIsIm9yaWdpbl9qdGkiOiI3N2IxNTc3MC02MWUzLTQ2MTMtODQ5MS1mZTU3M2U0MjhlNTAiLCJ0b2tlbl91c2UiOiJhY2Nlc3MiLCJzY29wZSI6ImF3cy5jb2duaXRvLnNpZ25pbi51c2VyLmFkbWluIG9wZW5pZCBwcm9maWxlIGVtYWlsIiwiYXV0aF90aW1lIjoxNjk5ODk3ODY1LCJleHAiOjE2OTk5MDE0NjUsImlhdCI6MTY5OTg5Nzg2NSwianRpIjoiNDAyNjE5ZWItM2YxOS00NDI4LThhZGUtYzg5MWQxZWEzZjA4IiwidXNlcm5hbWUiOiJnb29nbGVfMTE0MjM2NDY3MDg4NzkxNzAyODU3In0.ZMFfDkivFbcCOeqCdo5UuBrSHgcBzohPHDl3YLfyHXRLFK-Cs6pSebX8UAz-9sdmj1Tkq9RbnAgVPrYN6cC5qDDnoKFQkry3H277P4BQl2WjKrfK_26GGMNnddyE92339aKo3Ve6zTngCuRQGxNS1UZlPhurxe2sopglGjqzadgf3YzUxCILhTAGox8U5Zt0aocpaCNbzATDaWK7jklNd8EHyc42UvKGracK_6WzYi_9ZMnyTouVwvTJo951tAzoepd168aclTH-BrfpEWfhNreGI8DRNGPCkUqw59hS8bl60DSbZpzWvOrW_1ufLq7CuK_maBCWn13y3uqKaZUcDQ" // kotlin.String |
  val userId = "89443235-899b-4940-a367-79b0f95286e7"

  private val _sessionId = MutableStateFlow("initialValue")
  var sessionId: String
    get() = _sessionId.value
    set(value) {
      _sessionId.value = value
    }

  private val _messagesState = MutableStateFlow<MessagesState>(
    MessagesState(
      isLoading = false, // Indicate that data loading is in progress
      messageItems = emptyList(), // No messages initially
      // Set other fields to their default or initial values
    )
  )

  public val messagesState: StateFlow<MessagesState> = _messagesState

  /**
   * calls createSession and then createChat
   */
  fun createSession(text: String) {
    val sessionBody = SessionBody(
      imageSize = SessionBody.ImageSize._512x512, // Explicitly setting the image size
      sessionName = null // Setting the session name to null
    )
    val chatBody = ChatBody(txt = text)
    viewModelScope.launch(Dispatchers.IO) {
      try {
        // create session
        val newSessionId = SessionRepository().createSession(sessionBody, accessToken).getOrThrow() //apiInstanceSession.createSession(sessionBody, accessToken)
        // create chat
        val result: ChatHistory = ChatRepository().startChat(
          sessionId = newSessionId,
          chatBody = chatBody,
          accessToken = accessToken
        ).getOrThrow()
        Log.d("createChat", "result: $result")
      } catch (e: ClientException) {
        println("4xx response calling SessionApi#createSession")
        e.printStackTrace()
      } catch (e: ServerException) {
        println("5xx response calling SessionApi#createSession")
        e.printStackTrace()
      }
    }
  }

  /**
   * getHistory to show message lists
   */
  suspend fun getChatHistory() : List<ChatContent> {
    try {
      val result: ChatHistory = ChatRepository().pullChatHistory(sessionId = sessionId, accessToken = accessToken).getOrThrow()
      val chatContents = parseChatHistory(result.chatHistory)
      return chatContents

    } catch (e: ClientException) {
      println("4xx response calling SessionApi#createSession")
      e.printStackTrace()
    } catch (e: ServerException) {
      println("5xx response calling SessionApi#createSession")
      e.printStackTrace()
    }
    return emptyList()
  }

  fun parseChatHistory(chatHistory: List<Any>?): List<ChatContent> {
    return chatHistory?.mapNotNull { item ->
      try {
        // Assuming `item` can be cast to a Map representing the JSON structure
        val itemMap = item as? Map<String, Any> ?: return@mapNotNull null

        // Extracting fields from itemMap to create a ChatMessage
        val role = itemMap["role"] as? String ?: return@mapNotNull null
//                val content = Gson().toJsonTree(itemMap["content"])
        val text = itemMap["content"] as? String
        val images = parseImages(itemMap["content"] as? List<Map<String, Any>>)
        val requestTime = itemMap["request_time"] as? Long ?
        val requestId = itemMap["request_id"] as? String
        val isFirstPage = itemMap["is_first_page"] as? Boolean
        val isRegenerable = itemMap["is_regenerable"] as? Int
        val numOfImages = itemMap["num_of_images"] as? Int

        ChatContent(role, null, text, images, requestTime, requestId, isFirstPage, isRegenerable, numOfImages)
      } catch (e: Exception) {
        null // or handle the exception as needed
      }
    } ?: emptyList()
  }

  fun parseImages(imagesList: List<Map<String, Any>>?): List<ImageContent> {
    return imagesList?.mapNotNull { imageMap ->
      val imageId = imageMap["image_id"] as? String
      val imageUrl = imageMap["image_url"] as? String
      if (imageId != null && imageUrl != null) {
        ImageContent(imageId, imageUrl)
      } else {
        null
      }
    } ?: emptyList()
  }

  init {
    observeSessionIdChanges()
  }

  private fun observeSessionIdChanges() {
    viewModelScope.launch {
      _sessionId.collect { sessionId ->
        fetchAndProcessChatContents()
      }
    }
  }
  fun fetchAndProcessChatContents() {
    viewModelScope.launch {
      val chatContents = getChatHistory()
      val newMessagesState = createMessagesStateFromChatContents(chatContents)
      _messagesState.value = newMessagesState
    }
  }
  fun createMessagesStateFromChatContents(
    chatContents: List<ChatContent>,
   ): MessagesState {
    // Convert the list of ChatContent to a list of MessageItemState
    val messageItemStates = convertChatContentsToMessageItemStates(chatContents)

    // Create MessagesState using the list of MessageItemState
    return MessagesState(
      isLoading = false, // Set to false assuming the loading is complete
      isLoadingMore = false, // Assuming no more items are being loaded
      endOfMessages = chatContents.isEmpty(), // If chatContents is empty, we may have reached the end
      messageItems = messageItemStates,
      // Other properties can be set based on your application's logic or left as default
    )
  }

  fun convertChatContentsToMessageItemStates(chatContents: List<ChatContent>): List<MessageItemState> {
    return chatContents.map { chatContent ->
      // First, convert ChatContent to Message
      val message = convertChatContentToMessage(chatContent)

      // Then, convert Message to MessageItemState
      convertMessageToMessageItemState(message)
    }
  }

  fun convertChatContentToMessage(chatContent: ChatContent): Message {
    val userId = when (chatContent.role) {
      "user" -> "0"
      "assistant" -> "1"
      else -> "" // Default or error case
    }

    return Message(
      id = chatContent.request_id ?: "", // Assuming request_id can serve as the message id
      text = chatContent.text ?: "default",
      createdAt = chatContent.request_time?.let { Date(it) }, // Converting request_time to Date
      user = User(id = userId),
      // Attachments need to be handled based on how you want to map ImageContent to Attachment
      attachments = chatContent.images?.map { convertImageContentToAttachment(it) }?.toMutableList() ?: mutableListOf(),
      // Other fields are set to their default values or based on assumptions
      // ...
    )
  }

  fun convertImageContentToAttachment(imageContent: ImageContent): Attachment {
    // Assuming you have an Attachment class that can take ImageContent fields
    // This is a placeholder implementation
    return Attachment(
      imageUrl = imageContent.image_url,
      // other fields
    )
  }

  fun convertMessageToMessageItemState(message: Message): MessageItemState {
    return MessageItemState(
      message = message,
      isMine = true, // Example logic to determine if the message is 'mine'
      // Other fields are set to their default values or based on your logic
      // For example, you might want to determine 'isMessageRead', 'shouldShowFooter', etc., based on your application's state
    )
  }


}