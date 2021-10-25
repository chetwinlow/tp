package seedu.address.model.table;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_DATETIME_PRINT_FORMAT;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import seedu.address.model.reservation.Reservation;
import seedu.address.model.reservation.exception.ReservationException;

public class TableManager {
    private static final String MESSAGE_RESTAURANT_FULL = "Restaurant is fully booked at %1$s";
    private static final String MESSAGE_TOO_MANY_PEOPLE = "No table can accommodate %1$d person(s) on ";
    private static final String MESSAGE_NO_TABLES_ADDED =
            "No tables exist. Set tables first before making reservations";

    private final TableList tables;

    /**
     * Constructs a new TableManager
     */
    public TableManager() {
        tables = new TableList();
    }

    public TableManager(TableList tables) {
        this.tables = tables;
    }

    public int getNumberOfTables() {
        return tables.getNumberOfTables();
    }

    public Table getAvailableTable(int numberOfPeople, List<Reservation> filteredReservations)
            throws ReservationException {

        // Check if tables have been added to table list
        if (tables.isEmpty()) {
            throw new ReservationException(MESSAGE_NO_TABLES_ADDED);
        }

        // If the number of reservations is already more than number of tables, throw exception
        if (filteredReservations.size() >= getNumberOfTables()) {
            throw new ReservationException(MESSAGE_RESTAURANT_FULL);
        }

        // Filter away tables with reservations already
        List<Table> availableTables = tables
                .asUnmodifiableObservableList()
                .stream()
                .filter(table -> filteredReservations
                        .stream()
                        .noneMatch(reservation -> table.getTableId() == reservation.getTableId()))
                .collect(Collectors.toList());

        // Filter away available tables that cannot fit the required number of
        availableTables.removeIf(table -> !table.canFit(numberOfPeople));

        // If no table can accommodate required number of people, throw exception
        if (availableTables.isEmpty()) {
            throw new ReservationException(String.format(MESSAGE_TOO_MANY_PEOPLE, numberOfPeople)
                    .concat(MESSAGE_DATETIME_PRINT_FORMAT));
        }

        // Return smallest table that can accommodate the required number of people
        return Collections.min(availableTables, Table::compareTableSize);
    }

    public void setTables(List<Table> tables) {
        this.tables.setTables(tables);
    }

    /**
     * Replaces the table {@code target} in the list with {@code editedTable}
     */
    public void setTable(Table target, Table editedTable) {
        requireNonNull(editedTable);
        tables.setTable(target, editedTable);
    }

    /**
     * Check if {@code table} exists in the database
     */
    public boolean hasTable(Table table) {
        requireNonNull(table);
        return tables.contains(table);
    }

    /**
     * Adds a new table to the list
     */
    public void addTable(Table table) {
        tables.add(table);
    }

    /**
     * Removes {@code key} from the database
     * {@code key} must exist in the list
     */
    public void removeTable(Table key) {
        tables.remove(key);
    }

    public void setTableList(List<Table> tables) {
        this.tables.setTables(tables);
    }

    public void resetTableCount() {
        Table.resetTableCount();
    }

    /**
     * Return the backing list as an unmodifiable {@code ObservableList}
     */
    public ObservableList<Table> getUnmodifiableObservableList() {
        return tables.asUnmodifiableObservableList();
    }
    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TableManager // instanceof handles nulls
                && tables.equals(((TableManager) other).tables));
    }

}