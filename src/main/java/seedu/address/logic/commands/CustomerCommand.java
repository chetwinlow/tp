package seedu.address.logic.commands;

import seedu.address.model.Model;

/**
 * Switches to a the supplier view in the application.
 */
public class CustomerCommand extends Command {

    public static final String COMMAND_WORD = "customer";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Switches to customer view.\n"
            + "Example: " + COMMAND_WORD;

    public static final String SHOWING_SWITCH_MESSAGE = "Switched to Customer View.";

    @Override
    public CommandResult execute(Model model) {
        return new CommandResult(SHOWING_SWITCH_MESSAGE, false, false, true, false, false, false);
    }
}
