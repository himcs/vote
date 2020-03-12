package top.himcs.vote.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import top.himcs.vote.model.Person;

@RestController
@RequestMapping("/persons")
public class PersonController {
    @GetMapping("/test")
    @ResponseStatus(HttpStatus.CREATED)
    public String  test(){
        return "test";
    }

    @GetMapping("/id1/{id}")
    public Person getPerson(@PathVariable Long id) {
        Person p = new Person();
        p.setName(id.toString());
        return p;
    }

    @PostMapping(path = "/add",consumes = MediaType.APPLICATION_JSON_VALUE )
    public void add(){

    }
}
