Original App Design Project - README Template
===

# Farmer's Market

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
Users will be able to buy and sell overflow home-grown produce.

### App Evaluation
[Evaluation of your app across the following attributes]
- **Category:** Marketplace App
- **Mobile:** Users can use the camera on their phone to take pictures of produce and can use the location services feature to see how close sellers are to them.
- **Story:** Often times, produce is produced in overflow for one individual family to consume (e.g.: an orange tree produces between 100 to 300 oranges a year) - to prevent food waste and to make some extra money, users can sell their overflow produce.
- **Market:** The app would be more useful for users in warmer climates (e.g.: California or Florida) where produce is often grown in overflow. Anyone in these climates could use the app.
- **Habit:** Users would be able to buy and sell produce on the app. They would likely use it every week when they purchase groceries. 
- **Scope:** The basic MVP of this app could be completed during the next 5 weeks. Additional features can be implemented as time permits.

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* Sign up
* Log in
* Profile page
* Home feed
* Make a listing
* Search
* Maps feature

**Optional Nice-to-have Stories**

* Messaging
* In-app payment 
* Following/Followers feature
* Liking feature
* Visual effects (pull to refresh, double tap to like, etc.)

### 2. Screen Archetypes

* Login screen
   * Log in feature
   * Link to sign up screen
* Sign up screen
   * Sign up feature
   * Link to login screen
* Home feed
* Search screen
   * Seach feature
* List screen
   * Make a listing
* Profile screen
   * Profile screen 

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Bottom navigation
   * Home
   * Search
   * List
   * Profile

**Flow Navigation** (Screen to Screen)

* Login screen
   * If users don't have an account: sign up screen
   * If users log in successfully: home screen
* Sign up screen
   * If users have an account: log in screen
   * If users sign up successfully: home screen
* Home screen
   * If users click on a listing: detailed listing screen
   * If users click on search (bottom navigation): search screen
   * If users click on list (bottom navigation): list screen
   * If users click on profile (bottom navigation): profile screen
* Search screen
   * Search results screen
* Search results screen 
   * If users click on a search result: detailed listing screen
* List screen
   * If users post successfully: home screen
* Profile screen

## Wireframes
[Add picture of your hand sketched wireframes in this section]
<img src="YOUR_WIREFRAME_IMAGE_URL" width=600>

### [BONUS] Digital Wireframes & Mockups

### [BONUS] Interactive Prototype

## Schema 
# Post
| Property        | Type                    | Description                                  | Required? |
| -------------   | ----------------------- | -------------------------------------------- | --------  |
| objectId        | String                  | Unique ID for the listing (default field)    | Yes       |
| createdAt       | DateTime                | Date when listing is created (default field) | Yes       |
| author          | Pointer to User         | Author of the listing                        | Yes       |
| image           | File                    | Image of the listing                         | No        |
| description     | String                  | Description of the listing                   | Yes       |
| location        |                         | Location of listing                          | Yes       |
| typeOfProduce   | String                  | Type of produce                              | Yes       |
| sellBy          | DateTime                | Date when the produce should be sold         | Yes       |

# User
| Property        | Type                    | Description                                  | Required? |
| -------------   | ----------------------- | -------------------------------------------- | --------  |
| objectId        | String                  | Unique ID for the user (default field)       | Yes       |
| username        | String                  | Username for the user                        | Yes       |
| password        | String                  | Password for the user                        | Yes       |
| email           | String                  | Email for the user                           | Yes       |
| profilePic      | File                    | Profile pic for the user                     | No        |
| location        |                         | Location of the user                         | No        |

### Models
[Add table of models]
### Networking
- [Add list of network requests by screen ]
- [Create basic snippets for each Parse network request]
- [OPTIONAL: List endpoints if using existing API such as Yelp]
