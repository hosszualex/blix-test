package com.example.blixtest.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.blixtest.databinding.FragmentChatBinding
import com.example.blixtest.room.MessagingAppRoomDatabase
import com.example.blixtest.utils.GsonUtil
import modals.Friend


class ChatFragment: Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding: FragmentChatBinding
        get() = _binding!!

    private lateinit var viewModel: ChatViewModel
    private lateinit var chatAdapter: ChatAdapter
    private var friend: Friend? = null

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
        getArgumentData()
        setupViews()
        connectViewModel()
    }

    private fun getArgumentData() {
        friend = GsonUtil.fromJsonObject(arguments?.getString("FRIEND_KEY").toString(), Friend::class.java) as Friend?
    }

    private fun setupViews() {
        chatAdapter = ChatAdapter(requireContext())
        binding.recyclerViewMessages.adapter = chatAdapter
        val mLinearLayoutManager = LinearLayoutManager(requireContext())
        mLinearLayoutManager.stackFromEnd = true
        binding.recyclerViewMessages.layoutManager = mLinearLayoutManager
        binding.imageButtonSend.setOnClickListener {
            friend?.let { unwrappedFriend ->
                val messageText = binding.editTextWrite.text.toString()
                binding.editTextWrite.text?.clear()
                viewModel.addNewMessage(unwrappedFriend, messageText)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun connectViewModel() {
        friend?.let { viewModel.getMessages(it) }
        viewModel.onError.observe(viewLifecycleOwner) {
            Log.i("===HOME_VIEW_MODEL_ERROR===>", it)
        }

        viewModel.isBusy.observe(viewLifecycleOwner) {
            // TODO add a spinner if we have time
        }

        viewModel.onGetMessage.observe(viewLifecycleOwner) { messages ->
            chatAdapter.chatMessages = messages
            binding.recyclerViewMessages.smoothScrollToPosition(chatAdapter.itemCount)
        }

    }

}