package com.example.blixtest.repositories

import modals.Friend

interface IFriendManagementRepository {

    // TODO use Kotlin Flows if we have time
    suspend fun getFriends(successCallback: (List<Friend>) -> Unit, failedCallback: (String) -> Unit)

    suspend fun updateAllFriends(friends: List<Friend>, successCallback: () -> Unit, failedCallback: (String) -> Unit)

    suspend fun addFriend(friend: Friend, successCallback: () -> Unit, failedCallback: (String) -> Unit)
}