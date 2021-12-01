package com.uzlah.githubsecsub.domain.view.detail.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uzlah.githubsecsub.domain.model.DetailUserResponse
import com.uzlah.githubsecsub.domain.network.ApiResult
import com.uzlah.githubsecsub.domain.repository.UserRepository
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    private val _detailUser: MutableLiveData<DetailUserResponse?> = MutableLiveData()
    val detailUser get() = _detailUser

    private val _loading: MutableLiveData<Boolean> = MutableLiveData()
    val loading get() = _loading

    private val _error: MutableLiveData<Throwable?> = MutableLiveData()
    val error get() = _error

    private var stringUsername: String = " "

    fun getDetailUser(username: String) {
        if (stringUsername != username) {
            viewModelScope.launch {
                stringUsername = username
                userRepository.getDetailUser(username).onStart {
                    _loading.value = true
                }.onCompletion {
                    _loading.value = false
                }.collect {
                    when (it) {
                        is ApiResult.Success -> {
                            _error.postValue(null)
                            _detailUser.postValue(it.data)
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