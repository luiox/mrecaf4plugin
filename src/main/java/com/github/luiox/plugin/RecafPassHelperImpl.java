package com.github.luiox.plugin;

import com.github.luiox.morpher.info.ClassInfo;
import com.github.luiox.morpher.transformer.IPassContext;
import com.github.luiox.morpher.transformer.PassHelper;
import jakarta.annotation.Nonnull;
import org.objectweb.asm.tree.ClassNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.function.Consumer;

public class RecafPassHelperImpl implements PassHelper {
    private static final Logger logger = LoggerFactory.getLogger(RecafPassHelperImpl.class);

    /**
     * 获取当前RecafPassHelperImpl的实例。
     *
     * @return RecafPassHelperImpl实例
     */
    private static RecafPassHelperImpl Instance;

    public static RecafPassHelperImpl getInstance() {
        if (Instance == null) {
            Instance = new RecafPassHelperImpl();
        }
        return Instance;
    }

    @Override
    public @Nonnull Map<String, ClassInfo> buildClassInfo(@Nonnull IPassContext ctx) {
        if (!(ctx instanceof RecafAdapterPassContext context)) {
            throw new IllegalArgumentException("ctx must be an instance of PassContext");
        }


        return Map.of();
    }

    @Override
    public void iterateClassNodeWithInfo(@Nonnull IPassContext ctx,
                                         @Nonnull Map<String, ClassInfo> infos,
                                         int rflag,
                                         int wflag,
                                         @Nonnull Consumer<ClassNode> consumer) {
        if (!(ctx instanceof RecafAdapterPassContext context)) {
            throw new IllegalArgumentException("ctx must be an instance of PassContext");
        }
    }

    @Override
    public void addLibPath(@Nonnull String s) {

    }
}
