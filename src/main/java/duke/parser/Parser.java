package duke.parser;

import duke.command.*;
import duke.exception.EmptyDescriptionException;
import duke.exception.UnknownCommandException;
import duke.task.Deadline;
import duke.task.Event;
import duke.task.ToDo;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Parser {

    public static Command parse(String input) throws EmptyDescriptionException, UnknownCommandException {
        String[] words = input.split(" ", 2);
        String command = words[0];

        switch (command) {
            case "bye":
                return new ExitCommand();
            case "list":
                return new ListCommand();
            case "mark":
                return new MarkCommand(Integer.parseInt(words[1]) - 1);
            case "unmark":
                return new UnmarkCommand(Integer.parseInt(words[1]) - 1);
            case "delete":
                return new DeleteCommand(Integer.parseInt(words[1]) - 1);
            case "todo":
                if (words.length < 2 || words[1].trim().isEmpty()) {
                    throw new EmptyDescriptionException("todo");
                }
                return new AddCommand(new ToDo(words[1].trim()));
            case "deadline":
                if (words.length < 2 || words[1].trim().isEmpty()) {
                    throw new EmptyDescriptionException("deadline");
                }
                String[] deadlineParts = words[1].split(" /by ");
                if (deadlineParts.length < 2 || deadlineParts[0].trim().isEmpty()) {
                    throw new EmptyDescriptionException("deadline");
                }
                try {
                    LocalDate byDate = LocalDate.parse(deadlineParts[1].trim());
                    return new AddCommand(new Deadline(deadlineParts[0].trim(), byDate));
                } catch (DateTimeParseException e) {
                    throw new UnknownCommandException("The date format is incorrect. Please use yyyy-MM-dd format.");
                }
            case "event":
                if (words.length < 2 || words[1].trim().isEmpty()) {
                    throw new EmptyDescriptionException("event");
                }
                String[] eventParts = words[1].split(" /from | /to ");
                if (eventParts.length < 3 || eventParts[0].trim().isEmpty()) {
                    throw new EmptyDescriptionException("event");
                }
                return new AddCommand(new Event(eventParts[0].trim(), eventParts[1].trim(), eventParts[2].trim()));
            case "find":
                if (words.length < 2 || words[1].trim().isEmpty()) {
                    throw new EmptyDescriptionException("find");
                }
                return new FindCommand(words[1].trim());
            case "show":
                if (words.length < 2 || !words[1].startsWith("on ")) {
                    throw new EmptyDescriptionException("show on");
                }
                String dateString = words[1].substring(3).trim(); // Extract the date after "on"
                try {
                    LocalDate date = LocalDate.parse(dateString);
                    return new ShowOnDateCommand(date);
                } catch (DateTimeParseException e) {
                    throw new UnknownCommandException("The date format is incorrect. Please use yyyy-MM-dd format.");
                }

            default:
                throw new UnknownCommandException("I'm sorry, but I don't know what that means.");
        }
    }
}