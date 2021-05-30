package ru.job4j.accident.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.repository.AccidentMem;

import java.util.List;

@Controller
public class IndexControl {

    @GetMapping("/")
    public String index(Model model) {
        List<Accident> accidents = List.of(
                Accident.of("BMW and Lada", "Lada pumped on BMW at a relatively slow speed", "Lenina 50"),
                Accident.of("Hyundai scratched", "Hyundai i40 got scratched by bycicle passing by", "Beregovaya 12"),
                Accident.of("Bus crached", "City bus fall over on the central crossroad, no injured", "Sadovaya 1")
        );
        AccidentMem repository = new AccidentMem();
        accidents.forEach(repository::addAccident);
        model.addAttribute("accidents", repository.getAccidents().values());
        return "index";
    }
}
