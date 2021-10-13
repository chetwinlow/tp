package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_CUSTOMERS;
//import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import seedu.address.model.Model;

/**
 * Lists all customers in the address book to the user.
 */
public class ListCustomerCommand extends Command {

    public static final String COMMAND_WORD = "listcustomer";

    public static final String MESSAGE_SUCCESS = "Listed all customers";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredCustomerList(PREDICATE_SHOW_ALL_CUSTOMERS);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}