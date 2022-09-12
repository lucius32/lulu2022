package com.lucius.taskscheduler.services;

import com.lucius.taskscheduler.dto.TaskDTO;
import com.lucius.taskscheduler.dto.UserDTO;
import com.lucius.taskscheduler.model.Task;
import com.lucius.taskscheduler.model.User;

import java.util.List;

public interface UserServices {
    User registerUser(UserDTO userDTO);

    String loginUser(String email, String password);

    Task createTask(TaskDTO taskDTO);

    String backwardStatus(int id);

    Task updateTitleAndDescription(TaskDTO taskDTO , int id);

    Task getTaskById(int id);

    List<Task> viewAllTasks();

    boolean updateTaskStatus(String status, int id);

    List<Task> viewAllTaskByStatus(String status);

    boolean deleteById(int id);
    User getUserByEmail(String email);
    String forwardStatus(int id);

    List<Task> showTaskByUser(Integer id);
}
