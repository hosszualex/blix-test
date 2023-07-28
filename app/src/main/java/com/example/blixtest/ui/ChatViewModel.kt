package com.example.blixtest.ui

import androidx.lifecycle.*
import com.example.blixtest.repositories.IMessageManagementRepository
import com.example.blixtest.repositories.RoomMessagesManagementRepositoryImpl
import com.example.blixtest.room.MessagingAppRoomDatabase
import com.example.blixtest.utils.SingleLiveEvent
import kotlinx.coroutines.launch
import modals.Message

class ChatViewModel(private val database: MessagingAppRoomDatabase) : ViewModel() {

    private val _isBusy = MutableLiveData<Boolean>(false)
    val isBusy: LiveData<Boolean>
        get() = _isBusy

    private val _onError = SingleLiveEvent<String>()
    val onError: SingleLiveEvent<String>
        get() = _onError

    private val _onGetMessages = MutableLiveData<ArrayList<Message>>()
    val onGetMessage: LiveData<ArrayList<Message>>
        get() = _onGetMessages

    private val roomRepository: IMessageManagementRepository =
        RoomMessagesManagementRepositoryImpl(database.messageDAO())

    init {
        getMessages()
    }


    fun getMessages() {
        if (_isBusy.value == null || _isBusy.value == true)
            return

        _isBusy.value = true
        viewModelScope.launch {
            roomRepository.getMessages({ messages ->
                _isBusy.value = false
                _onGetMessages.value = ArrayList(messages)
            }, {
                _isBusy.value = false
                _onError.value = it
            }
            )
        }
    }


    fun addNewMessage(message: String?, isReceived: Boolean = false) {
        if (_isBusy.value == null || _isBusy.value == true)
            return

        if (message.isNullOrEmpty())
            return

        val newId = _onGetMessages.value?.lastOrNull()?.id?.plus(1) ?: 1
        _isBusy.value = true
        val message = Message(newId, message, isReceived)
        viewModelScope.launch {
            roomRepository.addMessage(message, {
                _isBusy.value = false
                getMessages()
            }, {
                _isBusy.value = false
                _onError.value = it
            })
        }
    }

}

class ChatViewModelFactory(private val roomDatabase: MessagingAppRoomDatabase) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(roomDatabase) as T
        }
        throw IllegalArgumentException("UNKNOWN VIEW MODEL CLASS")
    }
}