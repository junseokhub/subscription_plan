package com.subscription.plan.openai;

import com.openai.models.responses.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OpenAiController {

    private final OpenAiService openAiService;

    @PostMapping("text")
    public Response text(@RequestBody String text) {
        return openAiService.text(text);
    }
}
