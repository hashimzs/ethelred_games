package org.ethelred.games.player;

import feign.InvocationContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.ResponseInterceptor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CookieJar implements RequestInterceptor, ResponseInterceptor {
    private static final Logger LOGGER = LogManager.getLogger();

    private final ThreadLocal<Map<String, String>> tlValues = ThreadLocal.withInitial(HashMap::new);
    @Override
    public void apply(RequestTemplate template) {
        var values = tlValues.get();
        if (!values.isEmpty()) {
            template.header("Cookie",
                    values.entrySet()
                            .stream()
                            .map(e -> "%s=%s".formatted(e.getKey(),e.getValue()))
                            .collect(Collectors.joining("; "))
            );
        }
        LOGGER.debug("Cookies: {}",values);
    }

    @Override
    public Object aroundDecode(InvocationContext invocationContext) throws IOException {
        var values = tlValues.get();
        var headers = invocationContext.response().headers();
        if (headers.containsKey("Set-Cookie")) {
            var cookieHeaders = headers.get("Set-Cookie");
            cookieHeaders.forEach(header -> {
                var split1 = header.split(";")[0];
                var split2 = split1.split("=",2);
                if (split2.length > 1) {
                    values.put(split2[0],split2[1]);
                }
            });
        }
        return invocationContext.proceed();
    }
}
