package com.matteojoliveau.plugface.security;

import java.security.Policy;

public class SandboxSecurityManager extends SecurityManager{

    public void grantFileReadPermission() {
        SandboxSecurityPolicy policy = (SandboxSecurityPolicy) Policy.getPolicy();
        System.out.println(policy.getClass());
    }
}
