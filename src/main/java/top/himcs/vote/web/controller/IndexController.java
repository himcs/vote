package top.himcs.vote.web.controller;

import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
public class IndexController {

    @RequestMapping("/")
    public String index(Locale locale, @CookieValue(value = "JSESSIONID",required = false)String cookie) {

        return cookie + "hello" + locale.toString();
    }

    @GetMapping("/pets/{petId}")
    public String findPet(@PathVariable String petId, @MatrixVariable int q) {
        return String.valueOf(q);
        // q == 11
    }
}
