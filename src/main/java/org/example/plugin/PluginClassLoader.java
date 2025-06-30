package org.example.plugin;

import com.github.luiox.morpher.plugin.IPassPlugin;
import com.github.luiox.morpher.plugin.PluginInfo;
import com.github.luiox.morpher.transformer.AbstractPass;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.slf4j.Logger;
import software.coley.recaf.analytics.logging.Logging;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.jar.JarFile;

public final class PluginClassLoader {
    private static final Logger logger = Logging.get(PluginClassLoader.class);
    String SeachPath = "D:\\WorkSpace\\mc\\morpher\\morpher-api\\morpher-example\\build\\libs";
    String PluginDesc = Type.getInternalName(IPassPlugin.class);

    public PluginClassLoader() {
    }

    public Map<String,List<AbstractPass>> scanAndLoadPassPlugin() {
       Map<String, List<AbstractPass>>  passes = new HashMap<>();

        logger.info("Type.getInternalName(IPassPlugin.class)) = {}", PluginDesc);
        // 扫描path下的jar
        try {
            Files.walk(Paths.get(SeachPath)).forEach(path -> {
                logger.info("path: {}", path);
                if (Files.isRegularFile(path)) {
                    // 如果是jar文件
                    if (path.toString().endsWith(".jar")) {
                        try (JarFile jarFile = new JarFile(path.toFile())) {
                            List<AbstractPass> passList = new ArrayList<>();
                            AtomicReference<String> passName = new AtomicReference<>("");
                            // 遍历jar中的每个条目
                            jarFile.stream().forEach(entry -> {
                                if (entry.getName().endsWith(".class")) {
                                    // 读取class文件内容
                                    try (InputStream inputStream = jarFile.getInputStream(entry);
                                         ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                                        byte[] buffer = new byte[1024];
                                        int bytesRead;
                                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                                            outputStream.write(buffer, 0, bytesRead);
                                        }
                                        byte[] classBytes = outputStream.toByteArray();

                                        // 转为ClassNode
                                        ClassReader classReader = new ClassReader(classBytes);
                                        ClassNode classNode = new ClassNode();
                                        classReader.accept(classNode, 0);

                                        if (classNode.interfaces != null && !classNode.interfaces.isEmpty()) {
                                            for (String interfaceName : classNode.interfaces) {
                                                if (!interfaceName.equals(PluginDesc)) {
                                                    continue;
                                                }
                                                if (classNode.visibleAnnotations != null && !classNode.visibleAnnotations.isEmpty()) {
                                                    // 查找
                                                    for (AnnotationNode annotationNode : classNode.visibleAnnotations) {
                                                        if (annotationNode.desc.equals(Type.getDescriptor(PluginInfo.class))) {
                                                            // 加载这个类
                                                            try (MemoryClassLoader classLoader = new MemoryClassLoader(new URL[]{path.toUri().toURL()}, PluginClassLoader.class.getClassLoader())) {
                                                                var klazz = classLoader.loadClassFromMemory(classBytes);
                                                                var pluginIns = (IPassPlugin) klazz.getDeclaredConstructor().newInstance();
                                                                passList.addAll(pluginIns.getAvailablePasses());
                                                            }
                                                            passName.set((String) annotationNode.values.get(1));
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                    } catch (IOException | InvocationTargetException | InstantiationException |
                                             IllegalAccessException | NoSuchMethodException e) {
                                        logger.error(e.getMessage());;
                                    }
                                }
                            });
                            if(!passList.isEmpty() && !passName.get().isEmpty()){
                                passes.put(passName.get(), passList);
                            }
                        } catch (IOException e) {
                            logger.error(e.getMessage());
                        }

                    }
                }
            });
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return passes;
    }

    static class MemoryClassLoader extends URLClassLoader {
        public MemoryClassLoader(URL[] urls, ClassLoader parent) {
            super(urls, parent);
        }

        public Class<?> loadClassFromMemory(byte[] classBytes) {
            return defineClass(null, classBytes, 0, classBytes.length);
        }
    }
}
