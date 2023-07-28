package com.example.blixtest.repositories

import modals.Friend
import com.example.blixtest.room.IMessagesDAO
import modals.Message

class RoomMessagesManagementRepositoryImpl(private val messagesDAO: IMessagesDAO):
    IMessageManagementRepository {

    override suspend fun getMessages(
        successCallback: (List<Message>) -> Unit,
        failedCallback: (String) -> Unit
    ) {
        successCallback(messagesDAO.getMessagesChronologically())
    }

    override suspend fun addMessage(
        message: Message,
        successCallback: () -> Unit,
        failedCallback: (String) -> Unit
    ) {
        messagesDAO.insert(message)
        successCallback()
    }

}