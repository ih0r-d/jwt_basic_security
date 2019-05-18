package io.pyxis.api.security;

import io.pyxis.api.exception.CustomJwtTokenException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@AllArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain filter) throws ServletException, IOException {
        String token = tokenProvider.resolveToken(req);
        try {
            if (token != null && tokenProvider.isValid(token)){
                SecurityContextHolder.getContext().setAuthentication(tokenProvider.getAuth(token));
            }
        }catch (CustomJwtTokenException e){
            SecurityContextHolder.clearContext();
            resp.sendError(e.getHttpStatus().value(),e.getMessage());
        }
        filter.doFilter(req, resp);
    }
}
