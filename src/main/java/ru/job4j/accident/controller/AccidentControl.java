package ru.job4j.accident.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.repository.AccidentJdbcTemplate;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AccidentControl {
    private AccidentJdbcTemplate accidents;

    public AccidentControl(AccidentJdbcTemplate accidents) {
        this.accidents = accidents;
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("types", accidents.getTypes());
        model.addAttribute("rules", accidents.getRules());
        return "create";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Accident accident, HttpServletRequest req) {
        accidents.findAndSetType(accident);
        accidents.findAndSetRules(req.getParameterValues("rIds"), accident);
        accidents.saveAccident(accident);
        return "redirect:/";
    }

    @GetMapping("/edit")
    public String edit(Model model, @RequestParam int id) {
        model.addAttribute("accident", accidents.getAccidentById(id));
        model.addAttribute("types", accidents.getTypes());
        model.addAttribute("rules", accidents.getRules());
        return "edit";
    }
}
