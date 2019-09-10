package cz.rict.carcassonne.classic.base.launcher.dependency;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import cz.rict.carcassonne.classic.base.util.Utils;

public class DependencyUpdater
{
    private static final String DEP_FILE_PATH = "/META-INF/dependencies.cfg";

    private DependencyUpdater()
    {
    }

    public static List<String> checkDependencies()
    {
        final Path libDirectory = Utils.getRunDirPath().resolve("libraries");
        final List<String> result = new ArrayList<>();

        final InputStream is = DependencyUpdater.class.getResourceAsStream(DEP_FILE_PATH);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final byte[] buffer = new byte[1_024];
        final String deps;
        int length;

        try
        {
            Files.createDirectories(libDirectory);

            length = is.read(buffer);
            while (length != -1)
            {
                baos.write(buffer, 0, length);
                length = is.read(buffer);
            }

            deps = baos.toString(StandardCharsets.UTF_8.name());

            is.close();
        }
        catch (final IOException e)
        {
            // TODO: shutdown app and report error properly
            return result;
        }

        for (String dep : deps.split("\\r?\\n"))
        {
            dep = dep.trim();
            if (!dep.isEmpty())
            {
                final String path = resolveDependency(dep, libDirectory);
                if (!path.isEmpty())
                {
                    result.add(path);
                }
            }
        }

        return result;
    }

    private static String resolveDependency(final String depString, final Path directory)
    {
        final String modified = depString.replace("$lwjglNatives", Utils.getOSType().getLwjglNatives());
        final Dependency dep = new Dependency(modified);

        return DependencyResolvers.findResolver(dep).resolve(dep, directory);
    }

    protected static class Dependency
    {
        private final String repository;
        private final String group;
        private final String artifact;
        private final String version;
        private final String classifier;

        protected Dependency(final String dep)
        {
            final String[] subs = dep.split(":");
            int subIndex = 0;
            this.repository = subs.length > subIndex ? subs[subIndex] : "";
            this.group = subs.length > ++subIndex ? subs[subIndex] : "";
            this.artifact = subs.length > ++subIndex ? subs[subIndex] : "";
            this.version = subs.length > ++subIndex ? subs[subIndex] : "";
            this.classifier = subs.length > ++subIndex ? subs[subIndex] : "";
        }

        public String getRepository()
        {
            return repository;
        }

        public String getGroup()
        {
            return group;
        }

        public String getArtifact()
        {
            return artifact;
        }

        public String getVersion()
        {
            return version;
        }

        public String getClassifier()
        {
            return classifier;
        }
    }

    private enum DependencyResolvers
    {
        MAVEN_CENTRAL("mavenCentral", MavenResolver::resolveMavenCentral),
        UNKNOWN("", (dep, dir) -> "");

        private String repositoryName;
        private BiFunction<Dependency, Path, String> resolver;

        DependencyResolvers(final String repositoryName, final BiFunction<Dependency, Path, String> resolver)
        {
            this.repositoryName = repositoryName;
            this.resolver = resolver;
        }

        private String resolve(final Dependency dep, final Path directory)
        {
            return resolver.apply(dep, directory);
        }

        private static DependencyResolvers findResolver(final Dependency dep)
        {
            for (final DependencyResolvers d : DependencyResolvers.values())
            {
                if (dep.getRepository().equals(d.repositoryName))
                {
                    return d;
                }
            }
            return UNKNOWN;
        }
    }
}
