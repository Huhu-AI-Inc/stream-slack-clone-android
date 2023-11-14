package io.getstream.slackclone.uichat.chatthread

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.openapitools.client.apis.ChatApi
import org.openapitools.client.apis.SessionApi
import org.openapitools.client.infrastructure.ClientException
import org.openapitools.client.infrastructure.ServerException
import org.openapitools.client.models.ChatBody
import org.openapitools.client.models.ChatHistory
import org.openapitools.client.models.SessionBody
import java.util.UUID

class ChatDesignerChatVM : ViewModel(){
  val apiInstanceSession = SessionApi()
  val apiInstanceChat = ChatApi()
  val accessToken: kotlin.String = "eyJraWQiOiJJd0dXTzdaRU5GVEZYS3ZLUUJIRUkyN3Z6d1JGOGNMeXFtVnR3dzVTZ0FRPSIsImFsZyI6IlJTMjU2In0.eyJzdWIiOiI4OTQ0MzIzNS04OTliLTQ5NDAtYTM2Ny03OWIwZjk1Mjg2ZTciLCJjb2duaXRvOmdyb3VwcyI6WyJ1cy13ZXN0LTJfZHgwdEhkYmF2X0dvb2dsZSJdLCJpc3MiOiJodHRwczpcL1wvY29nbml0by1pZHAudXMtd2VzdC0yLmFtYXpvbmF3cy5jb21cL3VzLXdlc3QtMl9keDB0SGRiYXYiLCJ2ZXJzaW9uIjoyLCJjbGllbnRfaWQiOiIzYW5ldTdlMm9tbXRpYzFuMGIyaWJhN2hsdSIsIm9yaWdpbl9qdGkiOiI3N2IxNTc3MC02MWUzLTQ2MTMtODQ5MS1mZTU3M2U0MjhlNTAiLCJ0b2tlbl91c2UiOiJhY2Nlc3MiLCJzY29wZSI6ImF3cy5jb2duaXRvLnNpZ25pbi51c2VyLmFkbWluIG9wZW5pZCBwcm9maWxlIGVtYWlsIiwiYXV0aF90aW1lIjoxNjk5ODk3ODY1LCJleHAiOjE2OTk5MDE0NjUsImlhdCI6MTY5OTg5Nzg2NSwianRpIjoiNDAyNjE5ZWItM2YxOS00NDI4LThhZGUtYzg5MWQxZWEzZjA4IiwidXNlcm5hbWUiOiJnb29nbGVfMTE0MjM2NDY3MDg4NzkxNzAyODU3In0.ZMFfDkivFbcCOeqCdo5UuBrSHgcBzohPHDl3YLfyHXRLFK-Cs6pSebX8UAz-9sdmj1Tkq9RbnAgVPrYN6cC5qDDnoKFQkry3H277P4BQl2WjKrfK_26GGMNnddyE92339aKo3Ve6zTngCuRQGxNS1UZlPhurxe2sopglGjqzadgf3YzUxCILhTAGox8U5Zt0aocpaCNbzATDaWK7jklNd8EHyc42UvKGracK_6WzYi_9ZMnyTouVwvTJo951tAzoepd168aclTH-BrfpEWfhNreGI8DRNGPCkUqw59hS8bl60DSbZpzWvOrW_1ufLq7CuK_maBCWn13y3uqKaZUcDQ" // kotlin.String |
  val userId = "89443235-899b-4940-a367-79b0f95286e7"
  var sessionId : kotlin.String = "initialValue"

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
        sessionId = apiInstanceSession.createSession(sessionBody, accessToken)
        val requestId: String = UUID.randomUUID().toString().lowercase()
        // create chat
        val result: ChatHistory = apiInstanceChat.createChat(
          sessionId = sessionId,
          requestId = requestId,
          chatBody = chatBody,
          accessToken = accessToken
        )
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

  fun createChat() {

  }

}