package artepsy.testplatform.controller;

import artepsy.testplatform.datamodels.request.TestCRSRequest;
import artepsy.testplatform.datamodels.response.TestCRSResponse;
import artepsy.testplatform.service.TestCRSService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/test-crs")
@RequiredArgsConstructor
@Slf4j
public class TestCRSController {

    private final TestCRSService testCRSService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public TestCRSResponse scoreTestCRS(@RequestBody TestCRSRequest request) {
        log.info("Received request to score TestCRS: {}", request);
        return testCRSService.scoreTestCRS(request);
    }
}
