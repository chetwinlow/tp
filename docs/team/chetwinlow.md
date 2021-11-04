---
layout: page
title: Chetwin's Project Portfolio Page
---

## Project: Restaurant HR Helper

Restaurant HR Helper(RHRH) is a desktop application that helps to streamline restaurant operations by allowing 
managers to easily organise and keep track of their customers, employees, suppliers and reservations in their 
restaurants.

### My Contributions to the project
**Code contributed**: [RepoSense link](https://nus-cs2103-ay2122s1.github.io/tp-dashboard/?search=T17&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2021-09-17&tabOpen=true&tabType=authorship&tabAuthor=Javiier-pzk&tabRepo=AY2122S1-CS2103T-T17-1%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code&authorshipIsBinaryFileTypeChecked=false)

#### Features Implemented
1. Implemented the [`add,edit and delete Customer`](https://github.com/AY2122S1-CS2103T-T17-1/tp/pull/55) functionality.
    * What it does: Add/Edit/Delete a `Customer` with the required and optional fields to the list of `Customers`.
    * Justification: We need to separate the different people, between employees, customers and suppliers and thus, 
      we separated the functions of Customers.

3. Implemented the [`find customer`](https://github.com/AY2122S1-CS2103T-T17-1/tp/pull/98) functionality.
    * What it does: Finds all customers that contain the string of words that user has inputted to find.
    * Justification: This feature allows managers to filter the `Customer` list based on any keyword they wish to 
      search for such as searching for how many customers have 3000 loyalty points.


4. Implemented the [`list customer`](https://github.com/AY2122S1-CS2103T-T17-1/tp/pull/55/commits/6418b5f722911a61116e57fbc531ba3f6c66077e) functionality.
    * What it does: Clears all filters and shows the entire list of `Customers`.
    * Justification: Easy way to clear all filters and display the entire list of customers again. It also serves as 
      a quick way to switch to the customer list if a user is currently viewing another list in RHRH.


#### Enhancements to existing features
   * Enhanced the [`Allergies`](https://github.com/AY2122S1-CS2103T-T17-1/tp/pull/55/commits/3487e616a48b40961f9bb17675195bcafa76b375) and [`Special Requests`](https://github.com/AY2122S1-CS2103T-T17-1/tp/pull/55/commits/3487e616a48b40961f9bb17675195bcafa76b375) classes by allowing them to be optional.
   * Added and improved integration and unit testing for all `Customer` related functionality. 
      (Pull Requests [\#63](https://github.com/AY2122S1-CS2103T-T17-1/tp/pull/63), 
      [\#189](https://github.com/AY2122S1-CS2103T-T17-1/tp/pull/189))
   * Beautified UI [UI aesthetics](https://github.com/AY2122S1-CS2103T-T17-1/tp/pull/95)

#### Contributions to team based tasks
* Removed all backend instances and usages of [`Person`](https://github.com/AY2122S1-CS2103T-T17-1/tp/pull/83) class entirely.
* Restructured test cases and main for usages of [`CommandResult`](https://github.com/AY2122S1-CS2103T-T17-1/tp/pull/86) class.
* Implemented logic for earlier iterations of [`Find`](https://github.com/AY2122S1-CS2103T-T17-1/tp/pull/98) command.
    
#### Review and Mentoring contributions
* PRs reviewed with comments(non-exhaustive): PRs [\#191](https://github.com/AY2122S1-CS2103T-T17-1/tp/pull/191), 
      [\#99](https://github.com/AY2122S1-CS2103T-T17-1/tp/pull/99), [\#92](https://github.com/AY2122S1-CS2103T-T17-1/tp/pull/92)

#### Contributions beyond the team project
* Evidence of helping others: 
  * Found bugs in team [CS2103T-W17-3's](https://github.com/chetwinlow/ped/issues) product.
  
#### Contributions to the User Guide (UG):
* Documented all [`Customer`](https://github.com/AY2122S1-CS2103T-T17-1/tp/pull/135) related functionality in the UG
* Removed remaining traces of [`AB3`](https://github.com/AY2122S1-CS2103T-T17-1/tp/pull/45) related information from UG. 

#### Contributions to the Developer Guide (DG):
* Draw UML diagrams for [to be completed]
* Added [use case](https://github.com/AY2122S1-CS2103T-T17-1/tp/pull/32) for `Add Customer`, `Add Employee`, `Add Supplier` in DG
