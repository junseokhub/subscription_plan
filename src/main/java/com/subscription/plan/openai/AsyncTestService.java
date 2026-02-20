package com.subscription.plan.openai;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class AsyncTestService{

    // non-blocking, reactive
    public Flux<String> getWebFluxExample() {
        return Flux.interval(Duration.ofMillis(500))
                .map(e -> "Message " + e)
                .take(5) // n개만 처리
                .onBackpressureDrop(); // 버퍼차면 아이템 버림
//                .collectList()
//                .map(list -> String.join(", ", list));
    }

    // Thread-based 비동기
    public CompletableFuture<String> getAsyncExample() {
        return CompletableFuture.supplyAsync(() -> {
            List<String> results = new ArrayList<>();

            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(500);

                    String msg = "Message " + i;
                    System.out.println(msg);
                    results.add(msg + " - Processed with Async");

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            System.out.println(results);
            return results;
        }).thenApply(list -> String.join(", ", list));
    }
}
