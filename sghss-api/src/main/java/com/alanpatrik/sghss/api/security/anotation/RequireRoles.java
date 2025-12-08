package com.alanpatrik.sghss.api.security.anotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RequireRoles {

    /**
     * Lista de roles exigidas (sem prefixo ROLE_ no valor).
     * Ex.: {"ADMIN", "USER"}
     */
    String[] value() default {};

    Mode mode() default Mode.ANY;

    enum Mode {
        ANY, // usuário precisa ter pelo menos uma das roles
        ALL  // usuário precisa ter todas as roles
    }
}
