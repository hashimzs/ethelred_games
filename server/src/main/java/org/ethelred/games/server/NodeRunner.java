package org.ethelred.games.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.ethelred.games.util.Util;
import picocli.CommandLine;

public class NodeRunner implements Runnable, AutoCloseable {
    public static class NodeOptions {
        @CommandLine.Option(names = {"--enable-node"})
        boolean enabled;

        @CommandLine.Option(names = {"--node"})
        Path nodePath;

        @CommandLine.Option(names = {"--script"})
        Path scriptPath;

        boolean isEnabled() {
            return enabled || nodePath != null;
        }
    }
    private static final Logger LOGGER = LogManager.getLogger();

    private final Path node;
    private final Path script;
    private Process process;

    public NodeRunner(NodeOptions options) {
        node = findNode(options);
        script = findScript(options);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }));
    }

    private Path findScript(NodeOptions options) {
        // TODO using default build location for now
        var projectDir = Util.findParent(Path.of(System.getProperty("user.dir")), p -> p.endsWith("ethelred_games"));
        if (projectDir == null || !Files.exists(projectDir)) {
            throw new IllegalStateException("Could not find project directory");
        }
        var scriptDir = projectDir.resolve("frontend2").resolve("build");
        if (!Files.exists(scriptDir)) {
            throw new IllegalStateException("""
                    Could not find script directory in "%s"
                    """.formatted(projectDir));
        }
        return scriptDir;
    }

    private Path findNode(NodeOptions options) {
        if (options.nodePath != null) {
            return options.nodePath;
        }
        return Path.of("node");
    }

    @Override
    public void close() throws Exception {
        if (process != null && process.isAlive()) {
            process.destroy();
        }
    }

    @Override
    public void run() {
        try {
            LOGGER.info("Start node process \"{} {}\"", node, script);
            var pb = new ProcessBuilder()
                    .command(
                            node.toString(),
                            script.toString()
                    )
                    .inheritIO();

            process = pb.start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
