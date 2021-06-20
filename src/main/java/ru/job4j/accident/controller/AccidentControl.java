package ru.job4j.accident.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.repository.AccidentRepository;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AccidentControl {
    private AccidentRepository accidents;

    public AccidentControl(AccidentRepository accidents) {
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
//        accidents.findAndSetType(accident);
        accidents.findAndSetRules(req.getParameterValues("rIds"), accident);
        accidents.save(accident);
        return "redirect:/";
    }

    @GetMapping("/edit")
    public String edit(Model model, @RequestParam int id) {
        model.addAttribute("accident", accidents.findById(id).orElse(null));
        model.addAttribute("types", accidents.getTypes());
        model.addAttribute("rules", accidents.getRules());
        return "edit";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam int id) {
        Accident toDelete = new Accident();
        toDelete.setId(id);
        accidents.delete(toDelete);
        return "redirect:/";
    }
}