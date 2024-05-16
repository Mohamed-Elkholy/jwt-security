package com.kholy.bsmart.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),
    CUSTOMER_UPDATE("customer:update"),
    CUSTOMERR_READ("customer:read"),
    CUSTOMER_CREATE("customer:create"),
    CUSTOMER_DELETE("customer:delete")

    ;

    @Getter
    private final String permission;
}
