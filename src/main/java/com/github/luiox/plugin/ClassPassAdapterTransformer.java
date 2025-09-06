package com.github.luiox.plugin;

import com.github.luiox.morpher.transformer.ClassPass;
import com.github.luiox.morpher.transformer.IPassContext;
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

public class ClassPassAdapterTransformer implements JvmClassTransformer {
    ClassPass classPass;
    IPassContext passContext;

    public ClassPassAdapterTransformer(ClassPass classPass, IPassContext passContext) {
        this.classPass = classPass;
        this.passContext = passContext;
    }

    @Override
    public void setup(@Nonnull JvmTransformerContext context, @Nonnull Workspace workspace) {
        classPass.doInitialization(passContext);
    }

    @Override
    public void transform(@Nonnull JvmTransformerContext context, @Nonnull Workspace workspace,
                          @Nonnull WorkspaceResource resource, @Nonnull JvmClassBundle bundle,
                          @Nonnull JvmClassInfo initialClassState) throws TransformationException {
        ClassNode node = context.getNode(bundle, initialClassState);
        passContext.setCurrentClass(node);
        classPass.run(node, passContext);
        context.setNode(bundle, initialClassState, node);
    }

    @Override
    public @Nonnull String name() {
        var annotation = classPass.getClass().getAnnotation(PassInfo.class);
        // If the annotation is not present, we return the class name.
        if (annotation == null) {
            return classPass.getClass().getSimpleName();
        }
        // If the annotation is present, we return the name from the annotation.
        return annotation.name();
    }
}
