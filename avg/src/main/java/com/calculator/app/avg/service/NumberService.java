package com.calculator.app.avg.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.http.HttpHeaders;
import java.util.*;
import com.calculator.app.avg.model.*;

@Service
public class NumberService {
    private final WebClient webClient;
    private static final String BASE_URL = "http://20.244.56.144/test/";
    private final Queue<Integer> slidingWindow = new LinkedList<>();
    private final int WINDOW_SIZE = 10;
    private final String TOKEN = "";

    public NumberService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(BASE_URL).build();
    }

    public Map<String, Object> fetchNumbers(String type) {
        String url = BASE_URL + type;

        try {
            NumberResponse response = webClient.get()
                .uri(url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN)
                .retrieve()
                .bodyToMono(NumberResponse.class)
                .block(); // Blocking call to get synchronous response

            return processResponse(response);
        } catch (WebClientResponseException.Unauthorized e) {
            return Map.of("error", "Unauthorized access - Invalid API token");
        } catch (Exception e) {
            return Map.of("error", "Failed to fetch numbers: " + e.getMessage());
        }
    }

    private Map<String, Object> processResponse(NumberResponse response) {
        List<Integer> numbers = response.getNumbers();
        List<Integer> prevState = new ArrayList<>(slidingWindow);

        for (int num : numbers) {
            if (!slidingWindow.contains(num)) {
                if (slidingWindow.size() >= WINDOW_SIZE) {
                    slidingWindow.poll();
                }
                slidingWindow.offer(num);
            }
        }

        List<Integer> currState = new ArrayList<>(slidingWindow);
        double avg = slidingWindow.stream().mapToDouble(Integer::intValue).average().orElse(0.0);

        return Map.of(
            "windowPrevState", prevState,
            "windowCurrState", currState,
            "numbers", numbers,
            "avg", avg
        );
    }
}
