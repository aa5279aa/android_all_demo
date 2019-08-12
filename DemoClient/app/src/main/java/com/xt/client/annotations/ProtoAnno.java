package com.xt.client.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ProtoAnno {
    int tag();

    Label type() default Label.INT;

    boolean isArray();

    public static enum Label {
        INT(1), LONG(2), STRING(3), BOOLEAN(4), MESSAGE(5);

        private final int value;

        private Label(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }

    }

}