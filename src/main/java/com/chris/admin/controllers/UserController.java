package com.chris.admin.controllers;

import java.security.Principal;
import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.chris.admin.models.User;
import com.chris.admin.services.UserService;
import com.chris.admin.validator.UserValidator;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class UserController {
	
	private UserService userService;
	private UserValidator userValidator;
	
	public UserController(UserService userService, UserValidator userValidator) {
		this.userService = userService;
		this.userValidator = userValidator;
	}

	
	@RequestMapping("/register")
	public String registerForm(@Valid @ModelAttribute("user") User user) {
		return "registrationPage.jsp";
	}
	
	@PostMapping("/register")
	public String registration(@Valid @ModelAttribute("user") User user, BindingResult result, Model model, HttpSession session) {
		userValidator.validate(user, result);
		if(result.hasErrors()) {
			return "registrationPage.jsp";
		}
		userService.saveWithUserRole(user);
		return "redirect:/login";
	}
	
	@RequestMapping("/admin/{id}")
	public String makeAdmin(Principal principal, @PathVariable("id") Long id, Model model) {
		if(principal == null) {
			return "redirect:/login";
		}
		User user = userService.findById(id);
		userService.upgradeUser(user);
		
		model.addAttribute("users", userService.allUsers());
		return "redirect:/home";
	}
	
    @RequestMapping("/login")
    public String login(@RequestParam(value="error", required=false) String error, @RequestParam(value="logout", required=false) String logout, Model model) {
        if(error != null) {
            model.addAttribute("errorMessage", "Invalid Credentials, Please try again.");
        }
        if(logout != null) {
            model.addAttribute("logoutMessage", "Logout Successful!");
        }
        return "loginPage.jsp";
    }
    @RequestMapping(value = {"/", "/home"})
    public String home(Principal principal, Model model) {
    	if(principal == null) {
    		return "redirect:/login";
    	}
        String username = principal.getName();
        User user = userService.findByUsername(username);
        model.addAttribute("user", user);
        
        if(user != null) {
        	user.setLastLogin(new Date());
        	userService.updateUser(user);
        
        if(user.getRoles().get(0).getName().contains("ROLE_SUPER_ADMIN")||user.getRoles().get(0).getName().contains("ROLE_ADMIN")) {
			model.addAttribute("currentUser", userService.findByUsername(username));
			model.addAttribute("users", userService.allUsers());
			return "adminPage.jsp";
        }
    }
    return "homePage.jsp";
    }
    
	@RequestMapping("/users/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
	
    @RequestMapping("/admin")
    public String adminPage(Principal principal, Model model) {
        String username = principal.getName();
        model.addAttribute("currentUser", userService.findByUsername(username));
        return "adminPage.jsp";
    }
    
    @RequestMapping("/delete/{id}")
    public String deleteUser(Principal principal, @PathVariable("id") Long id, HttpSession session, Model model) {
    	
    	if(principal == null) {
    		return "redirect:/login";
    	}
    	
    	User user = userService.findById(id);
    	userService.deleteUser(user);
    	
    	model.addAttribute("users", userService.allUsers());
    	
    	return "redirect:/home";
    	}
}
