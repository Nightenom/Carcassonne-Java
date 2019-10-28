package cz.rict.carcassonne.classic.base.launcher;

import java.io.IOException;
import java.lang.module.Configuration;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReference;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import cz.rict.carcassonne.classic.base.event.TestEvent;
import cz.rict.carcassonne.classic.base.launcher.dependency.DependencyUpdater;
import cz.rict.carcassonne.classic.base.mod.Mod;

/**
 * Main class when using module path
 */
public final class Main
{
    // -Dcustom_key="custom_value"
    private static final String MOD_DIR_PROP = "game.mod.directory";
    private static final String BASE_MODULE_NAME = Main.class.getModule().getName();

    /**
     * Private constructor to hide the implicit public one
     */
    private Main()
    {}

    /**
     * CAN'T TOUCH THIS
     *
     * @param args WIERD THINGS WHICH SHOULD NOT BE USED
     */
    public static void main(final String[] args)
    {
        System.out.println("Mod discovery...");

        final Path modsDir = Path.of(System.getProperties().containsKey(MOD_DIR_PROP) ? System.getProperty(MOD_DIR_PROP) : "mods");
        if (Files.isDirectory(modsDir))
        {
            final ModuleFinder finder = ModuleFinder.of(modsDir);
            final ModuleLayer currentModuleLayer = ModuleLayer.boot();
            final Set<ModuleReference> moduleReferences = finder.findAll();
            final Set<String> moduleNames = moduleReferences.stream().map(moduleRef -> moduleRef.descriptor().name()).collect(Collectors.toSet());
            final Configuration configuration = currentModuleLayer.configuration().resolveAndBind(finder, ModuleFinder.of(new Path[0]), moduleNames);
            final ModuleLayer moduleLayer = currentModuleLayer.defineModulesWithOneLoader(configuration, Main.class.getClass().getClassLoader());

            moduleReferences.stream().forEach(moduleRef -> loadMod(moduleLayer.findModule(moduleRef.descriptor().name()).orElse(null), moduleRef));
        }
        else
        {
            System.out.println("No mods found at: " + modsDir.toAbsolutePath().toString());
        }

        TestEvent.post(": Test event");
    }

    /**
     * Tries to load module as mod, continues with mod loading pipeline if succeeded
     *
     * @param mod    mod module
     * @param modRef mod module reference
     */
    private static void loadMod(final Module mod, final ModuleReference modRef)
    {
        // To logger
        System.out.println("Found possible mod at: " + modRef.location().map(uri -> Paths.get(uri.normalize()).toString()).orElse("Unknown location"));

        // discover module-info.java, find exposed packages
        final List<String> accessiblePackages = new ArrayList<>();
        modRef.descriptor().exports().forEach(ex -> {
            if (ex.targets().isEmpty() || !ex.targets().contains(BASE_MODULE_NAME))
            {
                return; // unaccessible package
            }
            accessiblePackages.add(ex.source().replace('.', '/'));
        });

        if (accessiblePackages.isEmpty())
        {
            return;
        }

        // discover entire module, find exposed classes
        final List<String> modClassCandidates = new ArrayList<>();
        final AtomicReference<String> dependenciesCfgPath = new AtomicReference<String>();
        try
        {
            modRef.open().list().forEach(path -> {
                if (path.endsWith(".class") && !path.equals("module-info.class") && accessiblePackages.contains(path.substring(0, path.lastIndexOf('/'))))
                {
                    modClassCandidates.add(path.substring(0, path.length() - ".class".length()).replace('/', '.'));
                }
                if (path.equals(DependencyUpdater.DEP_FILE_PATH))
                {
                    dependenciesCfgPath.set(path);
                }
            });
        }
        catch (final IOException e)
        {
            dependenciesCfgPath.set("");
            System.out.println("Failed discovering mod file:");
            e.printStackTrace();
        }

        if (modClassCandidates.isEmpty())
        {
            return;
        }

        // filter actual mod classes
        modClassCandidates.forEach(classPath -> {
            final Class<?> modClazz = Class.forName(mod, classPath);
            if (modClazz == null)
            {
                return;
            }

            final Mod modId = modClazz.getAnnotation(Mod.class);
            if (modId != null)
            {
                System.out.println("Found mod: " + modId.value());
                // Hook event register here or f it and let mods register events in constructor
                // might need static list to prevent GC of mod instances, but not needed
                try
                {
                    modClazz.getConstructor().newInstance();
                }
                catch (final NoSuchMethodException | IllegalArgumentException e)
                {
                    System.out.println("Missing public non argument constructor");
                    e.printStackTrace();
                }
                catch (final SecurityException | IllegalAccessException e)
                {
                    System.out.println("Can't access mod constructor");
                    e.printStackTrace();
                }
                catch (final InstantiationException | InvocationTargetException e)
                {
                    System.out.println("Can't instantiate mod instance");
                    e.printStackTrace();
                }
            }
        });
    }
}
