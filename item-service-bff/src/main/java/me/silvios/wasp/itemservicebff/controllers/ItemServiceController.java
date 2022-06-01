package me.silvios.wasp.itemservicebff.controllers;

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
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;

@RestController
public class ItemServiceController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemServiceController.class);

    private final RestTemplate restTemplate;

    public static final String HTTPBIN_SERVICE_PATH = "/item-service";
    public static final String PAGEABLE_PREFIX = "pageable";

    public ItemServiceController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Value("${httpbin.host}")
    private String host;

    @GetMapping("/**")
    public ResponseEntity<?> mirrorRest(HttpMethod method, HttpServletRequest request)
            throws URISyntaxException {
        LOGGER.info("host: {}", host);
        LOGGER.info("request.getRequestURL(): {}", request.getRequestURL());
        LOGGER.info("request.getRequestURI(): {}", request.getRequestURI());
        LOGGER.info("request.getScheme(): {}", request.getScheme());
        LOGGER.info("request.getContextPath(): {}", request.getContextPath());

        String pathWithinApplication = new UrlPathHelper().getPathWithinApplication(request);

        LOGGER.info("UrlPathHelper().getPathWithinApplication(request): {}", pathWithinApplication);

        String requestUrl = getServiceURL(request.getRequestURI());

        LOGGER.info("getServiceURL(request.getRequestURI()): {}", requestUrl);

        URI uri =
                UriComponentsBuilder.fromUri(new URI(host))
                        .path(requestUrl)
                        .query(request.getQueryString())
                        .build(true)
                        .toUri();

        LOGGER.info("uri: {}", uri);

        HttpHeaders headers = new HttpHeaders();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);

            headers.set(headerName, headerValue);

            LOGGER.info("header.{}: {}", headerName, headerValue);
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
        LOGGER.info("originalURL: {} [length: {}]", originalURL, originalURL.length());

        int beginIndex = originalURL.indexOf(HTTPBIN_SERVICE_PATH) + HTTPBIN_SERVICE_PATH.length();

        LOGGER.info("beginIndex: {}", beginIndex);

        if (beginIndex >= 0 && beginIndex <= originalURL.length()) {
            return originalURL.substring(beginIndex);
        } else {
            return originalURL;
        }
    }

}
