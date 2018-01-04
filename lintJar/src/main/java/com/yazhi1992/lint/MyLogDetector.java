package com.yazhi1992.lint;

import com.android.tools.lint.client.api.JavaParser;
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

public class MyLogDetector extends Detector implements Detector.JavaScanner {

    public static final Issue ISSUE = Issue.create(
            "LogUse",
            "避免使用Log/System.out.println",
            "使用MyLog",
            Category.SECURITY, 10, Severity.FATAL,
            new Implementation(MyLogDetector.class, Scope.JAVA_FILE_SCOPE));


    @Override
    public List<Class<? extends Node>> getApplicableNodeTypes() {
        return Collections.<Class<? extends Node>>singletonList(MethodInvocation.class);
    }


    @Override
    public AstVisitor createJavaVisitor(final JavaContext context) {
        return new ForwardingAstVisitor() {
            @Override
            public boolean visitMethodInvocation(MethodInvocation node) {
                if (node.toString().startsWith("System.out.println")) {
                    context.report(ISSUE, node, context.getLocation(node),
                            "请使用MyLog，避免使用System.out.println");
                    return true;
                }

                JavaParser.ResolvedNode resolve = context.resolve(node);
                if (resolve instanceof JavaParser.ResolvedMethod) {
                    JavaParser.ResolvedMethod method = (JavaParser.ResolvedMethod) resolve;
                    // 方法所在的类校验
                    JavaParser.ResolvedClass containingClass = method.getContainingClass();
                    if (containingClass.matches("android.util.Log")) {
                        context.report(ISSUE, node, context.getLocation(node),
                                "请使用MyLog，避免使用Log");
                        return true;
                    }
                }
                return super.visitMethodInvocation(node);
            }
        };
    }
}
