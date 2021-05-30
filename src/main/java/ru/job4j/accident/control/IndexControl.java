package ru.job4j.accident.control;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class IndexControl {

    @GetMapping("/")
    public String index(Model model) {
        List<String> users = List.of(
                "Ivan Petrov",
                "Sergei Ivanov",
                "Andrei Mikitin",
                "Ruslan Beliy",
                "Григорий Васильев"
        );
        model.addAttribute("users", users);
        return "index";
    }
}
