package io.pyxis.api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.pyxis.api.domain.model.Role;
import io.pyxis.api.exception.CustomJwtTokenException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.token.expiration}")
    private long tokenExpiration;

    @Value("jwt.token.secret")
    private String key;

    private final String TOKEN_PREFIX = "Bearer ";
    private final String HEADER_STRING = "Authorization";


    private final JwtUserDetailsService userDetailsService;

    @PostConstruct
    protected void init(){
        key = encodeSecretKey(key);
    }

    public String createToken(String name, List<Role> roles){
        Claims subject = Jwts.claims().setSubject(name);
        subject.put("roles", roles.stream().map(s->s.getName()).collect(Collectors.toList()));

        Date current = new Date();

        return Jwts.builder()
                .setClaims(subject)
                .setIssuedAt(current)
                .setExpiration(new Date(current.getTime() + tokenExpiration))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();

    }

    public String resolveToken(HttpServletRequest req) {
        String token = req.getHeader(HEADER_STRING);
        if (token != null && token.startsWith(TOKEN_PREFIX)) {
            return token.substring(7);
        }
        return null;
    }

    private String getUsernameByToken(String token){
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public Authentication getAuth(String token){
        UserDetails details = userDetailsService.loadUserByUsername(getUsernameByToken(token));
        return new UsernamePasswordAuthenticationToken(details,"",details.getAuthorities());
    }


    public boolean isValid(String token) throws AuthenticationException {
        try {
            Date expiredToken = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration();
            return expiredToken.before(new Date());
        } catch (JwtException | IllegalArgumentException e){
            throw new CustomJwtTokenException("JWT token is invalid or expired", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private String encodeSecretKey(String key){
        return Base64.getEncoder().encodeToString(key.getBytes());
    }

}
