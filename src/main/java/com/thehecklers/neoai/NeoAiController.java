package com.thehecklers.neoai;

import org.springframework.ai.client.Generation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
        Generation gen = generate("dog", "Chicago");
        model.addAttribute("generation", gen.getText());
        return "index";
    }

    @GetMapping("/ai")
    @ResponseBody
    public Generation generate(@RequestParam(defaultValue = "pet") String pet,
                               @RequestParam(defaultValue = "location") String location) {
        HashMap<String, Object> prompts = new HashMap<>(Map.of("pet", pet, "location", location));
        return ragService.retrieve(prompts);
    }
}
