package com.tony.cars.api;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class IndexController {

    @GetMapping("login")
    public String loginWithQuery(@RequestParam String signIn, @RequestParam String pass) {
        return "QUERY: Login: " + signIn + " | Senha: " + pass;
    }

    @GetMapping("/sign-in/{login}/pass/{password}")
    public String loginWithPath(@PathVariable String login, @PathVariable String password) {
        return "PATH: Login: " + login + " | Senha: " + password;
    }

    @PostMapping("/login")
    public String loginWithPathAndPost(@RequestParam String signIn, @RequestParam String pass) {
        return "PATH com POST: Login: " + signIn + " | Senha: " + pass;
    }
}
