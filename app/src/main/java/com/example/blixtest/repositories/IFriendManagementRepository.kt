package com.example.blixtest.repositories

import modals.Friend

interface IFriendManagementRepository {

    // TODO use Kotlin Flows if we have time
    suspend fun getDeliveryOrders(successCallback: (List<Friend>) -> Unit, failedCallback: (String) -> Unit)

    suspend fun updateAllDeliveryOrders(friends: List<Friend>, successCallback: () -> Unit, failedCallback: (String) -> Unit)

    suspend fun updateDeliveryOrder(friend: Friend, successCallback: () -> Unit, failedCallback: (String) -> Unit)
}