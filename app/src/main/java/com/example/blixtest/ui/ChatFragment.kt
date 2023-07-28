package com.example.blixtest.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.blixtest.databinding.FragmentChatBinding
import com.example.blixtest.room.MessagingAppRoomDatabase

class ChatFragment: Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding: FragmentChatBinding
        get() = _binding!!

    private lateinit var viewModel: ChatViewModel
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        viewModel = ChatViewModelFactory(MessagingAppRoomDatabase.getDatabase(requireContext())).create(ChatViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        connectViewModel()
    }

    private fun setupViews() {
        chatAdapter = ChatAdapter(requireContext())
        binding.recyclerViewMessages.adapter = chatAdapter
        binding.imageButtonSend.setOnClickListener {
            val messageText = binding.editTextWrite.text.toString()
            viewModel.addNewMessage(messageText)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun connectViewModel() {
        viewModel.onError.observe(viewLifecycleOwner) {
            Log.i("===HOME_VIEW_MODEL_ERROR===>", it)
        }

        viewModel.isBusy.observe(viewLifecycleOwner) {
            // TODO add a spinner if we have time
        }

        viewModel.onGetMessage.observe(viewLifecycleOwner) { messages ->
            chatAdapter.chatMessages = messages
        }

    }

}