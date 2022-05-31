package me.silvios.wasp.itemservicebff;

import java.util.function.Supplier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestConfig {
  private final AsyncProperties asyncProperties;

  @Value("${service.timeout}")
  private Integer timeoutService;

  @Autowired
  public RestConfig(final AsyncProperties asyncProperties) {
    this.asyncProperties = asyncProperties;
  }

  @Bean
  public RestTemplate restTemplate(final RestTemplateBuilder restTemplateBuilder) {

    return restTemplateBuilder.requestFactory(new RequestFactorySupplier()).build();
  }

  class RequestFactorySupplier implements Supplier<ClientHttpRequestFactory> {

    @Override
    public ClientHttpRequestFactory get() {

      final PoolingHttpClientConnectionManager poolingConnectionManager =
              new PoolingHttpClientConnectionManager();

      poolingConnectionManager.setMaxTotal(asyncProperties.getMaxPoolSize());
      poolingConnectionManager.setDefaultMaxPerRoute(asyncProperties.getCorePoolSize());

      final CloseableHttpClient client =
              HttpClientBuilder.create().setConnectionManager(poolingConnectionManager).build();

      final HttpComponentsClientHttpRequestFactory clientHttpRequestFactory =
              new HttpComponentsClientHttpRequestFactory(client);

      clientHttpRequestFactory.setConnectTimeout(timeoutService);
      clientHttpRequestFactory.setReadTimeout(timeoutService);

      return clientHttpRequestFactory;
    }
  }
}
