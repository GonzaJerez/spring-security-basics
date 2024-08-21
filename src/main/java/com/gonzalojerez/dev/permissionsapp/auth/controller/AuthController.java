package com.gonzalojerez.dev.permissionsapp.auth.controller;

import com.gonzalojerez.dev.permissionsapp.auth.dto.LoginDto;
import com.gonzalojerez.dev.permissionsapp.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/login")
    Map<String,Object> login(@ModelAttribute LoginDto body){
        return authService.login(body);
    }

    @PostMapping("/google")
    Map<String,Object> googleSignIn(@RequestBody Map<String,String> body){
        String token = body.get("token");
        return authService.googleSignIn(token);

    }

    @PostMapping("/github")
    Map<String,Object> githubSignIn(@RequestParam String code){
        return authService.githubSignIn(code);
    }


}
