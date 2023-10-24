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

    // UI (such as it is)
    @PostMapping("/aiui")
    public String aiui(@RequestParam(defaultValue = "type") String type,
                       @RequestParam(defaultValue = "location") String location,
                       Model model) {
        Generation gen = generate(type, location);
        model.addAttribute("generation", gen.getText());
        return "index";
    }

    // API
    @GetMapping("/ai")
    @ResponseBody
    public Generation generate(@RequestParam(defaultValue = "type") String type,
                               @RequestParam(defaultValue = "location") String location) {
        HashMap<String, Object> prompts = new HashMap<>(Map.of("type", type, "location", location));
        return ragService.retrieve(prompts);
    }
}
