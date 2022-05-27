package com.example.chesstracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.parse.ParseObject
import com.parse.ParseUser
import okhttp3.Headers
import org.json.JSONArray
import org.json.JSONObject

/**
 * LoginActivity is called when the app is opened. This activity serves to log in the user using
 * the chess.com api and our Parse database. Several parts of this class were adapted from our
 * Parstagram assignment.
 */
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        /*
        //TODO: Only temporarily commenting this out since we don't have a logout button
        //This allows a currently logged in user not to have to log in again.
        if(ParseUser.getCurrentUser() != null){
            goToMainActivity()
        }*/

        findViewById<Button>(R.id.btn_login).setOnClickListener{
            val username = findViewById<EditText>(R.id.et_username).text.toString()
            val password = findViewById<EditText>(R.id.et_password).text.toString()
            checkChessAPI(username, password)
        }

        findViewById<Button>(R.id.btn_signup).setOnClickListener{
            val username = findViewById<EditText>(R.id.et_username).text.toString()
            val password = findViewById<EditText>(R.id.et_password).text.toString()
            signUpUser(username, password)
        }
    }

    /**
     * If the username is not a chess.com username, then that is Toasted. If the username is a valid
     * chess.com username but does not exist in our Parse database then the user is prompted to make
     * an account. If the username and password are valid entries in our Parse database the user is
     * logged in.
     *
     * @param username A string that is the username typed in the username text field
     * @param password A string that is the password typed in the password text field
     */
    private fun checkChessAPI(username: String, password: String){
        val client = AsyncHttpClient()
        client.get("https://api.chess.com/pub/player/$username", object: JsonHttpResponseHandler() {

            //If the api call fails / the username does not exist in chess.com
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Toast.makeText(this@LoginActivity,
                    "Your username does not exist in chess.com. Please try again.",
                    Toast.LENGTH_SHORT).show()
                Log.e(TAG, "onFailure $statusCode")
            }

            //If the api call succeeds
            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                //Toast.makeText(this@LoginActivity,
                //    "We found some json. Here it is: $json", Toast.LENGTH_LONG).show()
                Log.e(TAG, "onSuccess $statusCode")
                val playerData = json.jsonObject
                //val playerID = json.jsonObject.get("player_id")

                loginUser(username, password, playerData)
                Log.e(TAG, "Player logged in: $playerData")
                //Toast.makeText(this@LoginActivity, playerData, Toast.LENGTH_SHORT).show()
            }
        })
    }

    //Sign up user
    //TODO: Also copy over playerID into database
    fun signUpUser(username: String, password: String){
        val user = ParseUser()

        // Set fields for the user to be created
        user.setUsername(username)
        user.setPassword(password)

        user.signUpInBackground { e ->
            if (e == null) {
                // User has successfully created a new account
                Log.i(TAG, "Successfully registered")
                Toast.makeText(this@LoginActivity, "Successfully registered and logged in",
                            Toast.LENGTH_SHORT).show()
                goToMainActivity()
            } else {
                // Sign up didn't succeed. Look at the ParseException
                // to figure out what went wrong
                Toast.makeText(this@LoginActivity, "Sign up was not successful", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }

    private fun loginUser(username: String, password: String,
                          playerData: JSONObject){
        ParseUser.logInInBackground(
            username, password, ({ user, e ->
                if (user != null) {
                    // Hooray! The user is logged in
                    Log.i(TAG, "Successfully logged in!")
                    goToMainActivity()
                } else {
                    e.printStackTrace()
                    //Toast.makeText(this,
                    //    "Username - " + username + "Password - " + password, Toast.LENGTH_SHORT).show()
                    Toast.makeText(this,
                        "Error logging in. An incorrect password or no " +
                                "ChessTracker account exists", Toast.LENGTH_SHORT).show()
                    Log.i(TAG, "Log in failed.")
                }
            })
        )
    }

    /**
     * Goes to MainActivity.
     */
    private fun goToMainActivity(){
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object{
        const val TAG = "LoginActivity"
    }
}