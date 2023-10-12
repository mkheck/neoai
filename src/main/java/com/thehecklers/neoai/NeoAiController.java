package com.thehecklers.neoai;

import org.springframework.ai.client.Generation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NeoAiController {
    private final RagService ragService;

    public NeoAiController(RagService ragService) {
        this.ragService = ragService;
    }

    @GetMapping("/ai")
    public Generation generate(@RequestParam(defaultValue = "Tell me a knock knock joke") String prompt) {
        return ragService.retrieve(prompt);
    }
}
