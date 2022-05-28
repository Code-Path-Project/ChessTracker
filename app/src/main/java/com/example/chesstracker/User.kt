package com.example.chesstracker

import com.parse.ParseClassName
import com.parse.ParseObject
import com.parse.ParseUser

//This is a class associated with the User database
@ParseClassName("User")
class User: ParseObject() {

    fun getUser(): ParseUser?{
        return getParseUser(KEY_USER)
    }

    fun setFriends(friends: MutableList<User>) {
        put(KEY_FRIEND_LIST, friends)
    }

    fun getFriends(): MutableList<String>? {
        return getList<String>(KEY_FRIEND_LIST)
    }

    companion object {
        const val KEY_USERNAME = "username"
        //const val KEY_IMAGE = "image"
        const val KEY_USER = "user"
        const val KEY_FRIEND_LIST = "friend_list"
    }
}