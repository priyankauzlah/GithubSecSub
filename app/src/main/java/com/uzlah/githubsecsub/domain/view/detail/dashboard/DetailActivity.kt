package com.uzlah.githubsecsub.domain.view.detail.dashboard

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.uzlah.githubsecsub.R
import com.uzlah.githubsecsub.adapter.ViewPagerAdapter
import com.uzlah.githubsecsub.databinding.ActivityDetailBinding
import com.uzlah.githubsecsub.domain.model.DetailUserResponse
import com.uzlah.githubsecsub.domain.model.ItemsItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var detailBinding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel
    private var user: ItemsItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(detailBinding.root)

        getDataObject()
        setViewModelProvider()
        observeData()
        Loading()
        errorSetup()
        setViewPager()
    }

    private fun setViewPager() {
        val viewPagerAdapter = ViewPagerAdapter(this)
        viewPagerAdapter.username = user?.login
        detailBinding.VpDetail.adapter = viewPagerAdapter
        val tabLayout: TabLayout = detailBinding.TlDetail
        TabLayoutMediator(tabLayout, detailBinding.VpDetail){tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun errorSetup() {
        detailViewModel.error.observe(this, {
            if (it == null){
                detailBinding.IvErrorDetail.visibility = View.GONE
                detailBinding.ClDetail.visibility = View.VISIBLE
            }else{
                detailBinding.IvErrorDetail.visibility = View.VISIBLE
                detailBinding.ClDetail.visibility = View.GONE
            }
        })
    }

    private fun Loading() {
        detailViewModel.loading.observe(this, {isLoading ->
            if(isLoading){
                detailBinding.PbDetail.visibility = View.VISIBLE
            }else{
                detailBinding.PbDetail.visibility = View.GONE
            }
        })
    }

    private fun observeData() {
        detailViewModel.getDetailUser(user?.login ?: "")
        detailViewModel.detailUser.observe(this, { detail ->
            detailBinding.apply {
                Glide.with(this.root).load(detail?.avatar_url).apply(
                    RequestOptions().centerCrop().override(100)
                ).into(IvDetail)
                TvNameDetail.text = detail?.name ?: "Name Unavailable"
                TvUsernameDetail.text = detail?.login ?: "Username Unavailable"
                TvCompanyDetail.text = detail?.company ?: "Company Unavailable"
                TvLocationDetail.text = detail?.location ?: "Location Unavailable"
            }
        })
    }

    private fun setViewModelProvider() {
        detailViewModel = ViewModelProvider(this)[DetailViewModel::class.java]
    }

    private fun getDataObject() {
        user = intent.getParcelableExtra(EXTRA_DATA)
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }
}