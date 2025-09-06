package com.github.luiox.plugin;

import com.github.luiox.morpher.transformer.IPassContext;
import com.github.luiox.morpher.transformer.MethodPass;
import com.github.luiox.morpher.transformer.PassInfo;
import jakarta.annotation.Nonnull;
import org.objectweb.asm.tree.ClassNode;
import software.coley.recaf.info.JvmClassInfo;
import software.coley.recaf.services.transform.JvmClassTransformer;
import software.coley.recaf.services.transform.JvmTransformerContext;
import software.coley.recaf.services.transform.TransformationException;
import software.coley.recaf.workspace.model.Workspace;
import software.coley.recaf.workspace.model.bundle.JvmClassBundle;
import software.coley.recaf.workspace.model.resource.WorkspaceResource;

public class MethodPassAdapterTransformer implements JvmClassTransformer {
    MethodPass methodPass;
    IPassContext passContext;

    public MethodPassAdapterTransformer(MethodPass methodPass, IPassContext passContext) {
        this.methodPass = methodPass;
        this.passContext = passContext;
    }

    @Override
    public void setup(@Nonnull JvmTransformerContext context, @Nonnull Workspace workspace) {
        methodPass.doInitialization(passContext);
    }

    @Override
    public void transform(@Nonnull JvmTransformerContext context, @Nonnull Workspace workspace,
                          @Nonnull WorkspaceResource resource, @Nonnull JvmClassBundle bundle,
                          @Nonnull JvmClassInfo initialClassState) throws TransformationException {

        ClassNode node = context.getNode(bundle, initialClassState);
        if (node.methods != null && !node.methods.isEmpty()) {
            passContext.setCurrentClass(node);
            for (var method : node.methods) {
                methodPass.run(method, passContext);
            }

            context.setNode(bundle, initialClassState, node);
        }
    }

    @Override
    public @Nonnull String name() {
        var annotation = methodPass.getClass().getAnnotation(PassInfo.class);
        // If the annotation is not present, we return the class name.
        if (annotation == null) {
            return methodPass.getClass().getSimpleName();
        }
        // If the annotation is present, we return the name from the annotation.
        return annotation.name();
    }
}
