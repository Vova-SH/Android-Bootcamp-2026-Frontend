package com.example.meet

import com.example.meet.data.dto.InvitationDto
import com.example.meet.data.dto.InvitationResponseStatus
import org.junit.Assert.assertEquals
import org.junit.Test

class InvitationGroupingTest {
    private fun group(invites: List<InvitationDto>): Triple<Int, Int, Int> {
        val pending = invites.filter { it.responseStatus.equals(InvitationResponseStatus.PENDING, true) }
        val accepted = invites.filter { it.responseStatus.equals(InvitationResponseStatus.ACCEPTED, true) }
        val declined = invites.filter { it.responseStatus.equals(InvitationResponseStatus.DECLINED, true) }
        return Triple(pending.size, accepted.size, declined.size)
    }

    @Test
    fun grouping_counts_match_statuses() {
        val items = listOf(
            InvitationDto(id = 1, meetingId = 10, userId = 100, responseStatus = "PENDING"),
            InvitationDto(id = 2, meetingId = 11, userId = 100, responseStatus = "ACCEPTED"),
            InvitationDto(id = 3, meetingId = 12, userId = 100, responseStatus = "DECLINED"),
            InvitationDto(id = 4, meetingId = 13, userId = 100, responseStatus = "pending"),
            InvitationDto(id = 5, meetingId = 14, userId = 100, responseStatus = "ACCEPTED")
        )
        val (pendingCount, acceptedCount, declinedCount) = group(items)
        assertEquals(2, pendingCount)
        assertEquals(2, acceptedCount)
        assertEquals(1, declinedCount)
    }
}
