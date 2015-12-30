package tmp;

import java.lang.reflect.Method;
import java.util.jar.JarFile;

import org.jboss.modules.DependencySpec;
import org.jboss.modules.Module;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ModuleSpec;
import org.jboss.modules.ResourceLoader;
import org.jboss.modules.ResourceLoaderSpec;
import org.jboss.modules.ResourceLoaders;
import org.jboss.modules.util.TestModuleLoader;
import org.jboss.modules.util.TestResourceLoader;
/**
 * @author fonkap
 */
public class Bootstrap {

	public static void main(String[] args) throws Exception {

		TestModuleLoader moduleLoader = new TestModuleLoader();
		
		//Building module for library1
		final ModuleIdentifier module1Id = ModuleIdentifier.fromString("library1");
		ModuleSpec.Builder moduleBuilder = ModuleSpec.build(module1Id);
		JarFile jarFile = new JarFile("lib/lib3-v1/lib3.jar", true);
		ResourceLoader rl1 = ResourceLoaders.createJarResourceLoader("lib3-v1", jarFile);
		moduleBuilder.addResourceRoot(ResourceLoaderSpec.createResourceLoaderSpec(
				rl1
				));
		moduleBuilder.addResourceRoot(ResourceLoaderSpec.createResourceLoaderSpec(
				TestResourceLoader.build()
				.addClass(Library1.class)
				.create()
				));
		moduleBuilder.addDependency(DependencySpec.createLocalDependencySpec());
		moduleLoader.addModuleSpec(moduleBuilder.create());

		//Building module for library2
		final ModuleIdentifier module2Id = ModuleIdentifier.fromString("library2");
		moduleBuilder = ModuleSpec.build(module2Id);
		jarFile = new JarFile("lib/lib3-v2/lib3.jar", true);
		rl1 = ResourceLoaders.createJarResourceLoader("lib3-v2", jarFile);
		moduleBuilder.addResourceRoot(ResourceLoaderSpec.createResourceLoaderSpec(
				rl1
				));
		moduleBuilder.addResourceRoot(ResourceLoaderSpec.createResourceLoaderSpec(
				TestResourceLoader.build()
				.addClass(Library2.class)
				.create()
				));
		moduleBuilder.addDependency(DependencySpec.createLocalDependencySpec());
		moduleLoader.addModuleSpec(moduleBuilder.create());

		//Building main module
		final ModuleIdentifier moduleMainId = ModuleIdentifier.fromString("main");
		moduleBuilder = ModuleSpec.build(moduleMainId);
		moduleBuilder.addResourceRoot(ResourceLoaderSpec.createResourceLoaderSpec(
				TestResourceLoader.build()
				.addClass(Main.class)
				.create()
				));
		moduleBuilder.addDependency(DependencySpec.createModuleDependencySpec(module1Id, true, false));
		moduleBuilder.addDependency(DependencySpec.createModuleDependencySpec(module2Id, true, false));
		moduleBuilder.addDependency(DependencySpec.createLocalDependencySpec());
		moduleLoader.addModuleSpec(moduleBuilder.create());

		//Loading Main class and running it
		Module moduleMain = moduleLoader.loadModule(moduleMainId);
		Class<?> m = moduleMain.getClassLoader().loadClass("tmp.Main");
		Method method = m.getMethod("main", String[].class);
		method.invoke(null, (Object) new String[0]);
	}

}
