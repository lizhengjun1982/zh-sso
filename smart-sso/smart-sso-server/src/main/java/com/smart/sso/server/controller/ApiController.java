package com.smart.sso.server.controller;

import com.smart.sso.server.model.User;
import com.smart.sso.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private UserService userService;

    @RequestMapping("/getUser")
    User getUser(@RequestParam("userId") int userId) {
        return userService.findById(userId);
    }

}
