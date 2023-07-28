package com.example.blixtest.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import modals.Friend
import com.example.blixtest.databinding.ItemFriendBinding

class ChatAdapter(
    private val context: Context,
    private val listener: IOnFriendClickListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var friendsList: List<Friend> = listOf()
        set(value) {
            // TODO If time, implement Diff Util
            field = value
            notifyDataSetChanged()
        }

    interface IOnFriendClickListener {
        fun onFriendClicked()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FriendViewHolder(
            ItemFriendBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ),
                parent, false
            )
        )
    }

    override fun getItemCount(): Int = friendsList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as FriendViewHolder).bindView(
            friendsList[position]
        )
    }

    inner class FriendViewHolder(private val binding: ItemFriendBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(item: Friend) {
            with(binding) {
                textViewName.text = item.name
                root.setOnClickListener {
                    listener.onFriendClicked()
                }
            }
        }

    }
}