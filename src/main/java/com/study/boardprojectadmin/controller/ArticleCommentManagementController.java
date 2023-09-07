package com.study.boardprojectadmin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/management/article-comments")
@Controller
public class ArticleCommentManagementController {

    @GetMapping
    public String articleComments(Model model) {
        return "management/article-comments";
    }
}
