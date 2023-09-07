package com.study.boardprojectadmin.controller;

import com.study.boardprojectadmin.dto.AdminAccountDto;
import com.study.boardprojectadmin.service.AdminAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/admin/members")
@Controller
@RequiredArgsConstructor
public class AdminAccountController {

    private final AdminAccountService adminAccountService;

    @GetMapping
    public String members() {
        return "admin/members";
    }

    @ResponseBody
    @GetMapping("/api/admin/members")
    public List<AdminAccountDto> getMember() {
        return List.of();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/api/admin/members/{userId}")
    public void delete(@PathVariable String userId) {

    }
}

