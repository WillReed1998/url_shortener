package cc.worldmandia.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class ApiRestController {

    @GetMapping("/test")
    public String test() {
        return "yrt";
    }
}
