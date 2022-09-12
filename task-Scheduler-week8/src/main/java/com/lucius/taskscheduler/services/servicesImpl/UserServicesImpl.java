package com.lucius.taskscheduler.services.servicesImpl;

import com.lucius.taskscheduler.dto.TaskDTO;
import com.lucius.taskscheduler.dto.UserDTO;
import com.lucius.taskscheduler.exception.TaskNotFoundException;
import com.lucius.taskscheduler.exception.UserNotFoundException;
import com.lucius.taskscheduler.model.Status;
import com.lucius.taskscheduler.model.Task;
import com.lucius.taskscheduler.model.User;
import com.lucius.taskscheduler.repository.TaskRepository;
import com.lucius.taskscheduler.repository.UserRepository;
import com.lucius.taskscheduler.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServicesImpl implements UserServices {
    private final UserRepository userRepository;

    private  final TaskRepository taskRepository;

    @Autowired
    public UserServicesImpl(UserRepository userRepository, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public User registerUser(UserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        return  userRepository.save(user);
    }

    @Override
    public String loginUser(String email, String password) {
        String message = "";
        User user = getUserByEmail(email);
        if (user.getPassword().equals(password)){
            message = "Success";
        }else {
            message = "incorrect password";
        }
        return message;
    }


    @Override
    public Task createTask(TaskDTO taskDTO) {
        Task task = new Task();
        User user = userRepository.findById(taskDTO.getUser_id()).get();
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setStatus(Status.PENDING);
        task.setUser(user);
        return taskRepository.save(task);
    }
    @Override
    public String forwardStatus(int id){
        String message = "";
        Task task = taskRepository.findById(id).get();
        if (task.getStatus() == Status.PENDING){
            task.setStatus(Status.IN_PROGRESS);
            taskRepository.save(task);
            message = "In Progress";
        }else if(task.getStatus()== Status.IN_PROGRESS){
            task.setStatus(Status.COMPLETED);
            task.setCompletedAt(LocalDateTime.now());
            taskRepository.save(task);
            message = "Completed";
        }else{
            message = "Not Authorized";
        }
        return message;
    }

    @Override
    public List<Task> showTaskByUser(Integer id) {
       return taskRepository.listOfTasksByUserId(id);
    }

    @Override
    public String backwardStatus(int id){
        String message = "";
        Task task = taskRepository.findById(id).get();
        if(task.getStatus() == Status.IN_PROGRESS){
            task.setStatus(Status.PENDING);
            taskRepository.save(task);
            message = "Now Pending";
        }else{
            message = "Unauthorized";
        }
        return message;
    }

    @Override
    public Task updateTitleAndDescription(TaskDTO taskDTO , int id) {
        Task task = getTaskById(id);
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        return taskRepository.save(task);
    }

    @Override
    public List<Task> viewAllTasks() {

        return taskRepository.findAll();
    }



    @Override
    public List<Task> viewAllTaskByStatus(String status) {
        return taskRepository.listOfTasksByStatus(status);
    }

    @Override
    public boolean deleteById(int id) {
        taskRepository.deleteById(id);
        return  true;
    }

    @Override
    public boolean updateTaskStatus(String status, int id){
        return taskRepository.updateTaskByIdAndStatus(status , id);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email + "not found in the database"));
    }

    @Override
    public Task getTaskById(int id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException( "Task not found in the database"));
    }
}
