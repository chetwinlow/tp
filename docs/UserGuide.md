---
layout: page
title: User Guide
---

Restaurant HR Helper (RHRH) is a **desktop app for managing restaurant contacts including employees, suppliers and customers, optimized for use via a Command Line Interface** (CLI) while still having the benefits of a Graphical User Interface (GUI). If you can type fast, RHRH can get your contact management tasks done faster than traditional GUI apps.

* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `11` or above installed in your Computer.

2. Download the latest `rhrh.jar` from [here]().

3. Copy the file to the folder you want to use as the _home folder_ for your RHRH.

4. Double-click the file to start the app. The GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)

5. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

   * `add customer` : Add a customer
     * Format: `add customer n/NAME p/PHONE_NUMBER e/EMAIL`
     * Example: `add customer n/John Doe p/87654321 e/e12345@u.nus.edu`
     * Note: customer specific question prompts will be triggered

6. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `add customer n/NAME`, `NAME` is a parameter which can be used as `add n/John Doe`.

* Items in square brackets are optional.<br>
  e.g `n/NAME [t/TAG]` can be used as `n/John Doe t/friend` or as `n/John Doe`.

* Items with `…`​ after them can be used multiple times including zero times.<br>
  e.g. `[t/TAG]…​` can be used as ` ` (i.e. 0 times), `t/friend`, `t/friend t/family` etc.

* Parameters can be in any order.<br>
  e.g. if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

* If a parameter is expected only once in the command but you specified it multiple times, only the last occurrence of the parameter will be taken.<br>
  e.g. if you specify `p/12341234 p/56785678`, only `p/56785678` will be taken.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) will be ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.

</div>

### Viewing help [COMING SOON]: `help`

Shows a message explaining how to access the help page.

![help message]()

Format: `help`


### Adding a customer: `add customer`

Adds a customer to RHRH.

Format: `add customer n/NAME p/PHONE_NUMBER e/EMAIL`

Examples: `add customer n/John Doe p/87654321 e/e12345@u.nus.edu`

Note: customer specific question prompts will be triggered

### Adding an employee: `add employee`

Adds an employee to RHRH.

Format: `add employee n/NAME p/PHONE_NUMBER e/EMAIL`

Examples: `add employee n/John Doe p/87654321 e/e12345@u.nus.edu`

Note: employee specific question prompts will be triggered

### Adding a supplier: `add supplier`

Adds a supplier to RHRH.

Format: `add supplier n/NAME p/PHONE_NUMBER e/EMAIL`

Examples: `add supplier n/John Doe p/87654321 e/e12345@u.nus.edu`

Note: supplier specific question prompts will be triggered

### Search a reservation's availability: `find -r`

Search a reservation's availability using the given date/time

Format:
* `find -r d/DATE t/TIME`
* `find -r t/TIME`
* `find -r d/DATE`

Examples:
* `find -r 2021-09-19 18:00`
* `find -r 18:00`
* `find -r 2021-09-19`

### Create a reservation: `reserve` 

Format: `reserve d/DATE t/TIME  n/NAME p/PHONE_NUMBER`

Examples: `reserve d/2021-09-19 t/1800 n/John Doe p/87654321`

Note: System will reject if time slot is not available

---

## These features will be coming soon!

### Listing all persons : `list`

Shows a list of all persons in RHRH.

Format: `list`

### Editing a person : `edit`

Edits an existing person in RHRH.

Format: `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [t/TAG]…​`

* Edits the person at the specified `INDEX`. The index refers to the index number shown in the displayed person list. The index **must be a positive integer** 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* When editing tags, the existing tags of the person will be removed i.e adding of tags is not cumulative.
* You can remove all the person’s tags by typing `t/` without
    specifying any tags after it.

Examples:
*  `edit 1 p/91234567 e/johndoe@example.com` Edits the phone number and email address of the 1st person to be `91234567` and `johndoe@example.com` respectively.
*  `edit 2 n/Betsy Crower t/` Edits the name of the 2nd person to be `Betsy Crower` and clears all existing tags.

### Locating persons by name: `find`

Finds persons whose names contain any of the given keywords.

Format: `find KEYWORD [MORE_KEYWORDS]`

* The search is case-insensitive. e.g `hans` will match `Hans`
* The order of the keywords does not matter. e.g. `Hans Bo` will match `Bo Hans`
* Only the name is searched.
* Only full words will be matched e.g. `Han` will not match `Hans`
* Persons matching at least one keyword will be returned (i.e. `OR` search).
  e.g. `Hans Bo` will return `Hans Gruber`, `Bo Yang`

Examples:
* `find John` returns `john` and `John Doe`
* `find alex david` returns `Alex Yeoh`, `David Li`<br>

### Deleting a person : `delete`

Deletes the specified person from RHRH.

Format: `delete INDEX`

* Deletes the person at the specified `INDEX`.
* The index refers to the index number shown in the displayed person list.
* The index **must be a positive integer** 1, 2, 3, …​

Examples:
* `list` followed by `delete 2` deletes the 2nd person in the address book.
* `find Betsy` followed by `delete 1` deletes the 1st person in the results of the `find` command.

### Clearing all entries : `clear`

Clears all entries from RHRH.

Format: `clear`

### Exiting the program : `exit`

Exits the program.

Format: `exit`

### Saving the data

RHRH data are saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

### Editing the data file

RHRH data are saved as a JSON file `[JAR file location]/data/rhrh.json`. Advanced users are welcome to update data directly by editing that data file.

<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**
If your changes to the data file makes its format invalid, RHRH will discard all data and start with an empty data file at the next run.
</div>

### Archiving data files `[coming in v2.0]`

_Details coming soon ..._

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous RHRH home folder.

--------------------------------------------------------------------------------------------------------------------

## Command summary

Action | Format, Examples
--------|------------------
**Add Customer** | `add customer n/NAME p/PHONE_NUMBER e/EMAIL` <br> e.g. `add customer n/John Doe p/87654321 e/e12345@u.nus.edu`
**Add Employee** | `add employee n/NAME p/PHONE_NUMBER e/EMAIL` <br> e.g. `add employee n/John Doe p/87654321 e/e12345@u.nus.edu`
**Add supplier** | `add supplier n/NAME p/PHONE_NUMBER e/EMAIL` <br> e.g. `add supplier n/John Doe p/87654321 e/e12345@u.nus.edu`
**Check a reservation availability** | `find -r d/DATE t/TIME`, `find -r t/TIME`, `find -r d/DATE` <br> e.g. `find -r 2021-09-19 18:00`, `find -r 18:00` `find -r 2021-09-19`
**Create reservation** | `reserve d/DATE t/TIME  n/NAME p/PHONE_NUMBER` <br> e.g. `reserve d/2021-09-19 t/1800 n/John Doe p/87654321`
**Clear [COMING SOON]** | `clear`
**Delete [COMING SOON]** | `delete INDEX`<br> e.g., `delete 3`
**Edit [COMING SOON]** | `edit INDEX [n/NAME] [p/PHONE_NUMBER] [e/EMAIL] [a/ADDRESS] [t/TAG]…​`<br> e.g.,`edit 2 n/James Lee e/jameslee@example.com`
**Find [COMING SOON]** | `find KEYWORD [MORE_KEYWORDS]`<br> e.g., `find James Jake`
**List [COMING SOON]** | `list`
**Help [COMING SOON]** | `help`
