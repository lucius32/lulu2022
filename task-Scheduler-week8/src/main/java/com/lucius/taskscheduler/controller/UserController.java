package com.lucius.taskscheduler.controller;


import com.lucius.taskscheduler.dto.TaskDTO;
import com.lucius.taskscheduler.dto.UserDTO;
import com.lucius.taskscheduler.model.Task;
import com.lucius.taskscheduler.model.User;
import com.lucius.taskscheduler.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

@Controller
@RequestMapping(value = "/user")
public class UserController {
    private final UserServices userServices;


    @Autowired
    public UserController(UserServices userServices) {
        this.userServices = userServices;
    }

    @GetMapping("/dashboard")
    public String index(Model model, HttpSession session) {
//        List<Task> allTasks = userServices.viewAllTasks();
//        model.addAttribute("tasks" , allTasks);
//        session.setAttribute("tasks", allTasks);
        if(session.getAttribute("id") == null){
            return "redirect:/";
        }else {
            List<Task> allTasks = userServices.showTaskByUser((Integer) session.getAttribute("id"));
            model.addAttribute("tasks", allTasks);
            session.setAttribute("tasks", allTasks);
            model.addAttribute("today", Instant.now().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfWeek());
            return "dashboard";
        }
    }

    @GetMapping(value = "/login")
    public String displayLoginPage(Model model){
        model.addAttribute("userDetails" , new UserDTO());
        return "login";
    }

    @PostMapping(value = "/loginUser")
    public String loginUser(@RequestParam String email , @RequestParam String password , HttpSession session , Model model){
        String message =  userServices.loginUser(email ,  password);

        if(message.equals("Success")){
            User user = userServices.getUserByEmail(email);
            session.setAttribute("email" , user.getEmail());
            session.setAttribute("id" , user.getId());
            session.setAttribute("name" , user.getName());
            return "redirect:/user/dashboard";
        }else{
            model.addAttribute("errorMessage" , message);
            return  "redirect:/login";
        }

    }

    @GetMapping(value = "/register")
    public  String showRegistrationForm(Model model){
        model.addAttribute("userRegistrationDetails" , new UserDTO());
        return  "register";
    }

    @PostMapping(value = "/userRegistration")
    public String registration(@ModelAttribute UserDTO userDTO){

        User registeredUser = userServices.registerUser(userDTO);
        if (registeredUser != null){

            return "redirect:/user/login";
        }else {
            return "redirect:/register";
        }
    }

    @GetMapping(value = "/task/{status}")
    public String taskByStatus(@PathVariable(name = "status") String status , Model model){
        List<Task> listOfTaskByStatus = userServices.viewAllTaskByStatus(status);
        model.addAttribute("tasksByStatus" , listOfTaskByStatus);
        return "task-by-status";
    }

    @PostMapping("/delete/{id}")
    public String deleteTask(@PathVariable(name = "id") Integer id){
        userServices.deleteById(id);
        return "redirect:/user/dashboard";
    }
    @GetMapping("/viewTask/{id}")
    public String getSingleTask(@PathVariable(name = "id") Integer id, Model model){
        Task singleTask = userServices.getTaskById(id);
       model.addAttribute("singleTask",singleTask);

        return "viewTask";
    }

    @GetMapping(value = "/editPage/{id}")
    public String showEditPage(@PathVariable(name = "id") Integer id , Model model){
        Task task = userServices.getTaskById(id);
        model.addAttribute("singleTask" , task);
        model.addAttribute("taskBody", new TaskDTO());
        return  "editTask";
    }

    @PostMapping(value = "/edit/{id}")
    public String editTask(@PathVariable( name = "id") Integer id , @ModelAttribute TaskDTO taskDTO){
        userServices.updateTitleAndDescription(taskDTO , id);
        return "redirect:/user/dashboard";
    }

    @GetMapping(value = "/addNewTask")
    public String addTask(Model model){
        model.addAttribute("newTask" , new TaskDTO());
        return "addTask";
    }

    @PostMapping(value = "/addTask")
    public String CreateTask(@ModelAttribute TaskDTO taskDTO){
        userServices.createTask(taskDTO);
        return "redirect:/user/dashboard";
    }

    @GetMapping(value = "/arrow-right/{id}")
    public String moveStatusForward(@PathVariable(name = "id") int id){
        userServices.forwardStatus(id);
        return "redirect:/user/dashboard";
    }

    @GetMapping("/arrow-left/{id}")
    public String moveStatusBackward(@PathVariable(name = "id") int id){
        userServices.backwardStatus(id);
        return "redirect:/user/dashboard";
    }

}
