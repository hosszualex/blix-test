package com.example.blixtest.repositories

import modals.Friend
import com.example.blixtest.room.IFriendDAO

class RoomFriendManagementRepositoryImpl(private val friendDAO: IFriendDAO):
    IFriendManagementRepository {

    override suspend fun getFriends(
        successCallback: (List<Friend>) -> Unit,
        failedCallback: (String) -> Unit
    ) {
        successCallback(friendDAO.getFriendsAlphabetically())
    }

    override suspend fun updateAllFriends(
        friends: List<Friend>,
        successCallback: () -> Unit,
        failedCallback: (String) -> Unit
    ) {
        friendDAO.deleteAll()
        friendDAO.insertAll(friends)
        successCallback()
    }

    override suspend fun addFriend(
        friend: Friend,
        successCallback: () -> Unit,
        failedCallback: (String) -> Unit
    ) {
        friendDAO.insert(friend)
        successCallback()
    }

}