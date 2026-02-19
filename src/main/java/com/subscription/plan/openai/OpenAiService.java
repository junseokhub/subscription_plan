//package com.subscription.plan.openai;
//
//import com.openai.client.OpenAIClient;
//import com.openai.client.okhttp.OpenAIOkHttpClient;
//import com.openai.models.ChatModel;
//import com.openai.models.responses.Response;
//import com.openai.models.responses.ResponseCreateParams;
//import com.openai.models.responses.ResponseOutputText;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class OpenAiService {
//
//    @Value("${open.ai.key}")
//    private String openAiKey;
//
//    private OpenAIClient client(String openAiKey) {
//        return OpenAIOkHttpClient.builder().apiKey(openAiKey).build();
//    }
//
//    public Response text(String text) {
//        ResponseCreateParams params = ResponseCreateParams.builder()
//                .input(text)
//                .model(ChatModel.GPT_5_2_CHAT_LATEST)
//                .build();
//        Response response = client(openAiKey).responses().create(params);
//        String output = response.output().stream()
//                .flatMap(item -> item.message().stream())
//                .flatMap(message -> message.content().stream())
//                .flatMap(content -> content.outputText().stream())
//                .map(ResponseOutputText::text)
//                .findFirst()
//                .orElse("");
//        System.out.println(output);
//        return response;
//    }
//}
