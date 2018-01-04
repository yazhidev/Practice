package com.yazhi1992.lint;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;

import java.util.Collections;
import java.util.List;

import lombok.ast.AstVisitor;
import lombok.ast.ForwardingAstVisitor;
import lombok.ast.MethodInvocation;
import lombok.ast.Node;

public class ToastDetector extends Detector implements Detector.JavaScanner {

    public static final Issue ISSUE = Issue.create(
            "ToastUse",
            "避免使用 Toast.makeText().show();",
            "使用MyToast",
            Category.SECURITY, 5, Severity.ERROR,
            new Implementation(ToastDetector.class, Scope.JAVA_FILE_SCOPE));


    @Override
    public List<Class<? extends Node>> getApplicableNodeTypes() {
        return Collections.<Class<? extends Node>>singletonList(MethodInvocation.class);
    }


    @Override
    public AstVisitor createJavaVisitor(final JavaContext context) {
        return new ForwardingAstVisitor() {
            @Override
            public boolean visitMethodInvocation(MethodInvocation node) {
                if (node.toString().startsWith("Toast")) {
                    context.report(ISSUE, node, context.getLocation(node),
                            "避免使用 Toast.makeText().show();");
                    return true;
                }
                return super.visitMethodInvocation(node);
            }
        };
    }
}
