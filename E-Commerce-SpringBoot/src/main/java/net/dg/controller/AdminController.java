package net.dg.controller;

import lombok.AllArgsConstructor;
import net.dg.model.User;
import net.dg.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@AllArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final String REDIRECT_ADMIN_USER = "redirect:/admin/users";

    private final UserService userService;

    @GetMapping("")
    public String adminHome() {
        return "/admin/admin_home";
    }

    @GetMapping("/users")
    public String index(Model model) {
        model.addAttribute("users", userService.findAll());
        return "/admin/users";
    }

    @GetMapping("/users/delete")
    public String removeUser(@RequestParam("userId") Long userId) throws Exception {
        Optional<User> userFromDB = userService.findById(userId);
        User user = userFromDB.orElseThrow(Exception::new);
        userService.delete(user);
        return REDIRECT_ADMIN_USER;
    }

    @GetMapping("/users/block")
    public String blockUser(@RequestParam("userId") Long userId) {
        userService.blockUser(userId);
        return REDIRECT_ADMIN_USER;
    }

    @GetMapping("/users/unblock")
    public String unBlockUser(@RequestParam("userId") Long userId) {
        userService.unblockUser(userId);
        return REDIRECT_ADMIN_USER;
    }

    @GetMapping("/account")
    public String editOwnAccount(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "/admin/admin_account";
    }

    @PostMapping("/account/{userId}")
    public String updateUserInfo(@AuthenticationPrincipal User user,
                                 @RequestParam String firstName,
                                 @RequestParam String lastName,
                                 @RequestParam String password,
                                 @RequestParam String city,
                                 @RequestParam String street,
                                 @RequestParam String streetNumber,
                                 @RequestParam String phoneNumber) {

        userService.updateUser(user, firstName, lastName, password, city, street, streetNumber, phoneNumber);
        return "redirect:/admin";
    }

}
