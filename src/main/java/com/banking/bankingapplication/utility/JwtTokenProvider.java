package com.banking.bankingapplication.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.banking.bankingapplication.constant.SecurityConstant;
import com.banking.bankingapplication.domain.UserPrincipal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.banking.bankingapplication.constant.SecurityConstant.AUTHORITIES;
@Component
public class JwtTokenProvider {

    @Value("jwt.secret")
    private String secret;

    public String generateJwtToken(UserPrincipal userPrincipal){
        String claims[]=getClaimsFromUser(userPrincipal);
         return JWT.create().withIssuer(SecurityConstant.GET_ARRAYS_LLC).withAudience(SecurityConstant.GET_ARRAYS_ADMINISTRATION).withIssuedAt(new Date()).withSubject(userPrincipal.getUsername()).
                 withArrayClaim(AUTHORITIES,claims).withExpiresAt(new Date(System.currentTimeMillis()+SecurityConstant.expirationTime))
                 .sign(Algorithm.HMAC512(secret));
    }

    public List<GrantedAuthority>  getAuthorities(String token){
    String[] claims=getClaimsFromToken(token);
    return  Arrays.stream(claims)
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
    }

    public Authentication getAuthentication(String userName, List<GrantedAuthority> authorities, HttpServletRequest request){
        UsernamePasswordAuthenticationToken userPasswordToken= new UsernamePasswordAuthenticationToken(userName,null,authorities);
        userPasswordToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return  userPasswordToken;
    }

    public boolean isTokenValide(String userName, String token){
        JWTVerifier jwtVerifier=getJwtVerifier();
        return StringUtils.isNotEmpty(userName)&& isTokenExpired(token,jwtVerifier);

    }

    public String getsubject (String token) {
        JWTVerifier verifier = getJwtVerifier();
        return verifier.verify(token).getSubject();
    }

    private boolean isTokenExpired(String token, JWTVerifier jwtVerifier) {
        Date expiration =jwtVerifier.verify(token).getExpiresAt();
        return  expiration.before(new Date());
    }

    private String[] getClaimsFromToken(String token) {
        JWTVerifier verifier= getJwtVerifier();
        return verifier.verify(token).getClaim(AUTHORITIES).asArray(String.class);
    }

    private JWTVerifier getJwtVerifier() {
        JWTVerifier jwtVerifier;
        try {
            Algorithm algorithm=Algorithm.HMAC512(secret);
            jwtVerifier=JWT.require(algorithm).withIssuer(SecurityConstant.GET_ARRAYS_LLC).build();

        }catch (JWTVerificationException exception){
            throw new JWTVerificationException(SecurityConstant.TOKEN_CAN_NOT_BE_VERIFIED);
        }
            return jwtVerifier;
    }

    private String[] getClaimsFromUser(UserPrincipal userPrincipal) {
        List<String> authorities= new ArrayList<>();
    for(GrantedAuthority grantedAuthority:userPrincipal.getAuthorities()){
        authorities.add(grantedAuthority.getAuthority());
    }
        return  authorities.toArray(new String[0]);
    }
}
