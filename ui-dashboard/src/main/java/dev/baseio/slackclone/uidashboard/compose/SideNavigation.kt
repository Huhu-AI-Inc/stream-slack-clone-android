package dev.baseio.slackclone.uidashboard.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.google.accompanist.insets.statusBarsPadding
import dev.baseio.slackclone.commonui.material.SlackSurfaceAppBar
import dev.baseio.slackclone.uidashboard.R
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.navigationBarsPadding
import dev.baseio.slackclone.commonui.theme.*

@Composable
fun SideNavigation() {
  SlackCloneSurface(color = SlackCloneColor, modifier = Modifier.fillMaxSize()) {
    Column(
      modifier = Modifier
        .statusBarsPadding()
        .fillMaxHeight(), verticalArrangement = Arrangement.SpaceBetween
    ) {
      Column {
        Workspaces()
        Workspace()
      }
      Spacer(modifier = Modifier.padding(8.dp))
      SideNavFooter()
    }

  }
}

@Composable
private fun SideNavFooter() {
  Column(modifier = Modifier.navigationBarsPadding()) {
    Divider(color = Color.White.copy(alpha = 0.2f))
    FooterItem(Icons.Filled.AddCircle, stringResource(id = R.string.add_workspace))
    FooterItem(Icons.Filled.Settings, stringResource(id = R.string.preferences))
    FooterItem(Icons.Filled.CheckCircle, stringResource(id = R.string.help))
  }
}

@Composable
fun FooterItem(imageVector: ImageVector, title: String) {
  Row(modifier = Modifier.padding(8.dp)) {
    Icon(
      imageVector,
      contentDescription = null,
      tint = Color.White,
      modifier = Modifier.padding(8.dp)
    )
    Text(
      text = title,
      style = SlackCloneTypography.subtitle1.copy(color = Color.White),
      modifier = Modifier.padding(8.dp)
    )
  }
}

@Composable
fun Workspace() {
  Row(modifier = Modifier.padding(8.dp)) {
    OrganizationLogo()
    OrganizationDetails()
  }
}

@Composable
fun OrganizationDetails() {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(start = 8.dp),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.Start
  ) {
    Text(text = "mutualmobile", style = SlackCloneTypography.h6.copy(color = Color.White))
    Text(
      "mutualmobile.slack.com",
      style = SlackCloneTypography.subtitle1.copy(
        fontWeight = FontWeight.SemiBold,
        color = Color.White.copy(alpha = 0.4f)
      )
    )
  }
}

@Composable
fun OrganizationLogo() {
  MMImage(Modifier.size(64.dp))
}

@Composable
private fun Workspaces() {
  SlackSurfaceAppBar(title = {
    Text(
      text = stringResource(id = R.string.head_workspaces),
      style = SlackCloneTypography.h5.copy(
        color = Color.White,
        fontFamily = slackFontFamily,
        fontWeight = FontWeight.Bold
      )
    )
  }, backgroundColor = SlackCloneColor)
}