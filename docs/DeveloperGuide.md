---
layout: page
title: Developer Guide
---
* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* This project is based on the AddressBook-Level3 project created by the [SE-EDU initiative](https://se-education.org).
* We would like to thank Jun Xiong for the invaluable guidance.

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams in this document can be found in the [diagrams](https://github.com/se-edu/addressbook-level3/tree/master/docs/diagrams/) folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** has two classes called [`Main`](https://github.com/AY2122S1-CS2103T-T17-1/tp/blob/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java). It is responsible for,
* At app launch: Initializes the components in the correct sequence, and connects them up with each other.
* At shut down: Shuts down the components and invokes cleanup methods where necessary.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

The rest of the App consists of four components.

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.


**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2122S1-CS2103T-T17-1/tp/blob/master/src/main/java/seedu/address/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2122S1-CS2103T-T17-1/tp/blob/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

How the `Logic` component works:
1. When `Logic` is called upon to execute a command, it uses the `RhrhParser` class to parse the user command.
2. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `AddEmployeeCommand`) 
   which is executed by the `LogicManager`.
3. The command can communicate with the `Model` when it is executed (e.g. to add a person).
4. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `RhrhParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddSupplierCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddSupplierCommand`) which the `RhrhParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddSupplierCommandParser`, `DeleteSupplierCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component

**API** : [`Model.java`](https://github.com/AY2122S1-CS2103T-T17-1/tp/blob/master/src/main/java/seedu/address/model/Model.java)

![ModelClassDiagram](images/ModelClassDiagram.png)

![ModelEntitiesClassDiagram](images/ModelEntitiesClassDiagram.png)

The `Model` component,

* stores RHRH data i.e., all `Customer`, `Employee`, `Supplier` and `Reservation` objects (which are contained in a `UniqueCustomerList`, `UniqueEmployeeList`, `UniqueSupplierList` and `UniqueReservationList` object respectively).
* stores the currently 'selected' `Customer`/`Employee`/`Supplier`/`Reservation` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Customer>`/`ObservableList<Employee>`/`ObservableList<Supplier>`/`ObservableList<Reservation>` respectively that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<div markdown="span" class="alert alert-info">:information_source: **Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `Rhrh`, which `Person` references. This allows `Rhrh` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<img src="images/BetterModelClassDiagram.png" width="450" />

</div>


### Storage component

**API** : [`Storage.java`](https://github.com/AY2122S1-CS2103T-T17-1/tp/blob/master/src/main/java/seedu/address/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save both RHRH data and user preference data in json format, and read them back into corresponding objects.
* inherits from both `RhrhStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.addressbook.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

<div markdown="span" class="alert alert-info">

:information_source: **Note:**

The lifeline for all activation bars should end at their respective destroy markers (X) but due to a limitation of PlantUML, the lifeline
  reaches the end of diagram.

</div>

### Add command

In the following section, we will be going through how our add command works. Since the add command is very similar across all person types, we will be using the `AddEmployeeCommand` to illustrate how the add command works. More information on the differences in adding `Customer`, `Employee` and `Supplier` can be found in our [User Guide](https://ay2122s1-cs2103t-t17-1.github.io/tp/UserGuide.html)

#### Current Implementation

{:no_toc}

Adding an employee can be split into 2 steps: `Parse` and `Execute`.

Below is a partial class diagram for `AddEmployeeCommand`. Note that to make it more comprehensible, details not
related to `AddEmployeeCommand` are abstracted away.

![AddEmployeeCommandClassDiagram](images/AddEmployeeCommandClassDiagram.png)

The sequence Diagram below illustrates the interactions within the `Logic` component for the `execute` API call. 

![AddEmployeeCommandSequenceDiagram](images/AddEmployeeCommandSequenceDiagram.png)

`Parse`:
1. When `Logic` is called upon to execute `AddEmployeeCommand`, it uses the `RhrhParser` class to parse the user command.
2. The `RhrhParser` then creates an `AddEmployeeCommandParser` to parse the command input.
3. If successful, this results in an `AddEmployeeCommand` object being created which will then be executed by the `LogicManager`.

`Execute`:
1. The instance of `AddEmployeeCommand` created can communicate with the `Model` when it is executed to add an employee to the `UniqueEmployeeList`.
2. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

**Flow of execution**

The activity diagram below shows the flow of execution when a user calls the `AddEmployeeCommand` together with the
details when this command is executed

![AddEmployeeCommandActivityDiagram](images/AddEmployeeCommandActivityDiagram.png)

#### Design Considerations

* Current Design: We separated `AddCustomerCommmand`, `AddEmployeeCommmand` and `AddSupplierCommmand` as different
  command types.
    * Pros
        * It is much neater as everything is divided based on the Person type and prefixes are easily separated based on whichever Person Type it is.
        * Errors in user input is easier to identify due to the separation of Person type.

    * Cons
        * Additional classes have to be implemented.
        * More commands for a similar feature.

* Alternative Design: We could have a singular `AddCommand` and parse the user's input to see what Person Type the user wanted to add.
    * Pros
        * Fewer commands for the user to remember.

    * Cons
        * Error messages would have been confusing to select.
    
#### Differences regarding adding reservation command

Parsing `Reservecommand` is similar to other add commands. However, since for `Reservation`, we need some logic to check for restaurant availability, then there are some differences regarding how a `Reservecommand` is executed.

The class diagram below demonstrates the class structure of `Reservecommand`.

![AddReservationCommandClassDiagram](images/AddReservationCommandClassDiagram.png)

The sequence diagram below shows how a valid `ReserveCommand` is executed.

![AddReservationCommandSequenceDiagram](images/AddReservationCommandSequenceDiagram.png)

### Edit command

In the following section, we will be going through how our edit command works. Since the edit command is very similar across all person types, we will be using the `EditCustomerCommand` to illustrate how the edit command works. More information on the differences in editing `Customer`, `Employee` and `Supplier` can be found in our [User Guide](https://ay2122s1-cs2103t-t17-1.github.io/tp/UserGuide.html)

#### Current Implementation

{:no_toc}

Editing a customer can be split into 2 steps: `Parse` and `Execute`.

Below is a partial class diagram for `EditCustomerCommand`. Note that to make it more comprehensible, details not
related to `EditCustomerCommand` are abstracted away.

![EditCustomerCommandClassDiagram](images/EditCustomerCommandClassDiagram.png)

The sequence Diagram below illustrates the interactions within the `Logic` component for the `execute` API call.

![EditCustomerCommandSequenceDiagram](images/EditCustomerCommandSequenceDiagram.png)

`Parse`:
1. When `Logic` is called upon to execute `EditCustomerCommand`, it uses the `RhrhParser` class to parse the user command.
2. The `RhrhParser` then creates an `EditCustomerCommandParser` to parse the command input.
3. If successful, this results in an `EditCustomerCommand` object being created which will then be executed by the `LogicManager`.

`Execute`:
1. The instance of `EditCustomerCommand` created can communicate with the `Model` when it is executed to add an customer to the `UniqueCustomerList`.
2. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

**Flow of execution**

The activity diagram below shows the flow of execution when a user calls the `EditCustomerCommand` together with the
details when this command is executed

![EditCustomerCommandActivityDiagram](images/EditCustomerCommandActivityDiagram.png)

#### Design Considerations

Due to the similarities of the design considerations between this command and the `Add` command, please refer to the `Design Considerations` section of the `Add` command above for more information.

#### Differences regarding editing reservation command

Implementation of `EditReservationcommand` is similar to other edit commands. However, to avoid clashing of the edited reservation with other reservations, only the editing of tags and remark is allowed.

### Delete Command

In the following section, we will be going through how our delete command works. Since the delete command is very 
similar across all person types, we will be using the `DeleteSupplierCommand` to illustrate how the delete 
command 
works. More information on the differences in deleting `Customer`, `Employee` and `Reservation` can be found in our 
[User Guide](https://ay2122s1-cs2103t-t17-1.github.io/tp/UserGuide.html)

#### Current Implementation

Deleting a supplier can be split into 2 steps: `Parse` and `Execute`.

Here's a partial class diagram for the `DeleteSupplierCommand`. Note that to make it more comprehensible, details
not related to the `DeleteSupplierCommand` are abstracted away.

![DeleteSupplierClassDiagram](images/DeleteSupplierClassDiagram.png)

The Sequence Diagram below illustrates the interactions within the `Logic` component for the `execute("deleteS 1")`
API call.

![Interactions Inside the Logic Component for the `deleteSupplier 1` Command](images/DeleteSequenceDiagram.png)

`Parse`:
1. When `Logic` is called upon to execute a command, it uses the `RhrhParser` class to parse the user command.
2. The `RhrhParser` then creates a `DeleteSupplierCommandParser` to parse the command input.
3. If successful, this results in a `DeleteSupplierCommand` object being created which then is executed by the
   `LogicManager`.


`Execute`:
1. The instance of `DeleteSupplierCommand` created can communicate with the `Model` when it is executed to delete a 
   supplier from the `UniqueSupplierList`.
2. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

**Flow of execution**

The activity diagram below shows the flow of execution when a user calls the `DeleteSupplierCommand` together with the
details when this command is executed

![DeleteSupplierActivityDiagram](images/DeleteActivityDiagram.png)


#### Design Considerations

Due to the similarities of the design considerations between this command and the `Add` command, please refer to the `Design Considerations` section of the `Add` command above for more information.

### Find Command

In the following section, we will be going through how our find command works. Since the find command is also very
similar across all person types, we will be using the `FindCustomerCommand` to illustrate how the find command
works. More information on the differences in finding `Supplier` and `Employee` can be found in our
[User Guide](https://ay2122s1-cs2103t-t17-1.github.io/tp/UserGuide.html)

#### Current Implementation

Finding of customers can be divided into two steps: `Parse` and `Execute`.

Below is a partial class diagram for `FindCustomerCommand`. Note that to make it more comprehensible, details not
related to `FindCustomerCommand` are abstracted away.

![FindCustomerCommandClassDiagram](images/FindCustomerClassDiagram.png)

The Sequence Diagram below illustrates the interactions within the `Logic` component for the `execute("findC Chetwin")` API call.

![FindCustomerCommandSequenceDiagram](images/FindCustomerCommandSequenceDiagram.png)

`Parse`:
1. When `Logic` is called upon to execute the `FindCustomerCommand`, it uses the `RhrhParser` class to parse the user command.
2. The `RhrhParser` then creates a `FindCustomerCommandParser` to parse the command input.
3. If successful, the `FindCustomerCommandParser` then creates a `CustomerClassContainsKeywordsPredicate` in `Model`
4. This finally results in a `FindCustomerCommand` object being created from the newly created `CustomerClassContainsKeywordsPredicate` and is then executed by the
   `LogicManager`.

`Execute`:
1. The instance of `FindCustomerCommand` created can communicate with the `Model` when it is executed to filter the
   `UniqueCustomerList` based on the `CustomerClassContainsKeywordsPredicate`.
2. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

**Flow of execution**

The activity diagram below shows the flow of execution when a user calls the `FindCustomerCommand` together with the 
details when this command is executed

![FindCustomerCommandActivityDiagram](images/FindCustomerCommandActivityDiagram.png)


#### Design Considerations

Due to the similarities of the design considerations between this command and the `Add` command, please refer to the `Design Considerations` section of the `Add` command above for more information.


### Sort Command
In the following section, we will be going through how our sort command works. Since the sort command is also very
similar across all person types, we will be using the `SortSupplierCommand` to illustrate how the sort command
works. More information on the differences in sorting `Customer` and `Employee` can be found in our
[User Guide](https://ay2122s1-cs2103t-t17-1.github.io/tp/UserGuide.html)

#### Current Implementation
Sorting of a supplier list can be divided into two steps: `Parse` and `Execute`.

Here's a partial class diagram for the `SortSupplierCommand`. Note that to make it more comprehensible, details
not related to the `SortSupplierCommand` are abstracted away.

![SortSupplierClassDiagram](images/SortClassDiagram.png)

The Sequence Diagram below illustrates the interactions within the `Logic` component for the `execute("sortS by/dd
o/a")` API call.

![SortSupplierSequenceDiagram](images/SortSequenceDiagram.png)

`Parse`:
1. When `Logic` is called upon to execute the `SortSupplierCommand`, it uses the `RhrhParser` class to parse the user command.
2. The `RhrhParser` then creates a `SortSupplierCommandParser` to parse the command input.
3. If successful, this results in a `SortSupplierCommand` object being created which then is executed by the
   `LogicManager`.

`Execute`:
1. The instance of `SortSupplierCommand` created can communicate with the `Model` when it is executed to sort the 
   `UniqueSupplierList` based on a sorting type and sorting order.
2. The `Comparator` that is used to sort the `UniqueSupplierList` is generated by the `SupplierComparator` class
3. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

**Flow of execution**

The activity diagram below shows the flow of execution when a user calls the `SortSupplierCommand` together with the
details when this command is executed

![SortSupplierActivityDiagram](images/SortActivityDiagram.png)


#### Design Considerations

Due to the similarities of the design considerations between this command and the `Add` command, please refer to the `Design Considerations` section of the `Add` command above for more information.

### Set Tables Command

#### Implementation

Set Tables command `settables` takes in a list of numbers as arguments and creates a TableList that is stored in RHRH

A SetTablesCommand is created through the usage of our `settables` command.

The process in which a new list of tables is set can be broken down into 2.

1. User input is sent into the RhrhParser and a SetTablesCommand is created.

2. The SetTablesCommand then proceeds to create and add the new tables to RHRH.

In the first step, the user input is parsed in SetTableCommandParser and broken down for other methods from other classes to create a SetTablesCommand. This is depicted in the Sequence Diagram below. 

![SetTablesSequenceDiagram](images/SetTablesSequenceDiagram.png)

Once the SetTablesCommand is created, the LogicManager will then execute the SetTablesCommand and add the newly created list of tables to the model

Finally, a success message is printed to the user saying how many tables were set

**Flow of execution:**

The activity diagram below shows the flow of execution when a user calls this command together with the details when SetTablesCommand is executed

![SetTablesActivityDiagram](images/SetTablesActivityDiagram.png)

After user calls the command:

1. Command syntax and user input arguments are checked if they are valid

2. User input arguments are parsed and a SetTablesCommand is created and executed

During the SetTablesCommand execution:

1. The Table ID will be reset

2. All current reservations will be removed

3. New tables with the specified sizes will be created and added into a new list

4. New list of tables will overwrite the old list of tables in the TableManager inside the RHRH

#### Design Considerations

1. User is allowed to format the argument for table sizes as a singular table size (e.g. `2` = 1 table that accommodates 2 people) or, with an `x` for multiple tables with the same size (e.g. `10x3` = 3 tables that accommodate 10 people)
    * This is because we find that in an actual restaurant, there will be many tables with the same sizes so this allows the user to set the tables faster without repeating the same table sizes many times. Hence, the design choice is to allow for these `x` syntax.


2. Table IDs will be reset to start from 1
   * This is because we want the new set of tables to have the correct table IDs starting from 1 and not be affected by the old set of tables


3. All current reservations will be removed
    * This is because each reservation is linked to exactly one table so changing the tables in the restaurant will mess up the reservation and thus the reservations and tables will not be in sync. Hence, the design choice is to remove all reservations.
    

4. There is no add/remove/edit table or table list
    * Again, this is because reservations are linked to the tables themselves and if the user alters the list of tables, the reservations will not be in sync.

**Alternatives considered:**

Alternatively, we plan to allow editing of tables and allowing the user to shift reservations to specific tables if they want to overwrite the original assigned table.

Checks would have to be implemented to ensure that the reservations-table pairings are still valid.

This will allow for greater flexibility for restaurant managers where they can manually assign seats to certain reservations (e.g. Assigning a big table to a small group of VIPs even though the table space is not optimized)

### Get corresponding customer command

For more information about this command and its usage, please refer to our [User Guide](https://ay2122s1-cs2103t-t17-1.github.io/tp/UserGuide.html)

#### Implementation

Getting corresponding customer can be divided into two steps, `Parse` and `Execute`.
Parsing for this command is similar to any Delete Command, where we only need to parse for an `INDEX`.
Hence, this section will focus on the executing aspect of this command.

The Sequence Diagram below illustrates how `getC 1` is executed, assuming `1` is a valid reservation index.

![GetCustomerReservingCommandSequenceDiagram](images/GetCustomerReservingCommandSequenceDiagram.png)

1. When a command is executed, it will interact with the `Model` to get the display reservation list. The `Reservation`
   specified by the given index is obtained from the list.
2. A `CustomerContainsPhonePredicate` is instantiated with the `Phone` of the `Reservation`, 
   and it is used to update the filtered customer list to contain only the corresponding customer.
3. The result of the command execution is encapsulated as a `CommandResult` object and returned back to the caller. 
   The Ui is also switched to customer view and display the corresponding customer.
   
#### Design Considerations

* Current design: We have a separate command to support viewing the information of a customer who made a reservation.
    * Pros: 
      * There is no overlapping information between customer view and reservation view, hence less coupling between reservation and customer code, thus it improves coding quality.
      *  We can manipulate reservations and customers separately, which is more flexible.
    * Cons: 
      * We need an extra step for viewing customer information corresponding to a reservation

* Alternative Design: Instead of a separate command, we can display the customer information with the reservation, or
display the reservations along with customer information.
  * Pros: 
    * This implementation is more intuitive as we do not need an extra step to view corresponding customer information.
  * Cons: 
    * It is harder to implement properly, as it will introduce more couplings to the codebase, espeacially when
    the database used is NoSQL, which hinders the ability to create relationship between entities.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedRhrh`. It extends `Rhrh` with an undo/redo history, stored internally as an `rhrhStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedRhrh#commit()` — Saves the current RHRH state in its history.
* `VersionedRhrh#undo()` — Restores the previous RHRH state from its history.
* `VersionedRhrh#redo()` — Restores a previously undone RHRH state from its history.

These operations are exposed in the `Model` interface as `Model#commitRhrh()`, `Model#undoRhrh()` and `Model#redoRhrh()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedRhrh` will be initialized with the initial RHRH state, and the `currentStatePointer` pointing to that single RHRH state.

![UndoRedoState0](images/UndoRedoState0.png)

Step 2. The user executes `delete 5` command to delete the 5th person in RHRH. The `delete` command calls `Model#commitRhrh()`, causing the modified state of RHRH after the `delete 5` command executes to be saved in the `rhrhStateList`, and the `currentStatePointer` is shifted to the newly inserted RHRH state.

![UndoRedoState1](images/UndoRedoState1.png)

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitRhrh()`, causing another modified RHRH state to be saved into the `rhrhStateList`.

![UndoRedoState2](images/UndoRedoState2.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If a command fails its execution, it will not call `Model#commitRhrh()`, so RHRH state will not be saved into the `rhrhStateList`.

</div>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoRhrh()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous RHRH state, and restores RHRH to that state.

![UndoRedoState3](images/UndoRedoState3.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index 0, pointing to the initial Rhrh state, then there are no previous Rhrh states to restore. The `undo` command uses `Model#canUndoRhrh()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</div>

The following sequence diagram shows how the undo operation works:

![UndoSequenceDiagram](images/UndoSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

The `redo` command does the opposite — it calls `Model#redoRhrh()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores RHRH to that state.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index `rhrhStateList.size() - 1`, pointing to the latest RHRH state, then there are no undone Rhrh states to restore. The `redo` command uses `Model#canRedoRhrh()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</div>

Step 5. The user then decides to execute the command `list`. Commands that do not modify RHRH, such as `list`, will usually not call `Model#commitRhrh()`, `Model#undoRhrh()` or `Model#redoRhrh()`. Thus, the `rhrhStateList` remains unchanged.

![UndoRedoState4](images/UndoRedoState4.png)

Step 6. The user executes `clear`, which calls `Model#commitRhrh()`. Since the `currentStatePointer` is not pointing at the end of the `rhrhStateList`, all RHRH states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

![UndoRedoState5](images/UndoRedoState5.png)

The following activity diagram summarizes what happens when a user executes a new command:

<img src="images/CommitActivityDiagram.png" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire RHRH.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* managers of restaurant in charge of overseeing the list of customers, employees and suppliers
* has a need to manage a significant number of contacts
* prefer desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**: This application is created with the purpose of helping restaurant managers to manage the employees’ information (shift schedule, contact), diners (reservation timing, diner contacts) and suppliers (supplier meet up timing/weekly collection schedule, contacts), and at the same time manage contacts faster than a typical mouse/GUI driven app


### User stories

Categories: Administrative, Customer, Employee, Supplier

Priorities: High, Low

| As a ... | I can ...                                                                                                             | so that ...                                                                            | Category       | Priority |
| -------- | --------------------------------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------- | -------------- | -------- |
| Manager  | View specific table capacity                                                                                          | I can assign the correct group to the correct table                                    | Administrative | High     |
| Manager  | Update table availability                                                                                             | I can ensure that people will not assign any more customers to that table              | Administrative | High     |
| Manager  | Check table availability                                                                                              | I can assign a new group to the table                                                  | Administrative | High     |
| Manager  | Add reservations (number of people, time, reserve table availability)                                                 | \-                                                                                     | Customer       | High     |
| Manager  | Check if a particular timeslot is available for reservation                                                           | Organise the customer's reservation slots                                              | Customer       | High     |
| Manager  | Add/View/Change/Delete particulars for customer(number, initials, allergies, special requests, number of people etc.) | I can contact the customers and also provide them with personalised dining experiences | Customer       | High     |
| Manager  | View the amount of unclaimed loyalty points a customer has                                                            | I can give them the option of redeeming the points for a reward                        | Customer       | High     |
| Manager  | Change the amount of loyalty points a customer has                                                                    | We can track the amount of loyalty points a customer has                               | Customer       | High     |
| Manager  | View reservations on a particular day                                                                                 | manage the reservations better                                                         | Customer       | High     |
| Manager  | Add customers to waiting list                                                                                         | Manage walk in customers                                                               | Customer       | Low      |
| Manager  | Add waiting list customers particulars                                                                                | Organise walk in customers turn to enter the restaurant                                | Customer       | Low      |
| Manager  | View customer's special payment method(vouchers)                                                                      | I can keep track of finances correctly                                                 | Customer       | Low      |
| Manager  | Add/View/Change/Delete employees' personal information                                                                | \-                                                                                     | Employee       | High     |
| Manager  | View/Change the number of leaves remaining a particular employee has                                                  | I can ensure he is only claiming leaves that he is entitled to                         | Employee       | High     |
| Manager  | View/Change who is supposed to be on shift today                                                                      | I know who is turning up for work today                                                | Employee       | High     |
| Manager  | View/Change who is supposed to be on shift this week                                                                  | I am aware of the week's scheduling                                                    | Employee       | High     |
| Manager  | View/Change who is supposed to be on shift this month                                                                 | I know who is turning up for work this month                                           | Employee       | High     |
| Manager  | View work shifts that have yet to be filled                                                                           | I can assign employees in to fill the manpower needs                                   | Employee       | High     |
| Manager  | Add employees to available slots in a work shift                                                                      | there would be no lack of personnel                                                    | Employee       | High     |
| Manager  | View/Change the monthly salary for a particular employee                                                              | I know if he is earning too much or too little                                         | Employee       | High     |
| Manager  | View/Change the job type for a particular employee (full time, part time etc)                                         | I know his payout scheme and if he deserves his pay                                    | Employee       | High     |
| Manager  | View which shifts a particular employee is working in a week                                                          | I know when he should be turning up                                                    | Employee       | Low      |
| Manager  | Add observations of an employee during a shift                                                                        | I can review them with him/her afterwards                                              | Employee       | Low      |
| Manager  | View/Change number of hours worked by a particular employee per period                                                | I can reward employees (employee of the month)                                         | Employee       | Low      |
| Manager  | Add/View/Update/Delete supplier particular(name, contact info, food type)                                             | \-                                                                                     | Supplier       | High     |
| Manager  | View all suppliers delivering today                                                                                   | I can know who to expect today                                                         | Supplier       | High     |
| Manager  | View all suppliers delivering this week                                                                               | I can know who to expect this week                                                     | Supplier       | High     |
| Manager  | View all deliveries for a particular supplier                                                                         | I can know the deliveries made by the supplier                                         | Supplier       | High     |
| Manager  | Add/View/Update/Delete delivery datetime by a particular supplier                                                     | I know what deliveries i will have for what time                                       | Supplier       | High     |
| Manager  | View status of deliveries                                                                                             | I know which deliveries have been completed and which are outstanding                  | Supplier       | High     |
| Manager  | Update deliveries as done                                                                                             | I won't accidentally wait for deliveries that have already been completed              | Supplier       | High     |
| Manager  | View past deliveries                                                                                                  | I can keep a receipt of the deliveries made in a specific period                       | Supplier       | High     |
| Manager  | View all suppliers by food type                                                                                       | I can know which supplier i have for a particular food type                            | Supplier       | Low      |

*{More to be added}*

### Use cases

(For all use cases below, the **System** is the `RHRH` and the **Actor** is the `user`, unless specified otherwise)

**Use case (UC01): Add a Customer**

{:no_toc}

**MSS**

1. User enters add customer command and keys in the customer’s details.

2. Customer is added to RHRH.

3. System confirms that the customer has been added.

   Use case ends.

**Extensions**

* 1a. System detects invalid command format.
    * 1a1. System displays an error, showing unknown command.
    * 1a2. User enters command again.

Steps 1a1 - 1a2 are repeated until the command is correctly formatted. <br>
Use case resumes from step 2.

* 1b. System detects invalid command arguments
  * 1b1. System displays error corresponding to the invalid argument and provides an example on the accepted format 
    and arguments.
  * 1b2. User enters command arguments again.

Steps 1b1 - 1b2 are repeated until all command arguments are valid. <br>
Use case resumes from step 2.

* 2a. Customer to add already exists in RHRH.
    * 2a1. System displays an error, and that the customer already exists in RHRH.
    * 2a2. User enters customer details again.

Steps 2a1 - 2a2 are repeated until the customer to be added is unique. <br>
Use case resumes from step 3.

**Use case (UC02): Add an Employee**

{:no_toc}

**MSS**

1. User enters add employee command and keys in the employee’s details.

2. Employee is added to RHRH.

3. System confirms that the employee has been added.

   Use case ends.

**Extensions**

* 1a. System detects invalid command format.
    * 1a1. System displays an error, showing unknown command.
    * 1a2. User enters command again.

Steps 1a1 - 1a2 are repeated until the command is correctly formatted. <br>
Use case resumes from step 2.

* 1b. System detects invalid command arguments.
    * 1b1. System displays error corresponding to the invalid argument and provides an example on the accepted format
      and arguments.
    * 1b2. User enters command arguments again.

Steps 1b1 - 1b2 are repeated until all command arguments are valid. <br>
Use case resumes from step 2.

* 2a. Employee to add already exists in RHRH.
    * 2a1. System displays an error, and that the employee already exists in RHRH.
    * 2a2. User enters employee details again.

Steps 2a1 - 2a2 are repeated until the employee to be added is unique. <br>
Use case resumes from step 3.

**Use case (UC03): Add a Supplier**

{:no_toc}

**MSS**

1. User enters add supplier command and keys in the supplier’s details.

2. Supplier is added to RHRH.

3. System confirms that the supplier has been added.

   Use case ends.

**Extensions**

* 1a. System detects invalid command format.
    * 1a1. System displays an error, showing unknown command.
    * 1a2. User enters command again

Steps 1a1 - 1a2 are repeated until the command is correctly formatted. <br>
Use case resumes from step 2.

* 1b. System detects invalid command arguments.
    * 1b1. System displays error corresponding to the invalid argument and provides an example on the accepted format
      and arguments.
    * 1b2. User enters command arguments again.

Steps 1b1 - 1b2 are repeated until all command arguments are valid. <br>
Use case resumes from step 2.

* 2a. System detects that supplier to add already exists in RHRH.
    * 2a1. System displays an error, and that the supplier already exists in RHRH.
    * 2a2. User enters supplier details again.

Steps 2a1 - 2a2 are repeated until the supplier to be added is unique. <br>
Use case resumes from step 3.

**Use case (UC04): Add a reservation**

**MSS**

1. User enters add reservation command and keys in reservations details.
2. System confirms that reservation with the given details is added.

   Use case ends

**Extensions**

* 1a. Tables are not set yet.
    * 1a1. System requests user to set the tables.
    * 1a2. User <u>sets the tables (UC <span style="color:red">{{FILL IN LATER}}</span>)</u>

    Use case resumes from step 1.

* 1b. System detects invalid command format.
    * 1b1. System displays an error, showing unknown command.
    * 1b2. User enters command again.

Steps 1b1 - 1b2 are repeated until the command is correctly formatted. <br>
Use case resumes from step 2.

* 1c. System detects invalid command arguments.
    * 1c1. System displays error corresponding to the invalid argument and provides an example on the accepted format
      and arguments.
    * 1c2. User enters command arguments again.

Steps 1c1 - 1c2 are repeated until the command arguments are valid. <br>
Use case resumes from step 2.

* 1d. System is unable to find a reservation slot based on the date time entered.
    * 1d1. System informs the user that the reservation slot is taken.
    * 1d2. User enters the command again with another date time.

Steps 1d1 - 1d2 are repeated until there is a reservation slot available. <br>
Use case resumes from step 2.

**Use case (UC05): Edit a Customer**

{:no_toc}

**MSS**

1. User enters the command to edit a customer and details of the customer to be edited.
2. The specified customer is edited in RHRH.
3. System confirms that customer has been edited.

   Use case ends.

**Extensions**

* 1a. System detects invalid command format.
    * 1a1. System displays an error, showing unknown command.
    * 1a2. User enters command again.

Steps 1a1 - 1a2 are repeated until the command is correctly formatted. <br>
Use case resumes from step 2.

* 1b. System detects invalid command arguments.
    * 1b1. System displays error corresponding to the invalid argument and provides an example on the accepted format
      and arguments.
    * 1b2. User enters command arguments again.

Steps 1b1 - 1b2 are repeated until all command arguments are valid. <br>
Use case resumes from step 2.

* 1c. System detects that customer to edit does not exist in RHRH.
    * 1c1. System displays an error that no such customer index exist in RHRH.
    * 1c2. User enters customer index again.

Steps 1c1 - 1c2 are repeated until the employee to be added is unique. <br>
Use case resumes from step 2.

* 1d. System detects that editing of current customer would cause duplicate customers in RHRH.
    * 1d1. System displays an error that customer already exists in RHRH.
    * 1d2. User enters customer index again.

Steps 1d1 - 1d2 are repeated until the employee to be added is unique. <br>
Use case resumes from step 2.

**Use case (UC06): Edit an Employee**

{:no_toc}

**MSS**

1. User enters the command to edit an employee and details of the employee to be edited.
2. The specified employee is edited in RHRH.
3. System confirms that employee has been edited.

   Use case ends.

**Extensions**

* 1a. System detects invalid command format.
    * 1a1. System displays an error, showing unknown command.
    * 1a2. User enters command again.

Steps 1a1 - 1a2 are repeated until the command is correctly formatted. <br>
Use case resumes from step 2.

* 1b. System detects invalid command arguments.
    * 1b1. System displays error corresponding to the invalid argument and provides an example on the accepted format
      and arguments
    * 1b2. User enters command arguments again.

Steps 1b1 - 1b2 are repeated until all command arguments are valid. <br>
Use case resumes from step 2.

* 1c. System detects that employee to edit does not exist in RHRH.
    * 1c1. System displays an error that no such employee index exist in RHRH.
    * 1c2. User enters employee index again.

Steps 1c1 - 1c2 are repeated until the employee to be added is unique. <br>
Use case resumes from step 2.

* 1d. System detects that editing of current employee would cause duplicate employees in RHRH.
    * 1d1. System displays an error that employee already exists in RHRH.
    * 1d2. User enters employee index again.

Steps 1d1 - 1d2 are repeated until the employee to be added is unique. <br>
Use case resumes from step 2.

**Use case (UC07): Edit a Supplier**

{:no_toc}

**MSS**

1. User enters the command to edit a supplier and details of the supplier to be edited.
2. The specified supplier is edited in RHRH.
3. System confirms that supplier has been edited.

   Use case ends.

**Extensions**

* 1a. System detects invalid command format.
    * 1a1. System displays an error, showing unknown command.
    * 1a2. User enters command again.

Steps 1a1 - 1a2 are repeated until the command is correctly formatted. <br>
Use case resumes from step 2.

* 1b. System detects invalid command arguments.
    * 1b1. System displays error corresponding to the invalid argument and provides an example on the accepted format
      and arguments.
    * 1b2. User enters command arguments again.

Steps 1b1 - 1b2 are repeated until all command arguments are valid. <br>
Use case resumes from step 2.

* 1c. System detects that supplier to edit does not exist in RHRH.
    * 1c1. System displays an error that no such supplier index exist in RHRH.
    * 1c2. User enters supplier index again.

Steps 1c1 - 1c2 are repeated until the employee to be added is unique. <br>
Use case resumes from step 2.

* 1d. System detects that editing of current supplier would cause duplicate suppliers in RHRH.
    * 1d1. System displays an error that supplier already exists in RHRH.
    * 1d2. User enters supplier index again.

Steps 1d1 - 1d2 are repeated until the supplier to be added is unique. <br>
Use case resumes from step 2.

**Use case (UC08): Edit a reservation**

**MSS**

1. User enters the command to edit reservation fields.
2. The specified reservation is edited in RHRH.
3. System confirms that the reservation has been edited and shows the edited reservation.

   Use case ends

**Extensions**

* 1a. User keys in invalid format.
    * 1a1. System displays an error, and the recommended format for editing a reservation.
    
    Use case ends.

* 1b. User keys in command without prefix.
    * 1b1. System displays an error that says that at least 1 field must be provided.

      Use case ends.

**Use case (UC09): Delete a Customer**

**MSS**

1. User enters the command to delete a customer.
2. The specified customer is deleted from RHRH.
3. System confirms that the customer has been deleted.

    Use case ends.

**Extensions**

* 1a. System detects invalid command format.
    * 1a1. System displays an error, showing unknown command.
    * 1a2. User enters command again.

Steps 1a1 - 1a2 are repeated until the command is correctly formatted. <br>
Use case resumes from step 2.

* 1b. System detects invalid customer index.
    * 1b1. System displays error corresponding to the invalid index and provides an example on the accepted format
      and arguments.
    * 1b2. User enters customer index again.

Steps 1b1 - 1b2 are repeated until all command arguments are valid. <br>
Use case resumes from step 2.

* 1c. System detects that customer to delete does not exist in RHRH.
    * 1c1. System displays an error that no such customer index exist in RHRH.
    * 1c2. User enters customer index again

Steps 1c1 - 1c2 are repeated until the customer to delete exists in RHRH. <br>
Use case resumes from step 2.

**Use case (UC10): Delete an Employee**

**MSS**

1. User enters the command to delete an employee.
2. The specified employee is deleted from RHRH.
3. System confirms that the employee has been deleted.

   Use case ends.

**Extensions**

* 1a. System detects invalid command format.
    * 1a1. System displays an error, showing unknown command.
    * 1a2. User enters command again.

Steps 1a1 - 1a2 are repeated until the command is correctly formatted. <br>
Use case resumes from step 2.

* 1b. System detects invalid employee index.
    * 1b1. System displays error corresponding to the invalid index and provides an example on the accepted 
      format and arguments.
    * 1b2. User enters employee index again.

Steps 1b1 - 1b2 are repeated until all command arguments are valid. <br>
Use case resumes from step 2.

* 1c. System detects that customer to delete does not exist in RHRH.
    * 1c1. System displays an error that no such employee index exist in RHRH.
    * 1c2. User enters employee index again

Steps 1c1 - 1c2 are repeated until the employee to delete exists in RHRH. <br>
Use case resumes from step 2.


**Use case (UC11): Delete a Supplier**

**MSS**

1. User enters the command to delete a supplier.
2. The specified supplier is deleted from RHRH.
3. System confirms that the supplier has been deleted.

   Use case ends.

**Extensions**

* 1a. System detects invalid command format.
    * 1a1. System displays an error, showing unknown command.
    * 1a2. User enters command again.

Steps 1a1 - 1a2 are repeated until the command is correctly formatted. <br>
Use case resumes from step 2.

* 1b. System detects invalid supplier index.
    * 1b1. System displays error corresponding to the invalid index and provides an example on the accepted format
      and arguments.
    * 1b2. User enters supplier index again.

Steps 1b1 - 1b2 are repeated until all command arguments are valid. <br>
Use case resumes from step 2.

* 1c. System detects that supplier to delete does not exist in RHRH.
    * 1c1. System displays an error that no such supplier index exist in RHRH.
    * 1c2. User enters supplier index again

Steps 1c1 - 1c2 are repeated until the supplier to delete exists in RHRH. <br>
Use case resumes from step 2.

**Use case (UC12): Find Customers in RHRH**

**MSS**

1. User enters the command to find the customers in RHRH based on a sequence of keywords.
2. The customer list is filtered in RHRH based on matching of keywords.
3. System confirms that the customer list has been filtered and displays number of filtered customers.

   Use case ends.

**Extensions**

* 1a. System detects invalid command format.
    * 1a1. System displays an error, showing unknown command.
    * 1a2. User enters command again.

Steps 1a1 - 1a2 are repeated until the command is correctly formatted. <br>
Use case resumes from step 2.

* 1b. System detects empty or invalid keywords.
    * 1b1. System displays error corresponding to the empty or invalid keywords and provides an example on the accepted format
      and arguments.
    * 1b2. User enters command arguments again.

Steps 1b1 - 1b2 are repeated until all command arguments are valid. <br>
Use case resumes from step 2.

**Use case (UC13): Find Employees in RHRH**

**MSS**

1. User enters the command to find the employees in RHRH based on a sequence of keywords.
2. The employee list is filtered in RHRH based on matching of keywords.
3. System confirms that the employee list has been filtered and displays number of filtered employees.

   Use case ends.

**Extensions**

* 1a. System detects invalid command format.
    * 1a1. System displays an error, showing unknown command.
    * 1a2. User enters command again.

Steps 1a1 - 1a2 are repeated until the command is correctly formatted. <br>
Use case resumes from step 2.

* 1b. System detects empty or invalid keywords.
    * 1b1. System displays error corresponding to the empty or invalid keywords and provides an example on the accepted format
      and arguments.
    * 1b2. User enters command arguments again.

Steps 1b1 - 1b2 are repeated until all command arguments are valid. <br>
Use case resumes from step 2.

**Use case (UC14): Find Suppliers in RHRH**

1. User enters the command to find the suppliers in RHRH based on a sequence of keywords.
2. The supplier list is filtered in RHRH based on matching of keywords.
3. System confirms that the supplier list has been filtered and displays number of filtered suppliers.

   Use case ends.

**Extensions**

* 1a. System detects invalid command format.
    * 1a1. System displays an error, showing unknown command.
    * 1a2. User enters command again.

Steps 1a1 - 1a2 are repeated until the command is correctly formatted. <br>
Use case resumes from step 2.

* 1b. System detects empty or invalid keywords.
    * 1b1. System displays error corresponding to the empty or invalid keywords and provides an example on the accepted format
      and arguments.
    * 1b2. User enters command arguments again.

Steps 1b1 - 1b2 are repeated until all command arguments are valid. <br>
Use case resumes from step 2.

**Use case (UC15): Search for reservation availability**

**MSS**

1. User requests to search for reservation availability at a date and time.
2. RHRH shows the availability for the indicated date time.
   
   Use case ends.

**Extensions:**

* 1a. User requests to search for reservation availability at a date.
    * 1a1. RHRH shows all the available slots on the indicated date.
      
    Use case ends.


* 1b. User requests to search for reservation availability at a time.
    * 1b1. RHRH shows the availability for the indicated time today.
      
    Use case ends.


* 1c. RHRH detects information missing from the command entered.
    * 1c1. RHRH requests for the missing details to be filled in.
    * 1c2. User enters missing data.

    Steps 1c1 - 1c2 are repeated until the date/time is correctly formatted.

    Use case resumes from step 2.

* 1d. RHRH detects an incorrect format of date/time.
    * 1d1. RHRH requests for the format to be corrected.
    * 1d2. User enters the command again.

    Steps 1d1 - 1d2 are repeated until the command is correctly formatted.

    Use case resumes from step 2.

**Use case (UC16): Sort Customers in RHRH*

**MSS**

1. User enters the command to sort the customer list in RHRH based on a sorting type and order.
2. The customer list is sorted in RHRH according to specified sorting type and order.
3. System confirms that the customers have been sorted.

   Use case ends.

**Extensions**

* 1a. System detects invalid command format.
    * 1a1. System displays an error, showing unknown command.
    * 1a2. User enters command again.

Steps 1a1 - 1a2 are repeated until the command is correctly formatted. <br>
Use case resumes from step 2.

* 1b. System detects invalid customer sorting type and order.
    * 1b1. System displays error corresponding to the invalid argument and provides an example on the accepted format
      and arguments.
    * 1b2. User enters command arguments again.

Steps 1b1 - 1b2 are repeated until all command arguments are valid. <br>
Use case resumes from step 2.

* 1c. System detects that there are no customers to sort in the current list.
    * 1c1. System displays an error that there are no customers to sort in the current list.
    

**Use case (UC17): Sort Employees in RHRH**

**MSS**

1. User enters the command to sort the employee list in RHRH based on a sorting type and order.
2. The employee list is sorted in RHRH according to specified sorting type and order.
3. System confirms that the employees have been sorted.

   Use case ends.

**Extensions**

* 1a. System detects invalid command format.
    * 1a1. System displays an error, showing unknown command.
    * 1a2. User enters command again.

Steps 1a1 - 1a2 are repeated until the command is correctly formatted. <br>
Use case resumes from step 2.

* 1b. System detects invalid employee sorting type and order.
    * 1b1. System displays error corresponding to the invalid argument and provides an example on the accepted format
      and arguments.
    * 1b2. User enters command arguments again.

Steps 1b1 - 1b2 are repeated until all command arguments are valid. <br>
Use case resumes from step 2.

* 1c. System detects that there are no employees to sort in the current list.
    * 1c1. System displays an error that there are no employees to sort in the current list.
    

**Use case (UC18): Sort Suppliers in RHRH**

**MSS**

1. User enters the command to sort the supplier list in RHRH based on a sorting type and order.
2. The employee list is sorted in RHRH according to specified sorting type and order.
3. System confirms that the employees have been sorted.

   Use case ends.

**Extensions**

* 1a. System detects invalid command format.
    * 1a1. System displays an error, showing unknown command.
    * 1a2. User enters command again.

Steps 1a1 - 1a2 are repeated until the command is correctly formatted. <br>
Use case resumes from step 2.

* 1b. System detects invalid supplier sorting type and order.
    * 1b1. System displays error corresponding to the invalid argument and provides an example on the accepted format
      and arguments.
    * 1b2. User enters command arguments again.

Steps 1b1 - 1b2 are repeated until all command arguments are valid. <br>
Use case resumes from step 2.

* 1c. System detects that there are no suppliers to sort in the current list.
    * 1c1. System displays an error that there are no suppliers to sort in the current list.
    

*{More to be added}*

### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `11` or above installed.
2.  Should be able to hold up to 1000 persons without a noticeable sluggishness in performance for typical usage.
3.  A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4.  Should not be stored in a **Database Management system**(DBMS).
5.  Should be stored *locally* and can be *manually* edited by the user.
6.  Program should be delivered to users in a single **JAR** file.
7.  Program should be able to run with just a *double-click* and no CLI required.
8.  Should be runnable on both `32-bit` and `64-bit` processors.
9.  New users should be able to navigate and use the program effectively.
10.  Software should not exceed **100MB**.
11.  Documents should not exceed **15MB** per file.
12.  Developer Guide and User Guide should be *PDF-friendly*(no expandable panels, embedded videos, animated GIFs etc.).
13.  Does not require a server component as the software will not perform automated tasks.

*{More to be added}*

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, OS-X
* **Contact**: A contact containing details about the particular person
* **Customer**: One of the types of contact, used to represent a customer of the restaurant
* **Loyalty** points: Points stored by each customer
* **Employee**: One of the types of contact, used to represent an employee of the restaurant
* **Employment type** : Used to distinguish the different categories of employees (Part-time, Full-time etc.)
* **Shift**: Time slot that employee is supposed to be working
* **Supplier**: One of the types of contact, used to represent a supplier of the restaurant
* **Table**: Contains a size and a table ID, and is used to ensure that each reservation has an available table
* **Reservation**: Reservations can be made by customers for a specific timeslot in the restaurant
* **Waiting list**: List of customers who are unable to get a reservation due to maxed-out reservations

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   2. Double-click the jar file <br>
   Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

2. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   2. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

3. Exiting the app

    1. While the app is still running, enter the `exit` command. <br>
       Expected: The app closes.
      
### Adding a customer/employee/supplier

1. Add a Customer to the Customer list
    1. Test case: `addC n/John Doe p/87654321 e/e12345@u.nus.edu a/30 Geylang Drive lp/1000 alg/Kiwi sr/outdoors` <br>
        Expected: Customer John Doe has been added to the Customer list. Details of the Customer John Doe will be shown on the result display area.
    2. Test case: `addC n/John Doey` <br>
        Expected: Customer will not be added, and `Invalid command format! ...` where `...` is the correct command format. Error is thrown due to missing compulsory fields.
    3. Test case: `addC n/J@hn p/87654321 e/e12345@u.nus.edu a/30 Geylang Drive lp/1000 alg/Kiwi sr/outdoors` <br>
        Expected: Customer will not be added, and an error message `Names should only contain alphanumeric characters and spaces, and it should not be blank` will be shown due to @ being non-alphanumeric.
    4. Test case: `addC n/John Doe p/87a2ff e/e12345@u.nus.edu a/30 Geylang Drive lp/1000 alg/Kiwi sr/outdoors`
        Expected: Customer will not be added, and an error message `Phone numbers should only contain numbers, and it should be at least 3 digits long` will be shown due to alphabets being in the phone number field.
    5. Test case: `addC n/John Doe p/87654321 e/e12345.LOL a/30 Geylang Drive lp/1000 alg/Kiwi sr/outdoors` <br>
        Expected: Customer will not be added, and an error message that tells the user the correct email format will be displayed.
    6. Test case: `addC n/John Doe p/87654321 e/e12345@u.nus.edu a/ lp/1000 alg/Kiwi sr/outdoors` <br>
        Expected: Customer will not be added, and an error message `Addresses can take any values, and it should not be blank` will be shown due to address being left blank.
    7. Test case: `addC n/John Doe p/87654321 e/e12345@u.nus.edu a/30 Geylang Drive lp/-2 alg/Kiwi sr/outdoors` <br>
        Expected: Customer will not be added, and an error message `Loyalty points must be numerical, more than or equals to 0 and less than or equals to 100000` will be displayed due to loyalty points being negative.
    8. Test case: `addC n/John Doe p/87654321 e/e12345@u.nus.edu a/30 Geylang Drive lp/1000 alg/*Kiwi* sr/outdoors` <br>
        Expected: Customer will not be added, and an error message `Allergies names should be alphanumeric` will be displayed due to * being non-alphanumeric.
    9. Test case: `addC n/John Doe p/87654321 e/e12345@u.nus.edu a/30 Geylang Drive lp/1000 alg/Kiwi sr/**outdoors**` <br>
        Expected: Customer will not be added, and an error message `SpecialRequests names should be alphanumeric` will be displayed due to * being non-alphanumeric.
       
2. Add an Employee to the Employee List
    1. Test case: `addE n/John Doe p/87654321 e/e12345@u.nus.edu a/30 Geylang Drive sal/4000 jt/Head Chef l/14 sh/2021-12-08 0800` <br>
        Expected: Employee John Doe has been added to the Employee List. Details of Employee John Doe will be shown on the result display area.
    2. Test case: `addE n/John Doey` <br>
        Expected: Employee will not be added, and `Invalid command format! ...` where `...` is the correct command format. Error is thrown due to missing compulsory fields.
    3. Test case: `addE n/John Doe p/87654321 e/e12345@u.nus.edu a/30 Geylang Drive sal/4000 jt/#Head Chef l/14 sh/2021-12-08 0800` <br>
        Expected: Employee will not be added, and an error message `job titles should only contain alphanumeric characters and spaces, and should not be left blank` will be shown due to # being non-alphanumeric.
    4. Test case: `addE n/John Doe p/87654321 e/e12345@u.nus.edu a/30 Geylang Drive sal/-100 jt/Head Chef l/14 sh/2021-12-08 0800` <br>
        Expected: Employee will not be added, and an error message `Salary should be numerical and more than or equal to 100 and less than or equals to 10 million` will be shown due to the salary being negative.
    5. Test case: `addE n/John Doe p/87654321 e/e12345@u.nus.edu a/30 Geylang Drive sal/4000 jt/Head Chef l/700 sh/2021-12-08 0800` <br>
        Expected: Employee will not be added, and an error message `Leaves should be numerical and be more than or equals to 0, less than or equals to 365` will be shown due to the number of leaves entitled exceeding 1 year.
       Test case: `addE n/John Doe p/87654321 e/e12345@u.nus.edu a/30 Geylang Drive sal/4000 jt/Head Chef l/14 sh/2021-12-08 2pm` <br>
        Expected: Employee will not be added, and an error message that shows the user the correct format for entering shifts will be displayed, due to the error in formatting for time.
       
3. Add a Supplier to the Supplier List
    1. Test case: `addS n/John Doe p/87654321 e/e12345@u.nus.edu a/30 Geylang Drive dd/2021-12-08 1200 st/Beef` <br>
        Expected: Supplier John Doe has been added to the Supplier List. Details of the Supplier John Doe will be shown on the result display area.
    2. Test case: `addS n/John Doey` <br>
        Expected: Supplier will not be added, and `Invalid command format! ...` where `...` is the correct command format. Error is thrown due to missing compulsory fields.
    3. Test case: `addS n/John Doe p/87654321 e/e12345@u.nus.edu a/30 Geylang Drive dd/2021-12-40 1200 st/Beef` <br>
        Expected: Supplier will not be added, and an error message that shows the user the correct format for entering delivery details will be displayed, due to the error caused by entering an invalid day.
    4. Test case: `addS n/John Doe p/87654321 e/e12345@u.nus.edu a/30 Geylang Drive dd/2021-14-12 1200 st/Beef` <br>
        Expected: Supplier will not be added, and an error message that shows the user the correct format for entering delivery details will be displayed, due to the error caused by entering an invalid month.
    5. Test case: `addS n/John Doe p/87654321 e/e12345@u.nus.edu a/30 Geylang Drive dd/2021-12-08 1200 st/#@$Beef` <br>
        Expected: Supplier will not be added, and an error message `Supply types should only contain alphanumeric characters and spaces, and it should not be blank` will be shown due to the presence of non-alphanumerical characters in the supply type.
        
       
### Adding a reservation

1. The tables are already set and there are still available tables.
   
   1. Prerequisites: The tables are already set using `setTables`. Please refer to our [User Guide](https://ay2122s1-cs2103t-t17-1.github.io/tp/UserGuide.html) for the command usage.
   2. Test case: `addr 2 p/PHONE at/24-12-2020 2000 r/some remark t/some tag t/another tag`, where `PHONE` must belong to a customer in the customer list, and there is an available table for 2 on `24-12-2020 2000`<br>
      Expected: a new reservation with the specified description is added to the reservation list.
   3. Test case: `addr 3 p/PHONE at/24-12-2020 1900`, where `PHONE` must belong to a customer in the customer list, and there is an available table for 3 on `24-12-2020 1900`<br>
      Expected: a new reservation with the specified description is added to the reservation list.
   4. Test case: `addr 5 p/PHONE at/DATE_TIME`, where `PHONE` and `DATE_TIME` belong one of the following case:
      * `PHONE` does not belong to any customer in the reservation list, `DATE_TIME` is valid (of correct format and there is an available table for 5 at that time)
      * `PHONE` is valid (belong to a customer) but `DATE_TIME` is invalid (of incorrect format or the restaurant is full at that time)<br>
      Expected: No reservation is added. Error details shown in the status message. Status bar remains the same.

2. The tables are not set yet.
   
   1. Prerequisite: the `tables` array in the data file is empty.
   2. Repeat the same test cases as 1.2 and 1.3.<br>
      Expected: No reservation is added. Error message shown, telling users to set tables before use.

### Editing a customer

1. Editing a customer while all customers are being shown.
    1. Prerequisites: List all customers using the `listC` command.
    2. Test case: `editC 1 n/Chetwin lp/5000` <br>
       Expected: First customer in the list will be updated to have "Chetwin" and "5000" as his `name` and `loyalty points` respectively. Details of the edited
       customer shown in the results display.
    3. Test case: `editC -1 e/chetwinlow@gmail.com` <br>
       Expected: Nobody is edited. Error details shown in the results display with a result message `Invalid comamnd
       format...`
    4. Test case: `editC 1` <br>
       Expected: Nobody is edited. Error details shown in the results display with a result message `At least one field to edit must be provided.`
    5. Other incorrect edit commands to try: `editC ` / `editC f/invalid prefix` / `edit 1` / `editC x n/john`(where x is larger than the customer
       list size) <br>
       Expected: Nobody is edited. Error details shown in the results display. Entering the first 2 commands will
       produce the same error message as test case 1.3 while entering `edit 1` will produce the error message
       `Unknown command` and the last one resulting in error message `The customer index provided is invalid`

2. Editing a customer while customer list is filtered.

    1. Prerequisites: Filter display list by using the `findC KEYWORD` command,
       where `KEYWORD` must exist in at least 1 of the customers.
    2. Repeat the same test cases as 1.2 to 1.4.

### Editing a supplier

1. Editing a supplier while all suppliers are being shown.
    1. Prerequisites: List all suppliers using the `listS` command.
    2. Test case: `editS 1 n/Chetwin st/chicken` <br>
       Expected: First customer in the list will be updated to have "Chetwin" and "chicken" as his `name` and `supply type` respectively. Details of the edited
       supplier shown in the results display.
    3. Test case: `editS -1 e/chetwinlow@gmail.com` <br>
       Expected: Nobody is edited. Error details shown in the results display with a result message `Invalid comamnd
       format...`
    4. Test case: `editS 1` <br>
       Expected: Nobody is edited. Error details shown in the results display with a result message `At least one field to edit must be provided.`
    5. Other incorrect edit commands to try: `editS ` / `editS f/invalid prefix` / `edit 1` / `editS x n/john`(where x is larger than the supplier
       list size) <br>
       Expected: Nobody is edited. Error details shown in the results display. Entering the first 2 commands will
       produce the same error message as test case 1.3 while entering `edit 1` will produce the error message
       `Unknown command` and the last one resulting in error message `The supplier index provided is invalid`

2. Editing a supplier while supplier list is filtered.

    1. Prerequisites: Filter display list by using the `findS KEYWORD` command,
       where `KEYWORD` must exist in at least 1 of the suppliers.
    2. Repeat the same test cases as 1.2 to 1.4.

### Editing an employee

1. Editing an employee while all employees are being shown.
    1. Prerequisites: List all employees using the `listE` command.
    2. Test case: `editE 1 n/Chetwin s/5000` <br>
       Expected: First employee in the list will be updated to have "Chetwin" and "5000" as his `name` and `salary` respectively. Details of the edited
       employee shown in the results display.
    3. Test case: `editE -1 e/chetwinlow@gmail.com` <br>
       Expected: Nobody is edited. Error details shown in the results display with a result message `Invalid comamnd
       format...`
    4. Test case: `editE 1` <br>
       Expected: Nobody is edited. Error details shown in the results display with a result message `At least one field to edit must be provided.`
    5. Other incorrect edit commands to try: `editE ` / `editE f/invalid prefix` / `edit 1` / `editE x n/john`(where x is larger than the employee
       list size) <br>
       Expected: Nobody is edited. Error details shown in the results display. Entering the first 2 commands will
       produce the same error message as test case 1.3 while entering `edit 1` will produce the error message
       `Unknown command` and the last one resulting in error message `The employee index provided is invalid`

2. Editing an employee while employee list is filtered.

    1. Prerequisites: Filter display list by using the `findE KEYWORD` command,
       where `KEYWORD` must exist in at least 1 of the employees.
    2. Repeat the same test cases as 1.2 to 1.4.

### Deleting a customer/employee/supplier/reservation

1. Deleting a customer/employee/supplier/reservation while all customers/employees/suppliers/reservation are being shown.

   1. Prerequisites: List all customers/employees/suppliers/reservations using the `listC`/`listE`/`listS`/`listR` command.
   2. Test case: `deleteC 1`/`deleteE 1`/`deleteS 1`/`deleteR 1` <br>
      Expected: First customer/employee/supplier/reservation is deleted from the list. Details of the deleted 
      customer/employee/supplier/reservation shown in the results display.
   3. Test case: `deleteC 0`/`deleteE -1`/`deleteS -50`/`deleteR 0`<br>
      Expected: Nobody is deleted. Error details shown in the results display with a result message `Invalid comamnd 
      format...`
   4. Other incorrect delete commands to try: `deleteS`, `deleteC x`, `delete 1` (where x is larger than the customer 
      list size) <br>
      Expected: Nobody is deleted. Error details shown in the results display. Entering the first command will 
      produce the same error message as test case 1.3, and `deleteC x` will result in `The customer index provided is invalid`, while entering `delete 1` will produce the error message 
      `Unknown command`
      
3. Deleting a customer/employee/supplier/reservation while customer/employee/supplier/reservation list is filtered.
   
   1. Prerequisites: Filter display list by using the `findC KEYWORD`/`findE KEYWORD`/`findS KEYWORD`/`check DATE` command,
      where `KEYWORD` must exist in at least 1 of the customers/employees/suppliers and there must be at least 1 reservation on `DATE`.
   2. Repeat the same test cases as 1.2 to 1.4.

### Finding customers

1. Finding the customers

   1. Test case: `findC Chetwin` <br>
       Expected: The customer list is filtered to only show customers that match "Chetwin"(case-insensitive) in any of its fields. Success message shown in results display.
   2. Test case: `findC Chetwin 15` <br>
      Expected: The customer list is filtered to only show customers that match "Chetwin"(case-insensitive) and "15" in any of its fields cumulatively. Success message shown in results display.
   3. Test case: `findC Chetwin 15 913045` <br>
      Expected: The customer list is filtered to only show customers that match "Chetwin"(case-insensitive) and "15" and "913045" in any of its fields cumulatively. Success message shown in results display.
   4. Test case: `findC ` <br>
      Expected: Customer list is not filtered. Error details shown in the result display, with a result message `Invalid command format`

### Finding suppliers

1. Finding the suppliers

    1. Test case: `findS Chetwin` <br>
       Expected: The supplier list is filtered to only show customers that match "Chetwin"(case-insensitive) in any of its fields. Success message shown in results display.
    2. Test case: `findS Chetwin 15` <br>
       Expected: The  list is filtered to only show suppliers that match "Chetwin"(case-insensitive) and "15" in any of its fields cumulatively. Success message shown in results display.
    3. Test case: `findS Chetwin 15 913045` <br>
       Expected: The supplier list is filtered to only show customers that match "Chetwin"(case-insensitive) and "15" and "913045" in any of its fields cumulatively. Success message shown in results display.
    4. Test case: `findS ` <br>
       Expected: Supplier list is not filtered. Error details shown in the result display, with a result message `Invalid command format`

### Finding employees

1. Finding the employees

    1. Test case: `findE Chetwin` <br>
       Expected: The employee list is filtered to only show customers that match "Chetwin"(case-insensitive) in any of its fields. Success message shown in results display.
    2. Test case: `findE Chetwin 15` <br>
       Expected: The employee list is filtered to only show suppliers that match "Chetwin 15"(case-insensitive) in any of its fields. Success message shown in results display.
    3. Test case: `findE Chetwin 15 913045` <br>
       Expected: The employee list is filtered to only show customers that match "Chetwin 15 913045" in any of its fields. Success message shown in results display.
    4. Test case: `findE ` <br>
       Expected: Supplier list is not filtered. Error details shown in the result display, with a result message `Invalid command format`


### Get the corresponding customer of a reservation

1. Getting the corresponding customer of a reservation while all reservations are shown
    
   1. Prerequisites: List all reservations using `listR`
   2. Test case: `getC 1`<br>
      Expected: The customer who made the first reservation is displayed.
   3. Test case: `getC 0`<br>
      Expected: No customer is shown. Error details are shown in the status message. Status bar remains the same.
   4. Other incorrect commands to try: `getC`, `getC -1`, `getC x` (where x is larger than the customer list size), `...` <br>
      Expected: Similar to previous.
      
1. Getting the corresponding customer of a reservation while reservation list is filtered.

    1. Prerequisites: Filter display reservation list using `check DATE`, where there must be at least 1 reservation 
       on `DATE`.
    2. Repeat the same test cases as 1.2 to 1.4. 

### Sorting the customer list

1. Sorting the customer list while all customers are being shown

    1. Prerequisites: List all customers using `listC` and use `resetC` to reset any previous sorting that you may 
       have applied. Note that customers are sorted by name in ascending order by default.
    2. Test case: `sortC by/n o/d` <br>
        Expected: The customer list is sorted by name in descending order. Success message shown in results display.
    3. Test case: `sortC by/lp o/a` <br>
       Expected: The customer list is sorted by loyalty points in ascending order. Success message shown in results display.
    4. Test case: `sortC by/n o/a by/lp` <br>
       Expected: The customer list is sorted by loyalty points in ascending order. Success message shown in results display.
    5. Test case: `sortC by/lp o/a o/d`
       Expected: The customer list is sorted by loyalty points in descending order. Success message shown in results display.
    6. Test case: `sortC by/lp o/n` <br>
       Expected: Customer list is not sorted. Error details shown in the result display, with a result message `Sorting order can only be either ascending or descending, and it should not be blank`
    7. Test case: `sortC by/dd o/d` <br>
       Expected: Customer list not sorted. Error details shown in the result display, with a result message `Sort by 
       can only be 1 of the customer fields...`
   
2. Sorting the customer list while the customer list is filtered.
   1. Prerequisites: Filter and display the customer list using `findC KEYWORDS`
   2. Test case: `sortC by/n o/d` when filtered customer list is `empty` <br>
      Expected: Customer list is not sorted. Error details shown in the result display, with a result message 
      `Customer list is currently empty!`
   3. Repeat the same test cases as 1.2 to 1.7. The filtered customer list will sort as per expected in test cases 1.2 to 1.7

### Sorting the employee list

1. Sorting the employee list while all employees are being shown

    1. Prerequisites: List all employees using `listE` and use `resetE` to reset any previous sorting that you may
       have applied. Note that employees are sorted by name in ascending order by default.
    2. Test case: `sortE by/sal o/a` <br>
       Expected: The employee list is sorted by salary in ascending order. Success message shown in results display.
    3. Test case: `sortE by/n o/d` <br>
       Expected: The employee list is sorted by name in descending order. Success message shown in results display.
    4. Test case: `sortE by/n o/a by/sal` <br>
       Expected: The employee list is sorted by salary in ascending order. Success message shown in results display.
    5. Test case: `sortE by/n o/a o/d`
       Expected: The employee list is sorted by name in descending order. Success message shown in results display.
    6. Test case: `sortE by/e o/n` <br>
       Expected: Employee list is not sorted. Error details shown in the result display, with a result message 
       `Sorting order can only be either ascending or descending, and it should not be blank`
    7. Test case: `sortE by/dd o/d` <br>
       Expected: Employee list not sorted. Error details shown in the result display, with a result message `Sort by
       can only be 1 of the employee fields...`

2. Sorting the employee list while the employee list is filtered.
    1. Prerequisites: Filter and display the employee list using `findE KEYWORDS`
    2. Test case: `sortE by/n o/d` when filtered employee list is `empty` <br>
       Expected: Employee list is not sorted. Error details shown in the result display, with a result message
       `Employee list is currently empty!`
    3. Repeat the same test cases as 1.2 to 1.7. The filtered employee list will sort as per expected in test cases 
       1.2 to 1.7

### Sorting the Supplier list

1. Sorting the supplier list while all suppliers are being shown

    1. Prerequisites: List all suppliers using `listS` and use `resetS` to reset any previous sorting that you may
       have applied. Note that suppliers are sorted by name in ascending order by default.
    2. Test case: `sortS by/dd o/d` <br>
       Expected: The supplier list is sorted by delivery details in descending order. Success message shown in results display.
    3. Test case: `sortS by/st o/a` <br>
       Expected: The supplier list is sorted by supply type in name order. Success message shown in results display.
    4. Test case: `sortS by/n o/a by/dd` <br>
       Expected: The supplier list is sorted by delivery details in ascending order. Success message shown in results display.
    5. Test case: `sortS by/n o/a o/d`
       Expected: The supplier list is sorted by name in descending order. Success message shown in results display.
    6. Test case: `sortS by/a o/n` <br>
       Expected: Supplier list is not sorted. Error details shown in the result display, with a result message
       `Sorting order can only be either ascending or descending, and it should not be blank`
    7. Test case: `sortS by/lp o/d` <br>
       Expected: Supplier list not sorted. Error details shown in the result display, with a result message `Sort by
       can only be 1 of the supplier fields...`

2. Sorting the supplier list while the supplier list is filtered.
    1. Prerequisites: Filter and display the supplier list using `findE KEYWORDS`
    2. Test case: `sortS by/dd o/a` when filtered supplier list is `empty` <br>
       Expected: Supplier list is not sorted. Error details shown in the result display, with a result message
       `Supplier list is currently empty!`
    3. Repeat the same test cases as 1.2 to 1.7. The filtered supplier list will sort as per expected in test cases
       1.2 to 1.7
