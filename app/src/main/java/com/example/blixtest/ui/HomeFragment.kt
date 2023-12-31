package com.example.blixtest.ui

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.blixtest.R
import com.example.blixtest.databinding.FragmentHomeBinding
import com.example.blixtest.room.MessagingAppRoomDatabase
import com.example.blixtest.utils.GsonUtil
import modals.Friend


class HomeFragment: Fragment(), FriendsListAdapter.IOnFriendClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = _binding!!

    private lateinit var viewModel: HomeViewModel
    private lateinit var friendsListAdapter: FriendsListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel = HomeViewModelFactory(MessagingAppRoomDatabase.getDatabase(requireContext())).create(HomeViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        connectViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupViews() {
        friendsListAdapter = FriendsListAdapter(requireContext(), this)
        binding.recyclerViewUsers.adapter = friendsListAdapter
        binding.floatingButtonAdd.setOnClickListener {
            showAddFriendDialog()
        }
    }

    private fun showAddFriendDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Add new friend")
        val input = EditText(requireContext())
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)
        builder.setPositiveButton("Add") { dialog, which ->
            viewModel.addNewFriend(input.text.toString())
        }
        builder.setNegativeButton(
            "Cancel"
        ) { dialog, which -> dialog.cancel() }

        builder.show()
    }

    private fun connectViewModel() {
        viewModel.onError.observe(viewLifecycleOwner) {
            Log.i("===HOME_VIEW_MODEL_ERROR===>", it)
        }

        viewModel.isBusy.observe(viewLifecycleOwner) {
            // TODO add a spinner if we have time
        }

        viewModel.onGetFriends.observe(viewLifecycleOwner) { friends ->
            friendsListAdapter.friendsList = friends
        }
    }

    override fun onFriendClicked(friend: Friend) {
        Navigation.findNavController(requireView()).navigate(R.id.fragmentChat, Bundle().apply {
            putString("FRIEND_KEY", GsonUtil.toJson(friend))
        })
    }
}