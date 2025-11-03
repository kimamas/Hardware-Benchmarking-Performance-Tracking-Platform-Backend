package nl.fontys.s3.gamerpc.business.security;

public interface AccessTokenDecoder {
    AccessToken decode(String accessTokenEncoded);
}
