package by.softarex.internship.task.questionnaireportalsystem.security;

import by.softarex.internship.task.questionnaireportalsystem.exception.JwtAuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private final UserDetailsService userDetailsService;
    @Value("${jwt.expiration}")
    private Long validityTime;
    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.header}")
    private String httpHeader;

    public JwtTokenProvider(@Qualifier("userService") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public String createToken(String nickname, String id) {
        Claims claims = Jwts.claims().setSubject(nickname);
        claims.put("id", id);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityTime * 1000);
        return buildToken(claims, now, validity);
    }

    public boolean validateToken(String token) {
        try {
            if (token.trim().length() == 0) {
                throw new JwtAuthenticationException("JWT token is not valid");
            }
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token.split(" ")[1]);
            return isTokenValid(claimsJws);
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtAuthenticationException("JWT token is not valid");
        }
    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader(httpHeader);
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getEmail(token.split(" ")[1]));
        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), Collections.emptyList());
    }

    @PostConstruct
    protected void init() {
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    private String getEmail(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    private boolean isTokenValid(Jws<Claims> claimsJws) {
        return !claimsJws.getBody().getExpiration().before(new Date());
    }

    private String buildToken(Claims claims, Date now, Date validity) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
