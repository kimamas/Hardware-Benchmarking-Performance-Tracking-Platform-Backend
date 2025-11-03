package nl.fontys.s3.gamerpc.business.security;

import java.util.Set;

public interface AccessToken {
    String getSubject();
    Set<String> getRoles();
    Long getUserId();
}
