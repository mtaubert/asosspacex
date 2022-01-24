package com.example.asosspacex.ui.viewmodels.listitems

import com.example.asosspacex.R
import com.example.asosspacex.db.entities.LaunchEntity
import com.example.asosspacex.db.entities.RocketEntity
import com.example.asosspacex.providers.CalendarProvider
import com.example.asosspacex.providers.ResourcesProvider
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import org.greenrobot.eventbus.EventBus
import org.junit.Before
import org.junit.Test

class LaunchListItemViewModelTest {

    private val resourcesProvider: ResourcesProvider = mockk(relaxed = true)
    private val eventBus: EventBus = mockk(relaxed = true)
    private val calendarProvider: CalendarProvider = mockk()
    private val launchEntity: LaunchEntity = mockk()
    private val rocketEntity: RocketEntity = mockk()
    private val launchYear: Int = 2022


    @Before
    fun setup() {
        every { calendarProvider.timeInMillis() } returns 1642899600000L //23-01-2022 at 1:00:00
        every { resourcesProvider.getString(R.string.launch_mission) } returns "Mission: %s"
        every { resourcesProvider.getString(R.string.launch_date_time) } returns "Date/Time: %s at %s"
        every { resourcesProvider.getString(R.string.launch_rocket) } returns "Rocket: %s/%s"
        every { resourcesProvider.getString(R.string.launch_rocket_error) } returns "Could not get rocket information"
        every { resourcesProvider.getString(R.string.days_until) } returns "Days until: %d"
        every { resourcesProvider.getString(R.string.days_since) } returns "Days since: %d"

        every { launchEntity.patchLink } returns "patchLink"
        every { launchEntity.wikipediaLink } returns "wikipediaLink"
        every { launchEntity.articleLink } returns "articleLink"
        every { launchEntity.webcastLink } returns "webcastLink"
        every { launchEntity.name } returns "name"
        every { launchEntity.date_unix } returns 1642726800L //21-01-2022 at 1:00:00
        every { launchEntity.upcoming } returns false
        every { launchEntity.rocketId } returns "rocketId"
        every { launchEntity.success } returns true

        every { rocketEntity.name } returns "rocketName"
        every { rocketEntity.type } returns "rocketType"

    }

    @Test
    fun onInit_withAllData_textSetCorrectly() {
        val subject = LaunchListItemViewModel(
            resourcesProvider,
            eventBus,
            calendarProvider,
            launchEntity,
            rocketEntity,
            launchYear
        )

        assertEquals("Mission: name", subject.missionNameText.get())
        assertEquals("Date/Time: 21-01-2022 at 01:00:00", subject.dateTimeText.get())
        assertEquals("Rocket: rocketName/rocketType", subject.rocketInfoText.get())
        assertEquals("Days since: 2", subject.daysText.get())
        assertEquals(true, subject.successfulLaunch.get())
        assertEquals(false, subject.upcomingLaunch.get())
        assertEquals("patchLink", subject.missionPatchURL.get())
    }

    @Test
    fun onInit_withAllData_launchInFuture_textSetCorrectly() {
        every { calendarProvider.timeInMillis() } returns 1642899600000L //23-01-2022 at 1:00:00
        every { launchEntity.date_unix } returns 1643072400L //25-01-2022 at 1:00:00
        every { launchEntity.upcoming } returns true

        val subject = LaunchListItemViewModel(
            resourcesProvider,
            eventBus,
            calendarProvider,
            launchEntity,
            rocketEntity,
            launchYear
        )

        assertEquals("Mission: name", subject.missionNameText.get())
        assertEquals("Date/Time: 25-01-2022 at 01:00:00", subject.dateTimeText.get())
        assertEquals("Rocket: rocketName/rocketType", subject.rocketInfoText.get())
        assertEquals("Days until: 2", subject.daysText.get())
        assertEquals(true, subject.successfulLaunch.get())
        assertEquals(true, subject.upcomingLaunch.get())
        assertEquals("patchLink", subject.missionPatchURL.get())
    }

    @Test
    fun onInit_missingRocketData_textSetCorrectly() {
        val subject = LaunchListItemViewModel(
            resourcesProvider,
            eventBus,
            calendarProvider,
            launchEntity,
            null,
            launchYear
        )

        assertEquals("Mission: name", subject.missionNameText.get())
        assertEquals("Date/Time: 21-01-2022 at 01:00:00", subject.dateTimeText.get())
        assertEquals("Could not get rocket information", subject.rocketInfoText.get())
        assertEquals("Days since: 2", subject.daysText.get())
        assertEquals(true, subject.successfulLaunch.get())
        assertEquals(false, subject.upcomingLaunch.get())
        assertEquals("patchLink", subject.missionPatchURL.get())
    }
}