package com.materiiapps.gloom.ui.screen.repo.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material.icons.outlined.ModeComment
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.materiiapps.gloom.Res
import com.materiiapps.gloom.gql.type.PullRequestReviewDecision
import com.materiiapps.gloom.gql.type.StatusState
import com.materiiapps.gloom.ui.component.Label
import com.materiiapps.gloom.ui.theme.gloomColorScheme
import com.materiiapps.gloom.util.TimeUtils.getTimeSince
import com.materiiapps.gloom.ui.util.parsedColor
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.datetime.Instant

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun IssueOrPRItem(
    createdAt: Instant,
    icon: ImageVector,
    color: Color,
    titleCDRes: StringResource,
    number: Int,
    title: String,
    authorUsername: String?,
    labels: List<Pair<String, String>>,
    totalAssigned: Int,
    assignedUsers: List<Pair<String, String>>,
    totalComments: Int,
    checksStatus: StatusState? = null,
    reviewDecision: PullRequestReviewDecision? = null
) {
    val rowCD = stringResource(titleCDRes)

    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .padding(
                horizontal = 16.dp,
                vertical = 16.dp
            )
            .semantics(
                mergeDescendants = true
            ) {
                this.contentDescription = rowCD
            }
    ) {
        Icon(
            imageVector = icon,
            tint = color,
            contentDescription = null
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = stringResource(
                    Res.strings.msg_issue_or_pr_author,
                    number,
                    authorUsername ?: "ghost"
                ),
                style = MaterialTheme.typography.labelLarge,
                color = LocalContentColor.current.copy(alpha = 0.5f)
            )


            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                modifier = Modifier.fillMaxWidth()
            ) {
                labels.forEach { (label, color) ->
                    val labelColor = color.parsedColor
                    Label(
                        text = label,
                        textColor = if (labelColor.luminance() > 0.5f) Color.Black else Color.White,
                        fillColor = labelColor,
                        borderColor = labelColor
                    )
                }

                if (labels.isNotEmpty()) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .height(23.dp)
                            .padding(horizontal = 3.dp)
                    ) {
                        VerticalDivider(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                            thickness = 1.dp,
                            modifier = Modifier.height(18.dp)
                        )
                    }
                }

                if (totalAssigned > 0) {
                    val additionalAssignees = totalAssigned - 1
                    val (_, avatarUrl) = assignedUsers.first()

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                    ) {
                        AsyncImage(
                            model = avatarUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .size(23.dp)
                                .clip(CircleShape)
                        )
                        if (additionalAssignees > 0) {
                            Text(
                                text = "+ $additionalAssignees",
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.padding(top = 5.dp, bottom = 5.dp, end = 7.dp)
                            )
                        }
                    }
                }

                if (checksStatus != null) {
                    val (statusIcon, statusColor, statusLabelRes) = when (checksStatus) {
                        StatusState.EXPECTED -> Triple(
                            Icons.Outlined.Circle,
                            MaterialTheme.colorScheme.surfaceTint,
                            Res.strings.label_checks
                        )

                        StatusState.PENDING -> Triple(
                            Icons.Filled.Circle,
                            MaterialTheme.gloomColorScheme.statusYellow,
                            Res.strings.label_checks
                        )

                        StatusState.SUCCESS -> Triple(
                            Icons.Filled.CheckCircle,
                            MaterialTheme.gloomColorScheme.statusGreen,
                            Res.strings.label_checks
                        )

                        else -> Triple(
                            Icons.Filled.Cancel,
                            MaterialTheme.colorScheme.error,
                            Res.strings.label_checks_failed
                        )
                    }

                    Label(
                        text = stringResource(statusLabelRes),
                        icon = statusIcon,
                        textColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        fillColor = MaterialTheme.colorScheme.secondaryContainer,
                        borderColor = MaterialTheme.colorScheme.secondaryContainer,
                        iconColor = statusColor
                    )
                }

                if (reviewDecision != null) {
                    val (statusIcon, statusColor) = when (reviewDecision) {
                        PullRequestReviewDecision.CHANGES_REQUESTED -> Icons.Filled.Cancel to MaterialTheme.colorScheme.error
                        PullRequestReviewDecision.APPROVED -> Icons.Filled.CheckCircle to MaterialTheme.gloomColorScheme.statusGreen
                        else -> Icons.Filled.Circle to MaterialTheme.colorScheme.surfaceTint
                    }

                    Label(
                        text = stringResource(Res.strings.label_reviews),
                        icon = statusIcon,
                        textColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        fillColor = MaterialTheme.colorScheme.secondaryContainer,
                        borderColor = MaterialTheme.colorScheme.secondaryContainer,
                        iconColor = statusColor
                    )
                }

                val labelCD = stringResource(Res.strings.cd_label_comments, totalComments)
                Label(
                    text = totalComments.toString(),
                    icon = Icons.Outlined.ModeComment,
                    textColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    fillColor = MaterialTheme.colorScheme.secondaryContainer,
                    borderColor = MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier.semantics(mergeDescendants = true) {
                        contentDescription = labelCD
                    }
                )
            }
        }

        Text(
            text = getTimeSince(createdAt),
            style = MaterialTheme.typography.labelLarge,
            color = LocalContentColor.current.copy(alpha = 0.5f)
        )
    }
}