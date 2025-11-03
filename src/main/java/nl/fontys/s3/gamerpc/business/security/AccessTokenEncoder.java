package nl.fontys.s3.gamerpc.business.security;

public interface AccessTokenEncoder {
    String encode(AccessToken accessToken);
}
