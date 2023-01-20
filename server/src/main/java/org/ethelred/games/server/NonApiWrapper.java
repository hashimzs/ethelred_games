package org.ethelred.games.server;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.HandlerWrapper;

import java.io.IOException;

public class NonApiWrapper extends HandlerWrapper {
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        LOGGER.info("Wrapper.handle {}", target);
        if (target != null && target.startsWith("/api/")) {
            baseRequest.setHandled(false);
        } else {
            super.handle(target, baseRequest, request, response);
        }

    }
}
