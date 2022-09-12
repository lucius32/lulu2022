package com.lucius.taskscheduler.servicesImpl;

import com.lucius.taskscheduler.dto.TaskDTO;
import com.lucius.taskscheduler.dto.UserDTO;
import com.lucius.taskscheduler.model.Status;
import com.lucius.taskscheduler.model.Task;
import com.lucius.taskscheduler.model.User;
import com.lucius.taskscheduler.repository.TaskRepository;
import com.lucius.taskscheduler.repository.UserRepository;
import com.lucius.taskscheduler.services.servicesImpl.UserServicesImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.time.Month.AUGUST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServicesImplTest {
    @Mock
    UserRepository userRepository;

    @Mock
    TaskRepository taskRepository;

    @InjectMocks
    UserServicesImpl userServiceImpl;

    private User user;
    private UserDTO userDTO;
    private TaskDTO taskDTO;
    private Task task;
    private LocalDateTime time;
    List<Task> taskList;
    @BeforeEach
    void setUp() {
        time = LocalDateTime.of(2022, AUGUST,3,6,30,40,50000);
        taskList = new ArrayList<>();
        taskList.add(task);
        this.user = new User(1, "Vincent" , "enwerevincent@gmail.com" , "12345" , taskList);
        this.task = new Task(1, "Write Code" , "Code till 7am" , Status.PENDING , time , time , time , user);
        this.taskDTO = new TaskDTO("Write Code","Code till 7am", 1 );
        this.userDTO = new UserDTO("vincent" , "enwerevincent@gmail.com", "12345");
        when(userRepository.save(user)).thenReturn(user);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskRepository.findAll()).thenReturn(taskList);
        when(taskRepository.listOfTasksByStatus("pending")).thenReturn(taskList);
        // when(taskRepository.(1)).thenReturn(Optional.ofNullable())
        when(userRepository.findById(1)).thenReturn(Optional.ofNullable(user));
        when(taskRepository.findById(1)).thenReturn(Optional.ofNullable(task));
        when(userRepository.findUserByEmail("enwerevincent@gmail.com")).thenReturn(Optional.of(user));
        when(taskRepository.updateTaskByIdAndStatus("ongoing" , 1)).thenReturn(true);

    }

    @Test
    void registerUser() {
        when(userServiceImpl.registerUser(userDTO)).thenReturn(user);
        var actual = userServiceImpl.registerUser(userDTO);
        var expected = user;
        assertEquals( expected , actual );
    }

    @Test
    void loginUser_Successfull() {
        String message = "Success";
        assertEquals(message , userServiceImpl.loginUser("enwerevincent@gmail.com" , "12345"));
    }

    @Test
    void loginUser_Unsuccessfull() {
        String message = "incorrect password";
        assertEquals(message , userServiceImpl.loginUser("enwerevincent@gmail.com" , "1234"));
    }


    @Test
    void createTask() {
        when(userServiceImpl.createTask(taskDTO)).thenReturn(task);
        assertEquals(task , userServiceImpl.createTask(taskDTO));
    }

    @Test
    void updateTitleAndDescription() {
        assertEquals(task , userServiceImpl.updateTitleAndDescription(taskDTO , 1));
    }

    @Test
    void viewAllTasks() {
        assertEquals(1 , userServiceImpl.viewAllTasks().size());
    }

    @Test
    void viewAllTaskByStatus() {

        assertEquals(taskList , userServiceImpl.viewAllTaskByStatus("pending"));
    }

//    @Test
//    void deleteById() {
//        when(userServiceImpl.deleteById(1)).thenReturn(true);
//        assertTrue(userServiceImpl.deleteById(1));
//    }

    @Test
    void updateTaskStatus() {
        assertTrue(userServiceImpl.updateTaskStatus("ongoing" , 1));
    }

    @Test
    void getUserByEmail() {
        assertEquals(user , userServiceImpl.getUserByEmail("enwerevincent@gmail.com"));
    }

    @Test
    void getTaskById() {
        assertEquals(task, userServiceImpl.getTaskById(1));
    }
}
