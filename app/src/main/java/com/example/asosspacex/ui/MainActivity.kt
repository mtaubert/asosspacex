package com.example.asosspacex.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.asosspacex.databinding.ActivityMainBinding
import com.example.asosspacex.ui.viewmodels.CompanyInfoViewModel
import com.example.asosspacex.ui.viewmodels.LaunchesListViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus

import org.greenrobot.eventbus.ThreadMode

import org.greenrobot.eventbus.Subscribe

import android.widget.PopupWindow

import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout

import com.example.asosspacex.R
import com.example.asosspacex.databinding.PopupLinksListBinding
import com.example.asosspacex.events.*
import com.example.asosspacex.ui.viewmodels.FiltersViewModel
import com.example.asosspacex.ui.viewmodels.LinksPopupViewModel
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var showFilters = false

    private val companyInfoViewModel by viewModels<CompanyInfoViewModel>()
    private val launchesListViewModel by viewModels<LaunchesListViewModel>()
    private val filtersViewModel by viewModels<FiltersViewModel>()
    @Inject
    lateinit var eventBus: EventBus

    override fun onStart() {
        super.onStart()
        eventBus.register(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.componentCompanyInfo.viewModel = companyInfoViewModel
        binding.componentLaunchesInfo.viewModel = launchesListViewModel
        binding.componentFilters.viewModel = filtersViewModel
        binding.filters.setOnClickListener {
           toggleFilters()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onShowLinksEvent(event: ShowLinksEvent) {
        if(event.links.isEmpty()) {
            Toast.makeText(this, R.string.no_links_found, Toast.LENGTH_SHORT).show()
        } else {
            val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val popupBinding = PopupLinksListBinding.inflate(inflater)

            val popupWindow = PopupWindow(this)
            popupWindow.contentView = popupBinding.root
            popupWindow.isFocusable = true
            popupWindow.width = ConstraintLayout.LayoutParams.MATCH_PARENT
            popupWindow.height = ConstraintLayout.LayoutParams.WRAP_CONTENT

            val linkPopupViewModel = LinksPopupViewModel()
            popupBinding.viewModel = linkPopupViewModel
            linkPopupViewModel.setLinks(event)

            popupWindow.showAtLocation(binding.root, Gravity.BOTTOM, 0, 0)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onShowToastEvent(event: ToastEvent) {
        Toast.makeText(this, event.stringRes, Toast.LENGTH_SHORT).show()
    }

    @Subscribe
    fun onLinkOpenEvent(event: OpenLinkEvent) {
        val linkIntent = Intent(Intent.ACTION_VIEW)
        linkIntent.data = Uri.parse(event.url)
        startActivity(linkIntent)
    }

    @Subscribe
    fun onFiltersAppliedEvent(event: FiltersAppliedEvent) {
        toggleFilters()
        launchesListViewModel.filterLaunches(event.filters)
    }

    @Subscribe
    fun onUpdateFiltersYearEvent(event: UpdateFiltersYearEvent) {
       filtersViewModel.updateYearsList(event.years)
    }

    private fun toggleFilters() {
        showFilters = !showFilters
        filtersViewModel.filtersVisible.set(showFilters)
        if(showFilters) showFilters() else hideFilters()
    }

    private fun showFilters() {
        val anim = AnimationUtils.loadAnimation(this, R.anim.show)
        binding.componentFilters.root.startAnimation(anim)
    }

    private fun hideFilters() {
        val anim = AnimationUtils.loadAnimation(this, R.anim.hide)
        binding.componentFilters.root.startAnimation(anim)
        anim.fillAfter = false

    }

    override fun onStop() {
        eventBus.unregister(this)
        super.onStop()
    }
}