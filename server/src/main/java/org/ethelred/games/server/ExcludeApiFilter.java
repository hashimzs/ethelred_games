package org.ethelred.games.server;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class ExcludeApiFilter extends HttpFilter {
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        var path = req.getServletPath();
        LOGGER.info("Filter path {}", path);
        if (path == null || !path.startsWith("/api/")) {
            chain.doFilter(req, res);
        }
    }
}
