package com.example.asosspacex.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.asosspacex.ui.adapters.LaunchesAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.databinding.ObservableField
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.asosspacex.R
import com.example.asosspacex.db.entities.LaunchEntity
import com.example.asosspacex.db.entities.RocketEntity
import com.example.asosspacex.events.ToastEvent
import com.example.asosspacex.events.UpdateFiltersYearEvent
import com.example.asosspacex.models.Filters
import com.example.asosspacex.networking.repositories.LaunchesRepository
import com.example.asosspacex.networking.repositories.RocketsRepository
import com.example.asosspacex.providers.BaseSchedulersProvider
import com.example.asosspacex.providers.CalendarProvider
import com.example.asosspacex.providers.ResourcesProvider
import com.example.asosspacex.ui.viewmodels.listitems.LaunchListItemViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import java.time.Instant
import java.time.ZoneOffset

@HiltViewModel
class LaunchesListViewModel @Inject constructor(
    val adapter: LaunchesAdapter,
    private val eventBus: EventBus,
    private val launchesRepository: LaunchesRepository,
    private val rocketsRepository: RocketsRepository,
    private val resourcesProvider: ResourcesProvider,
    private val schedulersProvider: BaseSchedulersProvider,
    private val calendarProvider: CalendarProvider
): ViewModel() {

    val showLoadingSpinner = ObservableField(true)
    val onRefreshListener = SwipeRefreshLayout.OnRefreshListener { loadLaunches() }

    private var launchesList: ArrayList<LaunchEntity> = arrayListOf()
    private val rocketMap: HashMap<String, RocketEntity> = hashMapOf()

    init {
        loadLaunches()
    }

    private fun loadLaunches() {
        showLoadingSpinner.set(true)
        launchesRepository.getLaunches()
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.ui())
            .subscribe(this::onLoadLaunchesFinished, this::launchLoadingError)
    }

    private fun onLoadLaunchesFinished(launches: List<LaunchEntity>) {
        launchesList.clear()
        launchesList.addAll(launches)
        loadRockets()
    }

    private fun loadRockets() {
        rocketsRepository.getRockets()
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.ui())
            .subscribe(this::onLoadRocketsFinished, this::launchLoadingError)
    }

    private fun onLoadRocketsFinished(rockets: HashMap<String, RocketEntity>) {
        rocketMap.clear()
        rocketMap.putAll(rockets)
        showLaunches()
    }

    private fun showLaunches() {
        val launchListItemViewModels = arrayListOf<LaunchListItemViewModel>()
        val years = arrayListOf<Int>()
        launchesList.forEach {
            val rocket = rocketMap[it.rocketId]
            val year = Instant.ofEpochSecond(it.date_unix).atZone(ZoneOffset.UTC).year
            launchListItemViewModels.add(
                LaunchListItemViewModel(
                    resourcesProvider,
                    eventBus,
                    calendarProvider,
                    it,
                    rocket,
                    year
                )
            )
            if(!years.contains(year)) years.add(year)
        }
        adapter.setLaunches(launchListItemViewModels)
        eventBus.post(UpdateFiltersYearEvent(years))
        showLoadingSpinner.set(false)
    }

    fun filterLaunches(filters: Filters) {
        adapter.setFilters(filters)
    }

    private fun launchLoadingError(t: Throwable) {
        t.printStackTrace()
        eventBus.post(ToastEvent(R.string.could_not_load_launches_data))
        showLoadingSpinner.set(false)
    }
}