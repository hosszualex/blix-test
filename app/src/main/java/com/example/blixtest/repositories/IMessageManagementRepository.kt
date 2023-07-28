package com.example.blixtest.repositories

import modals.Friend
import modals.Message

interface IMessageManagementRepository {

    // TODO use Kotlin Flows if we have time
    suspend fun getMessages(parentId: Int, successCallback: (List<Message>) -> Unit, failedCallback: (String) -> Unit)

    suspend fun addMessage(message: Message, successCallback: () -> Unit, failedCallback: (String) -> Unit)
}