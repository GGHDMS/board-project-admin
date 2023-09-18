package com.study.boardprojectadmin.controller;

import com.study.boardprojectadmin.dto.response.AdminAccountResponse;
import com.study.boardprojectadmin.service.AdminAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class AdminAccountController {

    private final AdminAccountService adminAccountService;

    @GetMapping("/admin/members")
    public String members() {
        return "admin/members";
    }

    @ResponseBody
    @GetMapping("/api/admin/members")
    public List<AdminAccountResponse> getMember() {
        return adminAccountService.users().stream()
                .map(AdminAccountResponse::from)
                .collect(Collectors.toList());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/api/admin/members/{userId}")
    public void delete(@PathVariable String userId) {
        adminAccountService.deleteUser(userId);
    }
}

