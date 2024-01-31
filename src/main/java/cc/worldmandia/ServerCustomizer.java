package cc.worldmandia;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

public class ServerCustomizer {
    @Component
    @Profile("dev")
    private static class ServerCustomizerDev implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {
        @Override
        public void customize(ConfigurableWebServerFactory factory) {
            factory.setPort(6666);
        }
    }

    @Component
    @Profile("!dev")
    private static class ServerCustomizerProd implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {
        @Override
        public void customize(ConfigurableWebServerFactory factory) {
            factory.setPort(9999);
        }
    }
}
