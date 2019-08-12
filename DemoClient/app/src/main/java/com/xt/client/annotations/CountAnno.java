package com.xt.client.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CountAnno {
    int count();

    Label type() default Label.SINGLE;


    public static enum Label {
        SINGLE(1), DOUBLE(2), THRICE(3);


        private final int value;

        private Label(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }

    }


}