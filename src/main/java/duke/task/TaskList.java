package duke.task;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TaskList {
    private ArrayList<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public Task getTask(int index) {
        return tasks.get(index);
    }

    public int getSize() {
        return tasks.size();
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void deleteTask(int index) {
        tasks.remove(index);
    }

    public void markTaskAsDone(int index) {
        tasks.get(index).markAsDone();
    }

    public void unmarkTaskAsDone(int index) {
        tasks.get(index).markAsNotDone();
    }

    public List<Task> getTasksList() {
        return new ArrayList<>(tasks); // Return a copy of the tasks list
    }

    public void printTasksOnDate(LocalDate date) {
        System.out.println("____________________________________________________________");
        System.out.println(" Here are the tasks occurring on " + date + ":");

        boolean hasTasks = false;
        for (Task task : tasks) {
            if (task instanceof Deadline) {
                Deadline deadline = (Deadline) task;
                if (deadline.getByDate().equals(date)) {
                    System.out.println(" " + task);
                    hasTasks = true;
                }
            }
        }

        if (!hasTasks) {
            System.out.println(" No tasks found on this date.");
        }
        System.out.println("____________________________________________________________");
    }

        public List<Task> findTasksByKeyword(String keyword) {
        List<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getDescription().contains(keyword)) {
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
    }
}