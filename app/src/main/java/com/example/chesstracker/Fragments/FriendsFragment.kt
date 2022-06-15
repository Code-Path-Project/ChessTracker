package com.example.chesstracker.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chesstracker.FriendsAdapter
import com.example.chesstracker.R
import com.example.chesstracker.User
import com.parse.ParseQuery
import com.parse.ParseUser


/**
 * Class that displays the friends list. It contain a recycler view for the friend list and a
 * text view for the title of the page. The recycler view was implemented in a similar fashion to
 * our Parstagram assignment's Feed page.
 */
class FriendsFragment : Fragment() {

    lateinit var friendsRV: RecyclerView

    lateinit var adapter: FriendsAdapter

    //Gets friends list from Parse database as a String with usernames split by commas
    var allFriends : String? = ParseUser.getCurrentUser().getString("friends_list")

    var allFriendsList: ArrayList<String> = arrayListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friends, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        friendsRV = view.findViewById<RecyclerView>(R.id.rv_friend_list)

        adapter = FriendsAdapter(requireContext(), allFriendsList)

        friendsRV.adapter = adapter
        friendsRV.layoutManager = LinearLayoutManager(requireContext())

        friendsRV.addItemDecoration( // Adding divider line between view
            DividerItemDecoration(
                friendsRV.getContext(),
                DividerItemDecoration.VERTICAL)
        )

        Log.i(TAG, "Before getFriends")
        getFriends()
        Log.i(TAG, "After getFriends")
    }

    fun getFriends(){
        Log.i(TAG, "$allFriends")

        if(allFriends.isNullOrBlank()){ return
        } else {
            //Split parse database string by , so that each item in allFriendsList is a username
            if (!(allFriends!!.contains(","))) {allFriendsList.add(allFriends!!)}
            else {
                allFriendsList.addAll(allFriends?.split(",") as ArrayList<String>)
            }

            adapter.notifyDataSetChanged()
        }
    }

    companion object {
        const val TAG = "FriendsFragment"


    }
}