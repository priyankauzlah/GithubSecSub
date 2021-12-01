package com.uzlah.githubsecsub.domain.view.detail.followers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.uzlah.githubsecsub.databinding.FragmentFollowersBinding
import com.uzlah.githubsecsub.domain.view.home.MainAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FollowersFragment : Fragment() {

    private lateinit var followersBinding: FragmentFollowersBinding
    private lateinit var followersViewModel: FollowersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        followersBinding = FragmentFollowersBinding.inflate(layoutInflater)
        showFollower()
        setViewModelProvider()
        observeData()
        loading()
        setError()

        return followersBinding.root
    }

    private fun setError() {
        followersViewModel.error.observe(viewLifecycleOwner, {
            if (it == null) {
                followersBinding.apply {
                    IvErrorFollower.visibility = View.GONE
                    RvFollowers.visibility = View.VISIBLE
                }
            } else {
                followersBinding.apply {
                    IvErrorFollower.visibility = View.VISIBLE
                    RvFollowers.visibility = View.GONE
                }
            }
        })
    }

    private fun loading() {
        followersViewModel.loading.observe(viewLifecycleOwner, { isLoading ->
            if (isLoading) {
                followersBinding.apply {
                    PbFollowers.visibility = View.VISIBLE
                    RvFollowers.visibility = View.GONE
                }
            } else {
                followersBinding.apply {
                    PbFollowers.visibility = View.GONE
                    RvFollowers.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun observeData() {
        val username = arguments?.getString(USERNAME)
        followersViewModel.apply {
            getFollowers(username ?: "")
            followerLiveData.observe(viewLifecycleOwner, { it ->
                if ((it?.size ?: 0) == 0) {
                    followersBinding.apply {
                        IvErrorFollower.visibility = View.VISIBLE
                        RvFollowers.visibility = View.GONE
                    }
                } else {
                    followersBinding.apply {
                        IvErrorFollower.visibility = View.GONE
                        RvFollowers.visibility = View.VISIBLE
                        val mainAdapter = MainAdapter(it)
                        RvFollowers.adapter = mainAdapter
                    }
                }
            })
        }
    }

    private fun setViewModelProvider() {
        followersViewModel = ViewModelProvider(this)[FollowersViewModel::class.java]
    }

    private fun showFollower() {
        followersBinding.RvFollowers.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = MainAdapter(listOf())
        }
    }

    companion object {
        private const val USERNAME = "username"
        fun newInstance(username: String): Fragment {
            val fragment = FollowersFragment()
            val bundle = Bundle()
            bundle.putString(USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }
}