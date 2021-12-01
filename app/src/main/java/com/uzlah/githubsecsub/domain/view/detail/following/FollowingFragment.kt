package com.uzlah.githubsecsub.domain.view.detail.following

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.uzlah.githubsecsub.databinding.FragmentFollowingBinding
import com.uzlah.githubsecsub.domain.view.home.MainAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FollowingFragment : Fragment() {

    private lateinit var followingBinding: FragmentFollowingBinding
    private lateinit var followingViewModel: FollowingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        followingBinding = FragmentFollowingBinding.inflate(layoutInflater)
        setRecyclerView()
        setViewModelProvider()
        observeData()
        loading()
        setError()

        return followingBinding.root
    }

    private fun setError() {
        followingViewModel.error.observe(viewLifecycleOwner, {
            if (it == null) {
                followingBinding.apply {
                    IvErrorFollowing.visibility = View.GONE
                    RvFollowing.visibility = View.VISIBLE
                }
            } else {
                followingBinding.apply {
                    IvErrorFollowing.visibility = View.VISIBLE
                    RvFollowing.visibility = View.GONE
                }
            }
        })
    }

    private fun loading() {
        followingViewModel.loading.observe(viewLifecycleOwner, { isLoading ->
            if (isLoading) {
                followingBinding.apply {
                    PbFollowing.visibility = View.VISIBLE
                    RvFollowing.visibility = View.GONE
                }
            } else {
                followingBinding.apply {
                    PbFollowing.visibility = View.GONE
                    RvFollowing.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun observeData() {
        val username = arguments?.getString(USERNAME)
        followingViewModel.getFollowing(username ?: "")
        followingViewModel.followingLiveData.observe(viewLifecycleOwner, { listFollowing ->
            if ((listFollowing?.size ?: 0) == 0) {
                followingBinding.apply {
                    IvErrorFollowing.visibility = View.VISIBLE
                    RvFollowing.visibility = View.GONE
                }
            } else {
                followingBinding.apply {
                    PbFollowing.visibility = View.GONE
                    RvFollowing.visibility = View.VISIBLE

                    val mainAdapter = MainAdapter(listFollowing)
                    RvFollowing.adapter = mainAdapter
                }
            }
        })
    }

    private fun setViewModelProvider() {
        followingViewModel = ViewModelProvider(this)[FollowingViewModel::class.java]
    }

    private fun setRecyclerView() {
        followingBinding.RvFollowing.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = MainAdapter(listOf())
        }
    }

    companion object {
        private const val USERNAME = "username"
        fun newInstance(username: String): Fragment {
            val fragment = FollowingFragment()
            val bundle = Bundle()
            bundle.putString(FollowingFragment.USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }
}