Original App Design Project - README
===

Group Members: Chiseng Wong, Yiyi Huang, Eric Chen, Abijit Jayachandran 

# ChessTracker

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
A chess tracker app that allows a user to look into their chess statistics over time compared to their friends' and other users. 

### App Evaluation

- **Category:** Games/Utility
- **Mobile:** Convenience. It is a companion of sorts to a chess mobile app. It may be easier to access your statistics through an app rather than a website. 
- **Story:** Allows users to track their chess statistics and compare it to their friends'
- **Market:** Chess players who want to look at specific statistics and those who want to compare with friends. 
- **Habit:** Most users will use the app every day or at least once a week to track their progress. 
- **Scope:** This app could start off as a simple statistics app that looks at your past ratings compared to your friends. Perhpas, in the future, it could also include a forum of sorts where you can discuss chess with others. 

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* User can log-in using their chess.com username
* User can see a leaderboard with their friends 
* User can see their rating over time
* User can see their chess game history and look over specific games. 

**Optional Nice-to-have Stories**

* Visualisation of the data (pie charts, bar charts, line graph)
* A "compare" feature that puts your statistics next to someone elses 
* User can create leaderboard with specific people rather than all their friends. 
* Have a leaderboard for specific regions such as San Diego, or LA.
* Have a forum for discussion
* User can logout and change preferences
* User can search for other users from chess.com


### 2. Screen Archetypes

* Register - User signs up or logs into their account
* User is prompted to log in to one's chess.com account gain access to their profile information
* Settings Screen
* User can logout and change preferences 
* Detail/ Profile Screen
* User can view their own or other users' detailed stats
* Main/Stream Screen
* User can see a leaderboard with their friends 
* User can see their rating over time
* User can see their chess game history and look over specific games. 
* User can search for other users from chess.com


### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Main Screen
* Settings

**Flow Navigation** (Screen to Screen)

* Log-in screen 
* Main Screen
* Detail (specific users from search)


## Wireframes
<img src="https://github.com/Code-Path-Project/ChessTracker/blob/main/Wireframe_codePath_v2.jpg" width=600>

## Schema 
### Models
Model: User
| Property       | Type          | Description |
| -----------    | -----------   | ----------- |
| playerId       | Integer       | unique Id for user
| username       | String        | username of user
| rating         | Number        | rating of the user
| profileImage   | File          | profile picture of the user
| recentGames    | Array<Game>   | recent games from user
| friendList     | Array<User>   | friends of user

Model: Game
| Property    | Type             | Description |
| ----------- | -----------      | ----------- |
| id          | String           | unique id of game
| Result      | Number           | Result of the game
| player1     | Pointer to User  | Player playing white 
| player2     | Pointer to User  | Player playing black 
| date        | DateTime         | date and time that the game was played


### Networking

#### List of network requests by screen 

- Login Screen
  - (Read/GET) Query for specified user

- Main Screen
  - (Read/GET) Query all players sorted by rating //leaderboard
  - (Read/GET) Query all games in recentGames array //recentGame
  - (Read/GET) Query all rating from user's recentGames //data graph
- Detail Screen
  - (Read/GET) Query all games in the player's recentGames array //recentGame
  - (Read/GET) Query all rating from the player's recentGames //data graph
- Setting Screen
  - (Read/GET) Query preferences on Chess.com (profileImage, username)
  - (Update/PUT) Update user's app preference (theme)
- FriendList Screen
  - (Create/POST) Add a new User (object) as a friend
  - (Read/GET) Query if the user exists on the site
  - (Read/GET) Query all players in friendList array

#### Code Snippet for network requests 
```kotlin
val client = AsyncHttpClient()
client.get("https://api.chess.com/pub/{Endpoint}", object: JsonHttpResponseHandler() {
override fun onFailure(
statusCode: Int,
headers: Headers?,
response: String?,
throwable: Throwable?
) {
Log.e(TAG, "onFailure $statusCode")
}

override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
// use data
}
}
```
The general framework for each network call will follow the above format. However, we will have to change the API endpoint and the JSON data extraction based on what we need for that particular request. The idea for this framework was taken from our Flixster assignment. However, to make our friend list and log-in we need a different code snippet. 

```kotlin
val query: ParseQuery<User> = ParseQuery.getQuery(Post::class.java)
query.include(User.playerID)
```
We can use the above format to add data to our own Parse database. Our database contains each user's playerID,  their app preferences and their friend list. This format is similar to the statements made in our Parstagram assignment. The log-in screen will also make a request to our Parse database.     

#### Chess&#46;com API

We will be using the [chess.com API](https://www.chess.com/news/view/published-data-api#pubapi-endpoint-player-stats) to access a given user's data. 

Base URL: https://api.chess.com/pub/
| HTTP Verb | Endpoint | Description |
| --------- | -------- | ----------- |
| GET       | /player/{username}       | get info about user
| GET       | /player/{username}/games | recent games of user
| GET       | /player/{username}/stats | gets rating data of user
