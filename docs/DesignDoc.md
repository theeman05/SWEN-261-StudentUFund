---
geometry: margin=1in
---
# PROJECT Design Documentation

> _The following template provides the headings for your Design
> Documentation.  As you edit each section make sure you remove these
> commentary 'blockquotes'; the lines that start with a > character
> and appear in the generated PDF in italics._

## Team Information
* Team name: Toasters
* Team members
  * Ethan Hartman
  * Albert Abaykhanov
  * Ben Griffin
  * Bevan Neiberg

## Executive Summary

A local organization that facilitates the funding of student projects. Volunteers can either provide monetary support or direct goods and services for projects of their choice.

### Purpose
Project Statement:
A web application which allows users to fund student projects in order to complete the projects.
The most important user group is the Student, which adds needs to their project. Their goal is to fulfill their project needs and finish the project.

### Glossary and Acronyms

| Term | Definition |
|------|------------|
| SPA | Single Page |
| MVP | Minimum Viable Product|


## Requirements

This section describes the features of the application.

> _In this section you do not need to be exhaustive and list every
> story.  Focus on top-level features from the Vision document and
> maybe Epics and critical Stories._

### Definition of MVP
The MVP allows for users to sign into the application, view student projects, add needs from those projects to their fund basket, and view the fund basket, as well as checking out their basket to fund the student projects.
For students, it allows the creation and managing of needs for project funding.

### MVP Features
The MVP is comprised of two top-level epics: the Admin Abilities and the Supporter Abilities, each having their respective user stories

Admin Abilities:
  > Managers can view who supports their needs: As an admin I want to view all the past receipts so that I can keep track of past funding.
  > Funded Need Leaderboard: As an Admin I want to see who has funded the most expensive sum of needs so that there is competition in the application.
  > Create New Need: AS an Admin I want to submit a request to create a new need (name [has to be unique], cost, quantity, type) SO THAT it is added to the cupboard.
  > Get a single Need: As an Admin I want to submit a request to get a single need so that I can access the cost, quantity and type.
  > Update a Need: As an Admin I want to submit a request to modify an item in the cupboard so that I can update needs in the cupboard.
  > Delete a single Need: As an Admin I want to submit a request to delete a single need SO THAT it is no longer in the cupboard.
  > Get entire cupboard: As an Admin I want to submit a request to get the cupboard SO THAT I can update needs in the cupboard.
  > Search for a Need: AS a Developer I WANT to submit a request to get the the needs in the cupboard whose name contains the given text, SO THAT I have access to only that subset of needs.

Supporter Abilities:
  > Add needs to basket: As a Supporter I want to add need to my funding basket so that I can potentially fund them later.
  > Remove Needs from basket: As a Supporter I want to remove needs from my funding basket so that if I change my mind, I don’t have to fund that need.
  > Fund Needs: As a Supporter I want to fund needs so that I can see a Student be successful with their project.
  > Get a Single Need: AS a Supporter I WANT to submit a request to get a single need SO THAT I can access the cost, quantity and type.
  > Search for Needs: AS a Supporter I WANT to submit a request to get the the needs in the cupboard whose name contains the given text, SO THAT I have access to only that subset of needs.
  > Funded Need Leaderboard: As a USER I want to see who has funded the most expensive sum of needs so that there is competition in the application.
  > Save Funded Needs: As a User I want funded needs to be saved so that they can be tracked.
  > Support Partial Amount: As a SUPPORTER I want to fund partial needs so that I don’t need to supply whole need quantities.
  > Display username: As a supporter, I want my screen to display my username so that I can tell that I’m logged in on the right account.
  > User Sign Up: As a potential supporter I want to create an account so that I can become a supporter.

### Enhancements

Enahncement 1: Supporter leaderboard
  After each user checks out needs, the monetary value of their support is saved and kept updated. A leaderboard is diplayed on the home page to illustrate the ranking of users based on the greatest total contributions.

Enhancement 2: Thanking message
  Admins have access to checkouts receipts, that also contain the contact information of the user who checked out, allowing the admin to send the user a personal message expressing their thanks for the user's support.


## Application Domain

This section describes the application domain.

![Domain Model](domain-model.png)

> _**[Sprint 2 & 4]** Provide a high-level overview of the domain for this application. You
> can discuss the more important domain entities and their relationship
> to each other._
Students can create and manage needs. Students can log in as admins.
Supporters can view needs in student projects and they can also add and remove needs from their funding basket.
Supporters can check out their fund basket.

## Architecture and Design

This section describes the application architecture.

### Summary

The following Tiers/Layers model shows a high-level view of the webapp's architecture. 
**NOTE**: detailed diagrams are required in later sections of this document. (_When requested, replace this diagram with your **own** rendition and representations of sample classes of your system_.) 

![The Tiers & Layers of the Architecture](architecture-tiers-and-layers.png)

The web application, is built using the Model–View–ViewModel (MVVM) architecture pattern. 

The Model stores the application data objects including any functionality to provide persistance. 

The View is the client-side SPA built with Angular utilizing HTML, CSS and TypeScript. The ViewModel provides RESTful APIs to the client (View) as well as any logic required to manipulate the data objects from the Model.

Both the ViewModel and Model are built using Java and Spring Framework. Details of the components within these tiers are supplied below.


### Overview of User Interface

The user flow begins at the home page, where users can either log in or explore the projects. Logged-in admin users, have access to a wide range of functionalities like project creation, management, and detailed views. Regular users can browse projects, support them, and engage through the messaging system. The checkout and basket pages facilitate the funding process, while the leaderboard adds a competitive or community-focused element. The application is designed to guide users smoothly from discovering projects to supporting them, with clear navigation and user-friendly interfaces.


### View Tier
The application is a system designed for creating and managing Needs. It offers a platform for students to seek support and for kind supporters to contribute, bringing projects to life.
Navigating the Application:

Upon launching the application, users are greeted with a login page. This is the gateway to the application's ecosystem, where users can access their roles either as students seeking support or as supporters willing to fund needs. 

The Admin Experience:

Logging in as an admin, users find themselves in the "Project Need Cupboard", a central repository where all student needs are listed. Here, an admin can edit existing needs by simply clicking on them. Once valid inputs are entered, the admin can successfully update the need, with the total cost dynamically reflected in the cupboard.

Creating and Managing Needs:

Transitioning to the perspective of a regular user, the application allows for the creation of new needs. A user can add any need they require as long as it doesn't contain conflicting names or other invalid input. In the funded needs section, the application showcases a comprehensive view of all needs that have been funded, including details about the supporters. Users can interact with this section to send messages to supporters.
 
The Supporter Experience:

From a supporter's perspective, the application provides a seamless experience in browsing and funding needs. A supporter logs in and navigates the list of needs, updating their basket with chosen items. The application dynamically updates the needs list and the stock availability as supporters fund items, ensuring real-time accuracy in the display of information. A search functionality is also available, allowing supporters to filter needs based on specific criteria.

Checkout and Leaderboard:

Supporters can proceed to checkout with their selected items in the funding basket. Post-checkout, the application updates the leaderboard, reflecting the contributions made by each supporter. The leaderboard is an engaging feature that ranks supporters based on their funding amounts, fostering a sense of community and competition.

Messages and Interaction:

Another key feature is the messaging system. Supporters receive messages for their contributions, which they can view and manage in their inboxes. This feature adds a layer of personal interaction between students and supporters, enhancing the sense of community within the application.

Conclusion and User Cycle:

As users log out and new users sign in, the application demonstrates its capacity to handle multiple user sessions and maintain the integrity of each user's actions. For example, when new user logs in, they see a fresh perspective of the application, with their funding basket and potential contributions reflected distinctly from previous users.

Sequence Diagram 1: Adding a Need to Funding Basket

![Sequence Diagram 1](sequin_1.png)

Sequence Diagram 2: Searching for a Need

![Sequence Diagram 2](sequin_2.png)

> _**[Sprint 4]** To adequately show your system, you will need to present the **class diagrams** where relevant in your design. Some additional tips:_
 >* _Class diagrams only apply to the **ViewModel** and **Model** Tier_
>* _A single class diagram of the entire system will not be effective. You may start with one, but will be need to break it down into smaller sections to account for requirements of each of the Tier static models below._
 >* _Correct labeling of relationships with proper notation for the relationship type, multiplicities, and navigation information will be important._
 >* _Include other details such as attributes and method signatures that you think are needed to support the level of detail in your discussion._

### ViewModel Tier
> _**[Sprint 4]** Provide a summary of this tier of your architecture. This
> section will follow the same instructions that are given for the View
> Tier above._

> _At appropriate places as part of this narrative provide **one** or more updated and **properly labeled**
> static models (UML class diagrams) with some details such as critical attributes and methods._
![ViewModel Tier class diagram](viewmodel_tier.png)

### Model Tier
For the model tier, we can see we have DAOS, which inherit from their own interfaces. These DAOS are used by the controllers. Each DAO has its own functionality, which is represented by the names of them. For example, the NeedFileDAO handles all persistence of Need objects. We can see it has public methods, createNeed, findNeeds, updateNeed, deleteNeed, and getNeed. This DAO additionally stores a list of the currently active needs in the system.
The NeedReceiptDAO handles all persistence of NeedReceipt objects. It has public methods, createOrUpdateReceipt, getReceipt, getUserFundingSSum, and getReceipts. This DAO additionally stores a list of the currently active need receipts in the system.
Lastly, the UserFileDAO handles all persistence of User objects. It has public methods, getUser, getMessageToUser, createSupporter, logoutCurUser, getBasketOrNormalNeed, deleteCurMessage, checkoutCurBasket, sendOrUpdateMessageToUser, loginUser, updateNeedInCurBasket. This DAO additionally stores a list of the currently active users in the system, and the logged in user information.
![Model Tier class diagram](model_tier.png)

## OO Design Principles
Principle 1: Single Responsibility
We can see single responsibility, as we have our controllers, persistence, and model classes all separated. Moving even deeper, we have a User class, which holds usernames for users, and a Supporter class specifically for supporters, which inherits from the user class and includes a funding basket. We separated the two because an admin is a user but is not a supporter. 

Principle 2: Law of Demeter
We see the law of Demeter when analyzing the controllers. The two controllers, UserController and NeedController only have access to their specific DAO class, UserDAO or NeedDAO. These controllers are then accessed by their specific Angular service, user service and need service.

Principle 3: High Cohesion
All of our classes are named according to their functionality, similarly, they use other classes which have related functionality.
For example, if we look at the NeedFileDAO, we can see that this handles all persistence of Need objects

Principle 4: Open/Closed
The Open/Closed Principle is used in our project such that classes are open for extension but closed for modification. A specific example of this was our use of extending error classes, instead of having to rewrite our files. We wrote our code so that when we need more error handling, we don't need to break other code to add more handling. 

## Static Code Analysis/Future Design Improvements

SonarQube recommends using fstrings instead of concatenation. For example, we should use LOG.info(“PUT /needs {need}”); Direct concatenation is apparently inefficient. 
![Using F-Strings](f_string_use.png)

Many of our controller returns include the item type while creating a new ResponseEntity. However, this is no longer used. Instead, we should just use empty diamond operator.
![Remove Type from Return Diamond Operator](diamond_remove.png)

All tests are public functions. These should be changed to private since they aren’t used elsewhere. 
![Public Test Methods](public_test_methods.png)

When doing test cases, assertEquals should use expected value, then actual as arguments. For some testing methods, we were doing the opposite.
![Assert Equals](assert_equals.png)

In the future, if we had more time, there would be numerous things we'd like to accomplish. Firstly, we would have liked making it so that the admin wasn't the only student who could have a project, and add needs to their project. It would have been nice if users could sign up as students, create projects for themselves, post needs, and have those needs be funded. Additionally, our UI was quite simple. It would have been nice to have a more complex UI, with more features, such as better need filtering, and a more complex leaderboard. Lastly, it would have been nice to track the progress of projects and needs so users can ssee how close a project is to being completed.

## Testing
> _This section will provide information about the testing performed
> and the results of the testing._

### Acceptance Testing
> _**[Sprint 4]** Report on the number of user stories that have passed all their
> acceptance criteria tests, the number that have some acceptance
> criteria tests failing, and the number of user stories that
> have not had any testing yet. Highlight the issues found during
> acceptance testing and if there are any concerns._
Sprint 2: 9/9 user stories passed all acceptance criteria tests. No issues found during acceptance testing.

Sprint 3: 30/34 acceptance criteria passing tests, 2 of which were old acceptance criteria which should have been updated, but they weren't, resulting in failed tests. For the last 2, the testing team couldn't figure out how to use the search bar. However, if used correctly, the acceptance criteria do pass.

### Unit Testing and Code Coverage
> _**[Sprint 4]** Discuss your unit testing strategy. Report on the code coverage
> achieved from unit testing of the code base. Discuss the team's
> coverage targets, why you selected those values, and how well your
> code coverage met your targets._
Sprint 2: 9/9 user stories passed all acceptance criteria tests. No issues found during acceptance testing.

Sprint 3: 100% code coverage, no missing instructions and no missing branches.
![](jacoco-sprint-3.png)

>_**[Sprint 2 & 4]** **Include images of your code coverage report.** If there are any anomalies, discuss
> those._
