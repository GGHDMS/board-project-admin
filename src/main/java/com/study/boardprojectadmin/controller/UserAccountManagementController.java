package com.study.boardprojectadmin.controller;
import com.study.boardprojectadmin.dto.response.UserAccountResponse;
import com.study.boardprojectadmin.service.UserAccountManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RequestMapping("/management/user-accounts")
@Controller
@RequiredArgsConstructor
public class UserAccountManagementController {

    private final UserAccountManagementService userAccountManagementService;

    @GetMapping
    public String userAccounts(Model model) {
        model.addAttribute("userAccounts",
                userAccountManagementService.getUserAccounts().stream().map(UserAccountResponse::from).collect(Collectors.toList()));
        return "management/user-accounts";
    }

    @GetMapping("/{userId}")
    @ResponseBody
    public UserAccountResponse userAccount(@PathVariable String userId) {
        return UserAccountResponse.from(userAccountManagementService.getUserAccount(userId));
    }

    @PostMapping("/{userId}")
    public String deleteUserAccount(@PathVariable String userId) {
        userAccountManagementService.deleteUserAccount(userId);

        return "redirect:/management/user-accounts";
    }
}
