package me.silvios.wasp.itemservicebff;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;

@RestController
public class HelloController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloController.class);

    private final RestTemplate restTemplate;

    public static final String HTTPBIN_SERVICE_PATH = "/item-service";
    public static final String PAGEABLE_PREFIX = "pageable";

    public HelloController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Value("${httpbin.host}")
    private String host;

    @GetMapping("/**")
    public ResponseEntity<?> mirrorRest(HttpMethod method, HttpServletRequest request)
            throws URISyntaxException {
        LOGGER.info("Requesting request.getRequestURI(): {}", request.getRequestURI());

        String requestUrl = getServiceURL(request.getRequestURI());

        URI uri =
                UriComponentsBuilder.fromUri(new URI(host))
                        .path(requestUrl)
                        .query(request.getQueryString())
                        .build(true)
                        .toUri();

        LOGGER.info("Requesting item-service proxy: {}", uri);
        HttpHeaders headers = new HttpHeaders();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.set(headerName, request.getHeader(headerName));
        }
        try {
            ResponseEntity<String> exchange =
                    restTemplate.exchange(uri, method, new HttpEntity<>(headers), String.class);
            LOGGER.debug("Request response headers: {}", exchange.getHeaders());
            LOGGER.debug("Request response body: {}", exchange.getBody());
            LOGGER.info("Request response status: {}", exchange.getStatusCode());

            return new ResponseEntity<>(
                    exchange.getBody(),
                    filterResponseHeaders(exchange.getHeaders()),
                    exchange.getStatusCode());
        } catch (HttpStatusCodeException e) {
            LOGGER.info(
                    "Status [{}] requesting item-service proxy: {}",
                    e.getStatusCode(),
                    e.getResponseBodyAsString());
            return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
        }
    }

    private HttpHeaders filterResponseHeaders(HttpHeaders headers) {
        HttpHeaders exchangeHeaders = HttpHeaders.writableHttpHeaders(headers);

        exchangeHeaders
                .entrySet()
                .removeIf(entry -> !entry.getKey().toLowerCase().startsWith(PAGEABLE_PREFIX));

        return exchangeHeaders;
    }

    private String getServiceURL(String originalURL) {
        return originalURL.substring(
                originalURL.indexOf(HTTPBIN_SERVICE_PATH) + HTTPBIN_SERVICE_PATH.length());
    }

}
