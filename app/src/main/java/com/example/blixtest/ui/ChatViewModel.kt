package com.example.blixtest.ui

import androidx.lifecycle.*
import com.example.blixtest.repositories.IMessageManagementRepository
import com.example.blixtest.repositories.RoomMessagesManagementRepositoryImpl
import com.example.blixtest.room.MessagingAppRoomDatabase
import com.example.blixtest.utils.SingleLiveEvent
import kotlinx.coroutines.launch
import modals.Friend
import modals.Message
import kotlin.random.Random

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


    fun getMessages(friend: Friend) {
        if (_isBusy.value == null || _isBusy.value == true)
            return

        _isBusy.value = true
        viewModelScope.launch {
            roomRepository.getMessages(friend.id, { messages ->
                _isBusy.value = false
                val newMessages = ArrayList(messages)
                _onGetMessages.value = newMessages
                verifyAutomaticResponse(friend, newMessages.lastOrNull())
            }, {
                _isBusy.value = false
                _onError.value = it
            }
            )
        }
    }


    fun addNewMessage(friend: Friend, message: String?, isReceived: Boolean = false) {
        if (_isBusy.value == null || _isBusy.value == true)
            return

        if (message.isNullOrEmpty())
            return

        val order = _onGetMessages.value?.lastOrNull()?.order?.plus(1) ?: 1
        _isBusy.value = true
        val message = Message((0..999999).random(), message, isReceived, friend.id, order)
        viewModelScope.launch {
            roomRepository.addMessage(message, {
                _isBusy.value = false
                getMessages(friend)
            }, {
                _isBusy.value = false
                _onError.value = it
            })
        }
    }

    private fun verifyAutomaticResponse(friend: Friend, message: Message?) {
        if (message == null)
            return
        // TODO Create constants if we have time
        val order = _onGetMessages.value?.lastOrNull()?.order?.plus(1) ?: 1
        val receivedMessage = when(message.message) {
            "Hi! How are you?" -> {
                "I'm good! How are you?"
            }
            "Did you manage to sell your car?" -> {
                "Unfortunately, not :(. It's very difficult in this marketplace"
            }
            else -> null
        }


        addNewMessage(friend, receivedMessage, true)
    }

}

class ChatViewModelFactory(private val roomDatabase: MessagingAppRoomDatabase) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChatViewModel(roomDatabase) as T
        }
        throw IllegalArgumentException("UNKNOWN VIEW MODEL CLASS")
    }
}