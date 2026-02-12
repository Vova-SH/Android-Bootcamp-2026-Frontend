package com.example.planify.main.navigation.screens.inbox_screen.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import coil3.target.ViewTarget
import com.example.planify.main.common.themes.Locals
import com.example.planify.main.features.meetings.domain.entities.Meeting
import com.example.planify.main.features.meetings.domain.entities.MeetingContext
import com.example.planify.main.features.meetings.domain.entities.MeetingInvite
import com.example.planify.main.features.meetings.domain.entities.MeetingInviteStatus
import com.example.planify.main.features.profiles.domain.entities.Profile
import com.example.planify.main.navigation.screens.inbox_screen.InboxViewModel
import com.example.planify.main.navigation.screens.inbox_screen.ui.components.MeetingInviteInboxCardShort
import java.time.LocalDateTime

private val fakeMeetingContext = MeetingContext(
    participantProfiles = listOf(
        Profile(
            userId = 101L,
            firstName = "Камилла",
            lastName = "Ахметова",
            position = "Project Manager",
            department = "Product",
            profileImageUrl = "https://cs13.pikabu.ru/post_img/big/2019/02/08/4/1549604155132231693.jpg"
        ),
        Profile(
            userId = 102L,
            firstName = "Дмитрий",
            lastName = "Козлов",
            position = "Senior Android Developer",
            department = "Mobile",
            profileImageUrl = "https://avatars.mds.yandex.net/get-shedevrum/9283310/acd3cc60b3a411ee8422feda328ae3aa/orig"

        ),
        Profile(
            userId = 103L,
            firstName = "Олег",
            lastName = "Смирнов",
            position = "Backend Developer",
            department = "Platform",
            profileImageUrl = "https://lh6.googleusercontent.com/proxy/udvC3WoJ1QQyTauRYMnaAKKMg80Ka__Qk9AACBTizR_pBlfZKa3xuwqd97mfvy5zKcxbSA"
        ),
        Profile(
            userId = 104L,
            firstName = "Алина",
            lastName = "Петрова",
            position = "UX/UI Designer",
            department = "Design",
            profileImageUrl = "https://cs13.pikabu.ru/post_img/big/2019/02/08/4/1549604155132231693.jpg"
        )
    ),
    invites = listOf(
        MeetingInvite(
            uuid = "7f3c9e6a-2d1b-4f6a-9c1a-12b6d9a3f0a1",
            meetingId = 5001L,
            senderId = 101L,
            targetId = 102L,
            status = MeetingInviteStatus.PENDING,
            createdAt = LocalDateTime.now().minusDays(1).withNano(0),
            updatedAt = LocalDateTime.now().minusHours(3).withNano(0)
        ),
        MeetingInvite(
            uuid = "a1b2c3d4-e5f6-47a8-9b0c-1d2e3f4a5b6c",
            meetingId = 5001L,
            senderId = 101L,
            targetId = 103L,
            status = MeetingInviteStatus.ACCEPTED,
            createdAt = LocalDateTime.now().minusDays(2).withNano(0),
            updatedAt = LocalDateTime.now().minusDays(1).withNano(0)
        ),
        MeetingInvite(
            uuid = "b9b0a1c2-d3e4-4f55-8a66-7b8c9d0e1f2a",
            meetingId = 5001L,
            senderId = 101L,
            targetId = 104L,
            status = MeetingInviteStatus.PENDING,
            createdAt = LocalDateTime.now().minusHours(10).withNano(0),
            updatedAt = LocalDateTime.now().minusHours(2).withNano(0)
        )
    ),
    meeting = Meeting(
        id = 5001L,
        ownerId = 101L,
        name = "Обсуждение UI/UX",
        description = "Презентация и обсуждение концепции, сроков и ролей команды.",
        location = "Главный зал",
        startsAt = LocalDateTime.now().plusDays(2).withHour(15).withMinute(0).withSecond(0).withNano(0),
        duration = 60
    ),
    invitedUserProfiles = emptyList()
)

@Composable
fun InboxViewSent(
    viewModel: InboxViewModel
) {
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(
                state = rememberScrollState()
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = Locals.dimens.inboxBoxTopNavBarHeight,
                    bottom = Locals.spacing.s
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Locals.spacing.m)
            ) {
                repeat(7) {
                    MeetingInviteInboxCardShort(
                        meetingInfo = fakeMeetingContext
                    )
                }
            }
        }
    }
}