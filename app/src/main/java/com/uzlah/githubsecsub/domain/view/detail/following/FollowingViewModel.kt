package com.uzlah.githubsecsub.domain.view.detail.following

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uzlah.githubsecsub.domain.model.ItemsItem
import com.uzlah.githubsecsub.domain.network.ApiResult
import com.uzlah.githubsecsub.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowingViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    private var stringUserName: String = ""

    private val _followingLiveData = MutableLiveData<List<ItemsItem?>?>()
    val followingLiveData get() = _followingLiveData

    private val _loading: MutableLiveData<Boolean> = MutableLiveData()
    val loading get() = _loading

    private val _error: MutableLiveData<Throwable?> = MutableLiveData()
    val error get() = _error

    fun getFollowing(username: String) {
        if (stringUserName != username) {
            viewModelScope.launch {
                stringUserName = username
                userRepository.getFollowing(username).onStart {
                    _loading.value = true
                }.onCompletion {
                    _loading.value
                }.onCompletion {
                    _loading.value = false
                }.collect {
                    when (it) {
                        is ApiResult.Success -> {
                            _error.postValue(null)
                            _followingLiveData.postValue(it.data)
                        }
                        is ApiResult.Error -> {
                            _error.postValue(it.throwable)
                        }
                    }
                }
            }
        }
    }
}