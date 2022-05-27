package com.example.chesstracker.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chesstracker.R
import com.example.chesstracker.User
import com.example.chesstracker.UserAdapter
import com.parse.ParseQuery
import com.parse.ParseUser


/**
 * Class that displays the friends list. It contain a recycler view for the friend list and a
 * text view for the title of the page. The recycler view was implemented in a similar fashion to
 * our Parstagram assignment's Feed page.
 */
class FriendsFragment : Fragment() {

    lateinit var friendsRV: RecyclerView

    lateinit var adapter: UserAdapter

    var allFriends : MutableList<String> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friends, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var tempFriends: MutableList<String> = mutableListOf("Friend 1", "Friend 2", "Friend 3")

        var currentUser: ParseUser = ParseUser.getCurrentUser()

        friendsRV = view.findViewById(R.id.rv_friend_list)

        adapter = UserAdapter(requireContext(), allFriends as ArrayList<User>)
        friendsRV.adapter = adapter
        friendsRV.layoutManager = LinearLayoutManager(requireContext())
    }

}