import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;
import task.Task;
import task.ToDo;
import task.Deadline;
import task.Event;
import exception.EmptyDescriptionException;
import exception.UnknownCommandException;

public class Duke {
    private static final String FILE_PATH = "./data/duke.txt";
    private Storage storage;
    private ArrayList<Task> tasks;

    public Duke() {
        storage = new Storage(FILE_PATH);
        try {
            tasks = storage.load();
        } catch (IOException e) {
            System.out.println("Error loading tasks: " + e.getMessage());
            tasks = new ArrayList<>();
        }
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("____________________________________________________________");
        System.out.println(" Hello! I'm Duke");
        System.out.println(" What can I do for you?");
        System.out.println("____________________________________________________________");

        while (true) {
            try {
                String input = scanner.nextLine();

                if (input.equals("bye")) {
                    System.out.println("____________________________________________________________");
                    System.out.println(" Bye. Hope to see you again soon!");
                    System.out.println("____________________________________________________________");
                    break;
                } else if (input.equals("list")) {
                    System.out.println("____________________________________________________________");
                    System.out.println(" Here are the tasks in your list:");
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println(" " + (i + 1) + "." + tasks.get(i));
                    }
                    System.out.println("____________________________________________________________");
                } else if (input.startsWith("mark ")) {
                    int taskNumber = Integer.parseInt(input.split(" ")[1]) - 1;
                    tasks.get(taskNumber).markAsDone();
                    System.out.println("____________________________________________________________");
                    System.out.println(" Nice! I've marked this task as done:");
                    System.out.println(" " + tasks.get(taskNumber));
                    System.out.println("____________________________________________________________");
                    storage.save(tasks); // Save the updated tasks list
                } else if (input.startsWith("unmark ")) {
                    int taskNumber = Integer.parseInt(input.split(" ")[1]) - 1;
                    tasks.get(taskNumber).markAsNotDone();
                    System.out.println("____________________________________________________________");
                    System.out.println(" OK, I've marked this task as not done yet:");
                    System.out.println(" " + tasks.get(taskNumber));
                    System.out.println("____________________________________________________________");
                    storage.save(tasks); // Save the updated tasks list
                } else if (input.startsWith("delete ")) {
                    int taskNumber = Integer.parseInt(input.split(" ")[1]) - 1;
                    if (taskNumber < 0 || taskNumber >= tasks.size()) {
                        throw new IndexOutOfBoundsException("Invalid task number.");
                    }
                    Task removedTask = tasks.remove(taskNumber);
                    System.out.println("____________________________________________________________");
                    System.out.println(" Noted. I've removed this task:");
                    System.out.println("   " + removedTask);
                    System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                    System.out.println("____________________________________________________________");
                    storage.save(tasks); // Save the updated tasks list
                } else if (input.startsWith("todo ")) {
                    String taskDescription = input.substring(5).trim();
                    if (taskDescription.isEmpty()) {
                        throw new EmptyDescriptionException("todo");
                    }
                    tasks.add(new ToDo(taskDescription));
                    System.out.println("____________________________________________________________");
                    System.out.println(" Got it. I've added this task:");
                    System.out.println("   [T][ ] " + taskDescription);
                    System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                    System.out.println("____________________________________________________________");
                    storage.save(tasks); // Save the updated tasks list
                } else if (input.startsWith("deadline ")) {
                    String[] parts = input.substring(9).split(" /by ");
                    if (parts.length < 2 || parts[0].trim().isEmpty()) {
                        throw new EmptyDescriptionException("deadline");
                    }
                    String taskDescription = parts[0].trim();
                    String by = parts[1].trim();

                    try {
                        LocalDate byDate = LocalDate.parse(by);  // Assumes the date is provided in yyyy-MM-dd format
                        tasks.add(new Deadline(taskDescription, byDate));
                        System.out.println("____________________________________________________________");
                        System.out.println(" Got it. I've added this task:");
                        System.out.println("   [D][ ] " + taskDescription + " (by: " + byDate.format(DateTimeFormatter.ofPattern("MMM dd yyyy")) + ")");
                        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                        System.out.println("____________________________________________________________");
                        storage.save(tasks); // Save the updated tasks list
                    } catch (DateTimeParseException e) {
                        System.out.println("____________________________________________________________");
                        System.out.println("OOPS!!! The date format is incorrect. Please use yyyy-MM-dd format.");
                        System.out.println("____________________________________________________________");
                    }
                } else if (input.startsWith("event ")) {
                    String[] parts = input.substring(6).split(" /from | /to ");
                    if (parts.length < 3 || parts[0].trim().isEmpty()) {
                        throw new EmptyDescriptionException("event");
                    }
                    String taskDescription = parts[0].trim();
                    String from = parts[1];
                    String to = parts[2];
                    tasks.add(new Event(taskDescription, from, to));
                    System.out.println("____________________________________________________________");
                    System.out.println(" Got it. I've added this task:");
                    System.out.println("   [E][ ] " + taskDescription + " (from: " + from + " to: " + to + ")");
                    System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                    System.out.println("____________________________________________________________");
                    storage.save(tasks); // Save the updated tasks list
                } else {
                    throw new UnknownCommandException(input);
                }
            } catch (EmptyDescriptionException | UnknownCommandException e) {
                System.out.println("____________________________________________________________");
                System.out.println(e.getMessage());
                System.out.println("____________________________________________________________");
            } catch (NumberFormatException e) {
                System.out.println("____________________________________________________________");
                System.out.println("OOPS!!! The task number must be a valid integer.");
                System.out.println("____________________________________________________________");
            } catch (IndexOutOfBoundsException e) {
                System.out.println("____________________________________________________________");
                System.out.println("OOPS!!! The task number is out of range.");
                System.out.println("____________________________________________________________");
            } catch (IOException e) {
                System.out.println("____________________________________________________________");
                System.out.println("An error occurred while saving your tasks.");
                System.out.println("____________________________________________________________");
            }
        }

        scanner.close();
    }

    public static void main(String[] args) {
        new Duke().run();
    }
}