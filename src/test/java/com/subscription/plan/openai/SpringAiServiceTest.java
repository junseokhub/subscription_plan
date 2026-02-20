package com.subscription.plan.openai;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
@SpringBootTest
class SpringAiServiceTest {
    @Autowired
    private SpringAiService springAiService;

    @Test
    void test() {
        springAiService.test("Hello");
    }

    @Test
    void test1() {
        springAiService.test1("Hello");
    }
}