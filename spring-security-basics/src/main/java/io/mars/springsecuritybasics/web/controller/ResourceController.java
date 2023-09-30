package io.mars.springsecuritybasics.web.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/resource/")
public class ResourceController{

    @GetMapping("all")
    public List<String> all(){
        return List.of("this", "is", "all", "of", "it");
    }

    @GetMapping("some")
    public List<String> some(){
        return List.of("well", "some", "defined", "miss");
    }

    @GetMapping("hse")
    public String hse(Authentication dto){
        SecurityContextHolder.getContext().getAuthentication();
        return "hse";
    }

}
