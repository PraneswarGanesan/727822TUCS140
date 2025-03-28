package com.calculator.app.avg.service;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.*;
import com.calculator.app.avg.model.*;
@Service
public class NumberService {
    private static final String BASE_URL = "http://20.244.56.144/test/";
    private final RestTemplate restTemplate = new RestTemplate();
    private final Queue<Integer> slidingWindow = new LinkedList<>();
    private final int WINDOW_SIZE = 10;

   public Map<String, Object> fetchNumbers(String type, String token) {
    String url = BASE_URL + type;

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(token); 
    HttpEntity<String> entity = new HttpEntity<>(headers);

    ResponseEntity<NumberResponse> response;
    try {
        response = restTemplate.exchange(url, HttpMethod.GET, entity, NumberResponse.class);
    } catch (HttpClientErrorException e) {
        throw new RuntimeException("Error fetching numbers: " + e.getResponseBodyAsString());
    }

    List<Integer> numbers = response.getBody().getNumbers();
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

    Map<String, Object> result = new HashMap<>();
    result.put("windowPrevState", prevState);
    result.put("windowCurrState", currState);
    result.put("numbers", numbers);
    result.put("avg", avg);

    return result;
}


}