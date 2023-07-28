package com.example.blixtest.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import modals.Friend
import com.example.blixtest.databinding.ItemFriendBinding
import com.example.blixtest.databinding.ItemReceivedMessageBinding
import com.example.blixtest.databinding.ItemSentMessageBinding
import modals.Message

class ChatAdapter(
    private val context: Context,
) :
    RecyclerView.Adapter<ViewHolder>() {

    var chatMessages: List<Message> = listOf()
        set(value) {
            // TODO If time, implement Diff Util
            field = value
            notifyDataSetChanged()
        }

    companion object {
        const val SENT_MESSAGE = 101
        const val RECEIVED_MESSAGE = 102
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == SENT_MESSAGE) {
            SentMessageViewHolder(
                ItemSentMessageBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ),
                    parent, false
                )
            )
        } else {
            ReceivedMessageViewHolder(
                ItemReceivedMessageBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ),
                    parent, false
                )
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (chatMessages[position].isReceivedMessage)
            RECEIVED_MESSAGE
        else
            SENT_MESSAGE
    }

    override fun getItemCount(): Int = chatMessages.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is SentMessageViewHolder -> holder.bindView(chatMessages[position])
            is ReceivedMessageViewHolder -> holder.bindView(chatMessages[position])
            else -> Unit
        }
    }

    inner class ReceivedMessageViewHolder(private val binding: ItemReceivedMessageBinding) :
        ViewHolder(binding.root) {
        fun bindView(item: Message) {
            with(binding) {
                textViewName.text = item.message
            }
        }
    }

    inner class SentMessageViewHolder(private val binding: ItemSentMessageBinding) :
        ViewHolder(binding.root) {
        fun bindView(item: Message) {
            with(binding) {
                textViewName.text = item.message
            }
        }
    }
}