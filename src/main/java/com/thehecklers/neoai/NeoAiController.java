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
    public String aiui(@RequestParam(defaultValue = "pet") String pet,
                       @RequestParam(defaultValue = "location") String location,
                       Model model) {
        Generation gen = generate(pet, location);
        model.addAttribute("generation", gen.getText());
        return "index";
    }

    @GetMapping("/ai")
//    @PostMapping("/ai")
//    @RequestMapping(path = "/ai", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Generation generate(@RequestParam(defaultValue = "pet") String pet,
                               @RequestParam(defaultValue = "location") String location) {
        HashMap<String, Object> prompts = new HashMap<>(Map.of("pet", pet, "location", location));
        return ragService.retrieve(prompts);
    }
}
