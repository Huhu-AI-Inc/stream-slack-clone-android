package ai.huhu.chatdesigner.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.openapitools.client.apis.SessionApi
import org.openapitools.client.apis.SessionsApi
import org.openapitools.client.models.Session
import org.openapitools.client.models.SessionBody

class SessionRepository {

  suspend fun createSession(sessionBody: SessionBody, accessToken: String): Result<String> =
    withContext(Dispatchers.IO) {
      try {
        val data = SessionApi().createSession(sessionBody, accessToken)
        Result.success(data)
      } catch (e: Exception) {
        Result.failure(e)
      }
    }

  suspend fun deleteSession(sessionId: String, accessToken: String): Result<Session> =
    withContext(Dispatchers.IO) {
      try {
        val data = SessionApi().deleteSession(sessionId, accessToken)
        Result.success(data)
      } catch (e: Exception) {
        Result.failure(e)
      }
    }

  suspend fun updateSession(sessionId: String, sessionBody: SessionBody, accessToken: String): Result<Session> =
    withContext(Dispatchers.IO) {
      try {
        val data = SessionApi().updateSession(sessionId, sessionBody, accessToken)
        Result.success(data)
      } catch (e: Exception) {
        Result.failure(e)
      }
    }

  suspend fun getSessions(startPageId: String? = null, endPageId: String? = null, accessToken: String): Result<Session> =
    withContext(Dispatchers.IO) {
      try {
        val data = SessionsApi().getSessions(startPageId, endPageId, accessToken)
        Result.success(data)
      } catch (e: Exception) {
        Result.failure(e)
      }
    }
}
