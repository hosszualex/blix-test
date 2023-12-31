package com.example.blixtest.ui

import androidx.lifecycle.*
import com.example.blixtest.repositories.IFriendManagementRepository
import com.example.blixtest.repositories.RoomFriendManagementRepositoryImpl
import com.example.blixtest.room.MessagingAppRoomDatabase
import com.example.blixtest.utils.SingleLiveEvent
import kotlinx.coroutines.launch
import modals.Friend

class HomeViewModel(private val database: MessagingAppRoomDatabase) : ViewModel() {

    private val _isBusy = MutableLiveData<Boolean>(false)
    val isBusy: LiveData<Boolean>
        get() = _isBusy

    private val _onError = SingleLiveEvent<String>()
    val onError: SingleLiveEvent<String>
        get() = _onError

    private val _onGetFriends = MutableLiveData<ArrayList<Friend>>()
    val onGetFriends: LiveData<ArrayList<Friend>>
        get() = _onGetFriends

    private val roomRepository: IFriendManagementRepository =
        RoomFriendManagementRepositoryImpl(database.friendDAO())

    init {
        getFriends()
    }


    fun getFriends() {
        if (_isBusy.value == null || _isBusy.value == true)
            return

        _isBusy.value = true
        viewModelScope.launch {
            roomRepository.getFriends({ friends ->
                _isBusy.value = false
                _onGetFriends.value = ArrayList(friends)
            }, {
                _isBusy.value = false
                _onError.value = it
            }
            )
        }
    }


    fun addNewFriend(name: String) {
        if (_isBusy.value == null || _isBusy.value == true)
            return

        val newId = _onGetFriends.value?.lastOrNull()?.id?.plus(1) ?: 1
        _isBusy.value = true
        val friend = Friend(newId, name)
        viewModelScope.launch {
            roomRepository.addFriend(friend, {
                _isBusy.value = false
                getFriends()
            }, {
                _isBusy.value = false
                _onError.value = it
            })
        }
    }

}

class HomeViewModelFactory(private val roomDatabase: MessagingAppRoomDatabase) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(roomDatabase) as T
        }
        throw IllegalArgumentException("UNKNOWN VIEW MODEL CLASS")
    }
}