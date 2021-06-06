package ru.job4j.accident.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.AccidentType;
import ru.job4j.accident.repository.AccidentMem;

@Controller
public class AccidentControl {
    private AccidentMem accidents;

    public AccidentControl(AccidentMem accidents) {
        this.accidents = accidents;
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("types", accidents.getTypes().values());
        return "create";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Accident accident) {
        AccidentType type = accident.getType();
        type.setName(accidents.getTypeById(type.getId()).getName());
        accidents.saveAccident(accident);
        return "redirect:/";
    }

    @GetMapping("/edit")
    public String edit(Model model, @RequestParam int id) {
        model.addAttribute("accident", accidents.getAccidentById(id));
        model.addAttribute("types", accidents.getTypes().values());
        return "edit";
    }
}
