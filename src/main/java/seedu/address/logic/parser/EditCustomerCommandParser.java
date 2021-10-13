package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ALLERGIES;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LP;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SPECIALREQUESTS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCustomerCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.customer.Allergy;
import seedu.address.model.person.customer.SpecialRequest;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditCustomerCommandParser implements Parser<EditCustomerCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditCustomerCommand
     * and returns an EditCustomerCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditCustomerCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS,
                        PREFIX_LP, PREFIX_ALLERGIES, PREFIX_SPECIALREQUESTS, PREFIX_TAG);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    EditCustomerCommand.MESSAGE_USAGE), pe);
        }

        EditCustomerCommand.EditCustomerDescriptor editCustomerDescriptor =
                new EditCustomerCommand.EditCustomerDescriptor();
        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            editCustomerDescriptor.setName(ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()));
        }
        if (argMultimap.getValue(PREFIX_PHONE).isPresent()) {
            editCustomerDescriptor.setPhone(ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get()));
        }
        if (argMultimap.getValue(PREFIX_EMAIL).isPresent()) {
            editCustomerDescriptor.setEmail(ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get()));
        }
        if (argMultimap.getValue(PREFIX_ADDRESS).isPresent()) {
            editCustomerDescriptor.setAddress(ParserUtil
                    .parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get()));
        }
        if (argMultimap.getValue(PREFIX_LP).isPresent()) {
            editCustomerDescriptor.setLoyaltyPoints(ParserUtil
                    .parseLoyaltyPoints(argMultimap.getValue(PREFIX_LP).get()));
        }
        parseAllergiesForEdit(argMultimap.getAllValues(PREFIX_ALLERGIES))
                .ifPresent(editCustomerDescriptor::setAllergies);
        parseSpecialRequestsForEdit(argMultimap.getAllValues(PREFIX_SPECIALREQUESTS))
                .ifPresent(editCustomerDescriptor::setSpecialRequests);
        parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG)).ifPresent(editCustomerDescriptor::setTags);

        if (!editCustomerDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCustomerCommand.MESSAGE_NOT_EDITED);
        }

        return new EditCustomerCommand(index, editCustomerDescriptor);
    }

    /**
     * Parses {@code Collection<String> allergies} into a {@code Set<Allergy>} if {@code Allergys} is non-empty.
     * If {@code Allergys} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Allergy>} containing zero Allergys.
     */
    private Optional<Set<Allergy>> parseAllergiesForEdit(Collection<String> allergies) throws ParseException {
        assert allergies != null;

        if (allergies.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> allergySet = allergies.size() == 1 && allergies.contains("")
                ? Collections.emptySet() : allergies;
        return Optional.of(ParserUtil.parseAllergies(allergySet));
    }

    /**
     * Parses {@code Collection<String> specialRequests} into a {@code Set<SpecialRequest>}
     * if {@code SpecialRequests} is non-empty.
     * If {@code SpecialRequests} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<SpecialRequest>} containing zero SpecialRequests.
     */
    private Optional<Set<SpecialRequest>> parseSpecialRequestsForEdit(Collection<String> specialRequests)
            throws ParseException {
        assert specialRequests != null;

        if (specialRequests.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> specialRequestSet = specialRequests.size() == 1 && specialRequests.contains("")
                ? Collections.emptySet() : specialRequests;
        return Optional.of(ParserUtil.parseSpecialRequests(specialRequestSet));
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * If {@code tags} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Tag>} containing zero tags.
     */
    private Optional<Set<Tag>> parseTagsForEdit(Collection<String> tags) throws ParseException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagSet = tags.size() == 1 && tags.contains("") ? Collections.emptySet() : tags;
        return Optional.of(ParserUtil.parseTags(tagSet));
    }

}