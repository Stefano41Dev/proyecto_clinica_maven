package security;

import model.enums.Rol;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RolesPermitidos {
    Rol[] value();
}
