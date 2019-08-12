package com.lxl.gradledemo;

import org.gradle.api.Project;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

import java.io.PrintStream;


class TraceVisitor extends ClassVisitor {

    Project project;

    /**
     * 类名
     */
    private String className;

    /**
     * 父类名
     */
    private String superName;

    /**
     * 该类实现的接口
     */
    private String[] interfaces;


    public TraceVisitor(String className, ClassVisitor classVisitor) {
        super(Opcodes.ASM5, classVisitor);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor oldMethodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
        if (name.equals("<init>")) {
            return oldMethodVisitor;
        }
        if (!className.contains("MainActivity")) {
            return oldMethodVisitor;
        }
        System.out.println("className:" + className + ",name:" + name);
        System.out.println("desc:" + desc);

        //注入打印埋点
//        MethodVisitor changeAdapter = new AdviceAdapter(Opcodes.ASM5, oldMethodVisitor, access, name, desc) {
//
//            private boolean isInject() {
//                //判断是否拦截进行注入
//                if (superName.contains("Activity")) {
//                    return true;
//                }
//                return false;
//            }
//
//
//            @Override
//            public void visitCode() {
//                super.visitCode();
//            }
//
//            @Override
//            public AnnotationVisitor visitAnnotation(String descc, boolean visible) {
//                return super.visitAnnotation(descc, visible);
//            }
//
//            @Override
//            public void visitFieldInsn(int opcode, String owner, String name, String descc) {
//                super.visitFieldInsn(opcode, owner, name, descc);
//            }
//
//            @Override
//            protected void onMethodEnter() {
//                if (isInject()) {
//                    if ("onCreate".equals(name)) {
//                        mv.visitVarInsn(ALOAD, 0);
//                        //添加一个本地成员变量
//                        mv.visitLdcInsn("mmm");
//                        mv.visitLdcInsn(name);
//                        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/lxl/demo/util/TraceUtil",
//                                "traceMethod", "(Landroid/app/Activity;)V", false);
//                    } else if ("onDestroy".equals(name)) {
//                        mv.visitVarInsn(ALOAD, 0);
//                        mv.visitLdcInsn(name);
//                        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/lxl/demo/util/TraceUtil",
//                                "traceMethod", "(Landroid/app/Activity;)V", false);
//                    }
//                }
//            }
//
//            @Override
//            protected void onMethodExit(int opcode) {
//                super.onMethodExit(opcode);
//            }
//        };

        ChangeAdapter changeAdapter = new ChangeAdapter(Opcodes.ASM5, oldMethodVisitor, access, name, desc);
        return changeAdapter;
    }

    static class ChangeAdapter extends AdviceAdapter {

        private int startTimeId = -1;
        private String methodName = null;

        protected ChangeAdapter(int api, MethodVisitor mv, int access, String name, String desc) {
            super(api, mv, access, name, desc);
            methodName = name;
            System.out.println("ChangeAdapter:" + name);
        }

        @Override
        protected void onMethodEnter() {
            super.onMethodEnter();
            startTimeId = newLocal(Type.LONG_TYPE);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
            mv.visitIntInsn(LSTORE, startTimeId);
        }

        @Override
        protected void onMethodExit(int opcode) {
            super.onMethodExit(opcode);
            int durationId = newLocal(Type.LONG_TYPE);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
            mv.visitVarInsn(LLOAD, startTimeId);
            mv.visitInsn(LSUB);
            mv.visitVarInsn(LSTORE, durationId);            //存储持续时间

            mv.visitLdcInsn("lxltest");

            mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);

            mv.visitLdcInsn("The cost time of " + methodName + " is ");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);

            mv.visitVarInsn(LLOAD, durationId);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(J)Ljava/lang/StringBuilder;", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);

            mv.visitMethodInsn(INVOKESTATIC, "android/util/Log", "i", "(Ljava/lang/String;Ljava/lang/String;)I", false);
        }
    }


    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        this.className = name;
        this.superName = superName;
        this.interfaces = interfaces;
    }
}
