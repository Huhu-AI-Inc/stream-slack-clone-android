package io.getstream.slackclone.uichannels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import io.getstream.slackclone.chatcore.data.UiLayerChannels
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.openapitools.client.infrastructure.ClientException
import org.openapitools.client.infrastructure.ServerException
import org.openapitools.client.models.Session

class SessionsViewModel : ViewModel() {
  // This will hold the list of session details
  private val _sessionDetails = mutableStateOf<List<UiLayerChannels.ChatDesignerSession>>(emptyList()) // Replace 'Any' with your session detail type
  val sessionDetails: State<List<UiLayerChannels.ChatDesignerSession>> = _sessionDetails

  // This will hold the userId from the session object
  private val _userId = mutableStateOf<String?>(null)
  val userId: State<String?> = _userId

  private val _errorMessage = MutableStateFlow<String?>(null)
  val errorMessage: StateFlow<String?> = _errorMessage


  init {
    fetchSessions()
  }

  // Call this function to fetch the session object which contains the userId and a list of session details
  fun fetchSessions() {
//    val apiInstance = SessionsApi()
    val accessToken: kotlin.String = "sample"

    CoroutineScope(Dispatchers.IO).launch {
      try {
        // Assuming getSessions is a suspend function that returns a Session object
        val sessionObject = SessionRepository().getSessions(accessToken = accessToken).getOrThrow()
        // Assuming sessionObject.sessions is a List of Maps, and you need to convert it to a List of SlackSession
        val sessionItems: List<UiLayerChannels.ChatDesignerSession> = transformToChatDesignerSessions(sessionObject)

        // Now assign the mapped list to the state variable
        _sessionDetails.value = sessionItems
        // Assuming sessionObject.userId is a String
        _userId.value = sessionObject.userId
      } catch (e: ClientException) {
        withContext(Dispatchers.Main) {
          // Now it's safe to interact with the UI
          Log.e("SessionViewModel", "4xx response calling SessionsApi#getSessions", e)
        }
      } catch (e: ServerException) {
        // Post an error message for the UI to observe
        withContext(Dispatchers.Main) {
          // Now it's safe to interact with the UI
          Log.e("SessionViewModel", "5xx response calling SessionsApi#getSessions", e)
        }
      } catch (e: Exception) {
        // Post an error message for the UI to observe
        withContext(Dispatchers.Main) {
          // Now it's safe to interact with the UI
          Log.e("SessionViewModel", "An error occurred: ${e.localizedMessage}", e)
        }
      }
    }
  }

  fun transformToChatDesignerSessions(sessionObject: Session): List<UiLayerChannels.ChatDesignerSession> {
    return sessionObject.sessions.map { session ->
      // Cast the item to a Map and extract the properties
      val sessionMap = session as Map<String, Any>
      UiLayerChannels.ChatDesignerSession(
        sessionId = sessionMap["session_id"] as String,
        sessionName = sessionMap["session_name"] as String,
        createTime = (sessionMap["create_time"] as Number).toLong(),
        lastModified = (sessionMap["last_modified"] as Number).toLong(),
        imgSize = sessionMap["img_size"] as String,
        imgUrl = sessionMap["img_url"] as String
      )
    }
  }

}
