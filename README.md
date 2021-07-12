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
- **Category:** 
- **Mobile:**
- **Story:**
- **Market:**
- **Habit:**
- **Scope:**

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
[This section will be completed in Unit 9]
### Models
[Add table of models]
### Networking
- [Add list of network requests by screen ]
- [Create basic snippets for each Parse network request]
- [OPTIONAL: List endpoints if using existing API such as Yelp]
