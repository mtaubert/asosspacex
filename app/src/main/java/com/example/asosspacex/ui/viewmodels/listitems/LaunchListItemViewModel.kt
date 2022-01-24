package com.example.asosspacex.ui.viewmodels.listitems

import android.view.View
import androidx.databinding.ObservableField
import com.example.asosspacex.R
import com.example.asosspacex.db.entities.LaunchEntity
import com.example.asosspacex.db.entities.RocketEntity
import com.example.asosspacex.events.ShowLinksEvent
import com.example.asosspacex.providers.CalendarProvider
import com.example.asosspacex.providers.ResourcesProvider
import org.greenrobot.eventbus.EventBus
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class LaunchListItemViewModel(
    private val resourcesProvider: ResourcesProvider,
    private val eventBus: EventBus,
    private val calendarProvider: CalendarProvider,
    private val launchEntity: LaunchEntity,
    private val rocketEntity: RocketEntity?,
    val launchYear: Int
) {
    val missionNameText = ObservableField(resourcesProvider.getString(R.string.launch_mission).format(launchEntity.name))
    val dateTimeText = ObservableField("")
    val rocketInfoText = ObservableField("")
    val daysText = ObservableField("")
    val successfulLaunch = ObservableField(launchEntity.success)
    val upcomingLaunch = ObservableField(launchEntity.upcoming)
    val missionPatchURL = ObservableField(launchEntity.patchLink)

    init {
        setRocketInfoText()
        setDateTimeText()
        setDaysText()
    }

    private fun setRocketInfoText() {
        if(rocketEntity == null) {
            rocketInfoText.set(resourcesProvider.getString(R.string.launch_rocket_error))
        } else {
            rocketInfoText.set(resourcesProvider.getString(R.string.launch_rocket).format(rocketEntity.name, rocketEntity.type))
        }
    }

    private fun setDateTimeText() {
        val date = Date(launchEntity.date_unix * 1000L)
        val dateString = SimpleDateFormat("dd-MM-yyyy").format(date)
        val timeString = SimpleDateFormat("HH:mm:ss").format(date)
        dateTimeText.set(resourcesProvider.getString(R.string.launch_date_time).format(dateString, timeString))
    }

    private fun setDaysText() {
        val currentUnixTime = calendarProvider.timeInMillis()
        if(launchEntity.upcoming) {
            val daysCounter = TimeUnit.DAYS.convert(launchEntity.date_unix * 1000L - currentUnixTime, TimeUnit.MILLISECONDS)
            daysText.set(resourcesProvider.getString(R.string.days_until).format(daysCounter))
        } else {
            val daysCounter = TimeUnit.DAYS.convert(currentUnixTime - launchEntity.date_unix * 1000L, TimeUnit.MILLISECONDS)
            daysText.set(resourcesProvider.getString(R.string.days_since).format(daysCounter))
        }
    }

    fun showLinks(v: View) {
        val linksListItems = arrayListOf<LinkListItemViewModel>()
        if(launchEntity.wikipediaLink != null) linksListItems.add(LinkListItemViewModel(eventBus, resourcesProvider.getString(R.string.link_wikipedia), launchEntity.wikipediaLink))
        if(launchEntity.articleLink != null) linksListItems.add(LinkListItemViewModel(eventBus, resourcesProvider.getString(R.string.link_article), launchEntity.articleLink))
        if(launchEntity.webcastLink != null) linksListItems.add(LinkListItemViewModel(eventBus, resourcesProvider.getString(R.string.link_youtube), launchEntity.webcastLink))

        eventBus.post(ShowLinksEvent(linksListItems))
    }

    fun wasSuccessful(): Boolean = launchEntity.success

    fun getLaunchTime(): Long = launchEntity.date_unix

    fun getLaunchId(): String = launchEntity.id
}