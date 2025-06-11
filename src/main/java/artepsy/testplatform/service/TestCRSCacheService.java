package artepsy.testplatform.service;

import artepsy.testplatform.datamodels.response.TestCRSResponse;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TestCRSCacheService {
    private final Map<String, TestCRSResponse> cache = new ConcurrentHashMap<>();

    public void saveResponse(String email, TestCRSResponse response) {
        cache.put(email, response);
    }

    public TestCRSResponse getResponse(String email) {
        return cache.get(email);
    }

    public void removeResponse(String email) {
        cache.remove(email);
    }
} 