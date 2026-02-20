package com.subscription.plan.openai;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SpringAiService {

    private final ChatModel chatModel;
    private final EmbeddingModel embeddingModel;

    public void test(String text) {
        SystemMessage systemMessage = new SystemMessage("반말로 대답하고 한국말로 대답해");
        UserMessage userMessage = new UserMessage(text);
//        AssistantMessage assistantMessage = new AssistantMessage("Hello, I am a helpful assistant.");

        List<Message> messages = List.of(systemMessage, userMessage);

        ChatResponse response = chatModel.call(
                new Prompt(
                        messages,
                        OpenAiChatOptions.builder()
                                .model("gpt-4o")
                                .build()
                )
        );
        System.out.println(Objects.requireNonNull(response.getResult()).getOutput().getText());
    }

    public void test1(String embeddingData) {
        EmbeddingResponse response = embeddingModel.call(
                new EmbeddingRequest(List.of(embeddingData),
                        OpenAiEmbeddingOptions.builder()
                                .model("text-embedding-3-large")
                                .dimensions(3072)
                                .build()
                )
        );
        System.out.println(Arrays.toString(response.getResult().getOutput()));
        Usage usage = response.getMetadata().getUsage();

        if (usage.getNativeUsage() instanceof org.springframework.ai.openai.api.OpenAiApi.Usage nativeUsage) {
            System.out.println("promptToken: " + nativeUsage.promptTokens());
            System.out.println("completionToken: " + nativeUsage.completionTokens());
            System.out.println("totalToken: " + nativeUsage.totalTokens());
        }
    }
}