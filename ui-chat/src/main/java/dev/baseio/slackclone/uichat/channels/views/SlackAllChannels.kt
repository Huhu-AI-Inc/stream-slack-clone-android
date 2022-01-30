package dev.baseio.slackclone.uichat.channels.views

import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import dev.baseio.slackclone.domain.model.channel.SlackChannel
import dev.baseio.slackclone.uichat.channels.data.ExpandCollapseModel
import dev.baseio.slackclone.uichat.R
import dev.baseio.slackclone.uichat.channels.SlackChannelVM
import dev.baseio.slackclone.uichat.models.ChatPresentation

@Composable
fun SlackAllChannels(
  onItemClick: (ChatPresentation.SlackChannel) -> Unit = {},
  channelVM: SlackChannelVM = hiltViewModel()
) {
  val recent = stringResource(R.string.channels)
  val channels by channelVM.channels.collectAsState(initial = emptyList())
  var expandCollapseModel by remember {
    mutableStateOf(
      ExpandCollapseModel(
        1, recent,
        needsPlusButton = true,
        isOpen = false
      )
    )
  }
  SKExpandCollapseColumn(expandCollapseModel = expandCollapseModel, onItemClick = onItemClick, {
    expandCollapseModel = expandCollapseModel.copy(isOpen = it)
  },channels)
}