package io.getstream.slackclone.uichannels.views

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import io.getstream.slackclone.chatcore.data.ExpandCollapseModel
import io.getstream.slackclone.chatcore.data.UiLayerChannels
import io.getstream.slackclone.uichannels.R
import io.getstream.slackclone.uichannels.SessionViewModel
import io.getstream.slackclone.uichannels.SlackChannelVM
import org.openapitools.client.models.Session

@Composable
fun SlackRecentChannels(
  onItemClick: (UiLayerChannels.SlackChannel) -> Unit = {},
  channelVM: SlackChannelVM = hiltViewModel(),
  onClickAdd: () -> Unit
) {
  val recent = stringResource(R.string.Recent)
  val channelsFlow = channelVM.channels.collectAsState()
  val channels by channelsFlow.value.collectAsState(initial = listOf())

  LaunchedEffect(key1 = Unit) {
    channelVM.allChannels()
  }

  var expandCollapseModel by remember {
    mutableStateOf(
      ExpandCollapseModel(
        1, recent,
        needsPlusButton = false,
        isOpen = true
      )
    )
  }
  SKExpandCollapseColumn(expandCollapseModel, onItemClick, {
    expandCollapseModel = expandCollapseModel.copy(isOpen = it)
  }, channels, onClickAdd)
}

@Composable
fun SlackRecentSessions(
  onItemClick: (UiLayerChannels.SlackSession) -> Unit = {},
  sessionVM: SessionViewModel = viewModel(),
  onClickAdd: () -> Unit
) {
  val recent = stringResource(R.string.Recent) // Replace with actual string resource ID for "Recent"
  val sessions = sessionVM.sessionDetails.value  // Directly access the value
  val userId = sessionVM.userId.value

  // LaunchedEffect to fetch sessions when the composable first enters the composition
  LaunchedEffect(key1 = Unit) {
    sessionVM.fetchSessions()
  }


  var expandCollapseModel by remember {
    mutableStateOf(
      ExpandCollapseModel(
        id = 1,
        title = recent,
        needsPlusButton = false,
        isOpen = true
      )
    )
  }

  // This column will show a list of session items
  SKExpandCollapseSessionColumn(expandCollapseModel, onItemClick, {
    expandCollapseModel = expandCollapseModel.copy(isOpen = it)
  }, sessions, onClickAdd)
}

