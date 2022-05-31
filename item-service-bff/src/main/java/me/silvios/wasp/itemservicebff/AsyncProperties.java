package me.silvios.wasp.itemservicebff;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "async")
public class AsyncProperties {

  private Integer corePoolSize;
  private Integer maxPoolSize;

  public Integer getCorePoolSize() {
    return corePoolSize;
  }

  public Integer getMaxPoolSize() {
    return maxPoolSize;
  }

  public void setCorePoolSize(final Integer corePoolSize) {
    this.corePoolSize = corePoolSize;
  }

  public void setMaxPoolSize(final Integer maxPoolSize) {
    this.maxPoolSize = maxPoolSize;
  }
}
