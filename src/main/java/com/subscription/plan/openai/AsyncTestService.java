package com.subscription.plan.openai;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

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
                sleep(500);

                String msg = "Message " + i;
                System.out.println(msg);
                results.add(msg + " - Processed with Async");

            }
            System.out.println(results);
            return results;
        }).thenApply(list -> String.join(", ", list));
    }

    public void test() {
        System.out.println("start");

        Mono<Long> mono = Mono.delay(Duration.ofSeconds(1))
                .doOnNext(i -> System.out.println("delay done"));

        mono.subscribe();
        System.out.println("end");
    }

    public void test1() {
        System.out.println("start");

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {}
            return "done";
        });

        future.thenAccept(result -> System.out.println("delay done: " + result));
        System.out.println("end");

        future.join();
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {}
    }
}
