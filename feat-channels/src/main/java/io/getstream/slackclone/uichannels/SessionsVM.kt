package io.getstream.slackclone.uichannels

import ai.huhu.chatdesigner.repository.SessionRepository
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
import org.openapitools.client.apis.SessionsApi
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
    val accessToken: kotlin.String = "eyJraWQiOiJJd0dXTzdaRU5GVEZYS3ZLUUJIRUkyN3Z6d1JGOGNMeXFtVnR3dzVTZ0FRPSIsImFsZyI6IlJTMjU2In0.eyJzdWIiOiI4OTQ0MzIzNS04OTliLTQ5NDAtYTM2Ny03OWIwZjk1Mjg2ZTciLCJjb2duaXRvOmdyb3VwcyI6WyJ1cy13ZXN0LTJfZHgwdEhkYmF2X0dvb2dsZSJdLCJpc3MiOiJodHRwczpcL1wvY29nbml0by1pZHAudXMtd2VzdC0yLmFtYXpvbmF3cy5jb21cL3VzLXdlc3QtMl9keDB0SGRiYXYiLCJ2ZXJzaW9uIjoyLCJjbGllbnRfaWQiOiIzYW5ldTdlMm9tbXRpYzFuMGIyaWJhN2hsdSIsIm9yaWdpbl9qdGkiOiI0NzliMTY4OS01ODA5LTRlYmEtOGQwMC02N2UzZGM5MGExY2UiLCJ0b2tlbl91c2UiOiJhY2Nlc3MiLCJzY29wZSI6ImF3cy5jb2duaXRvLnNpZ25pbi51c2VyLmFkbWluIG9wZW5pZCBwcm9maWxlIGVtYWlsIiwiYXV0aF90aW1lIjoxNjk5NTU1NDYzLCJleHAiOjE2OTk1ODQwNzYsImlhdCI6MTY5OTU4MDQ3NiwianRpIjoiOTNhMmIwMDAtMTA0NS00OThjLTk5MDYtMmIxZjIyZmE4OWIwIiwidXNlcm5hbWUiOiJnb29nbGVfMTE0MjM2NDY3MDg4NzkxNzAyODU3In0.nSsXnHf7joP02Qz2neQ2Dejtqsl-AfgKk1zLDyNFsp5Y-OoGgRsufAu70JaZRuAlXw5Pg5GEAzhbFxOmsxJE_1klvxbaN0jkHdKCmH_zyK3u15K_5QGXA2f6oeYFZ5s4ul_PojPxjKsWvWH9TISDRcEQEQiPFh81jvSNMa4aRHjNABfJhePkEQO6sZ2gP0vxkw83VQEfrEopvSJR8DOLjNsZ7ip2jHcX72M8XKxvvGqdpp3sD1J4nDZxGa6pKh6xYHjgWXNAvl8_75BzSJV-iiblJV15-LwU1ds8a-m_EWZzBFg5FAurYupZ9EEPgAp-tKHi-qD_48gR-MS8n-bc9g"

    CoroutineScope(Dispatchers.IO).launch {
      try {
        // Assuming getSessions is a suspend function that returns a Session object
        val sessionObject = SessionRepository().getSessions(accessToken = accessToken).getOrThrow()
        // Assuming sessionObject.sessions is a List of Maps, and you need to convert it to a List of SlackSession
        val sessionItems: List<UiLayerChannels.ChatDesignerSession> = sessionObject.sessions.map { session ->
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
}
