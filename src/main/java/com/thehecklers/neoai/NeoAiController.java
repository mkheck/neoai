package com.thehecklers.neoai;

import org.springframework.ai.client.Generation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
public class NeoAiController {
    private final RagService ragService;

    public NeoAiController(RagService ragService) {
        this.ragService = ragService;
    }

    @GetMapping
    public String index(Model model) {
        return "index";
    }

    @PostMapping("/aiui")
    public String aiui(@RequestParam(defaultValue = "type") String type,
                       @RequestParam(defaultValue = "location") String location,
                       Model model) {
        Generation gen = generate(type, location, 1);
        model.addAttribute("generation", gen.getText());
        gen = generate(type, location, 2);
        model.addAttribute("generationwithneo", gen.getText());
        return "index";
    }

    @GetMapping("/ai")
    @ResponseBody
    public Generation generate(@RequestParam(defaultValue = "type") String type,
                               @RequestParam(defaultValue = "location") String location,
                               int which) {
        HashMap<String, Object> prompts = new HashMap<>(Map.of("type", type, "location", location));
        if (which ==1) {
            return ragService.getIt(prompts);
        } else {
            return ragService.retrieve(prompts);
        }
    }
}
