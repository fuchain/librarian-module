package com.fpt.edu.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpt.edu.constant.Constant;
import com.fpt.edu.entities.User;
import org.json.JSONObject;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import static com.fpt.edu.security.SecurityConstants.*;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
        try {
            User credentials = new ObjectMapper()
                    .readValue(req.getInputStream(), User.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials.getEmail(),
                            credentials.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth) throws IOException {
        long expireDateUnixTime = System.currentTimeMillis() / 1000L + EXPIRATION_TIME;
        Date expireDate = new Date(expireDateUnixTime * 1000);
		String[] authorities = new String[auth.getAuthorities().size()];
		Iterator<SimpleGrantedAuthority> iterator= (Iterator<SimpleGrantedAuthority>) auth.getAuthorities().iterator();
		int i=0;
		while(iterator.hasNext()){
			authorities[i]=iterator.next().getAuthority();
		}
        String token = JWT.create()
                .withSubject(((UserDetails) auth.getPrincipal()).getUsername())
                .withExpiresAt(expireDate)
				.withArrayClaim(Constant.AUTHORITIES_HEADER, authorities)
                .sign(Algorithm.HMAC512(SECRET.getBytes()));

        res.setContentType("application/json");

        JSONObject tokenObj = new JSONObject();
        tokenObj.put("token", token);
        tokenObj.put("email", ((UserDetails) auth.getPrincipal()).getUsername());
        tokenObj.put("expire", expireDateUnixTime);
        tokenObj.put("authorize", auth.getAuthorities());
        res.getWriter().write(tokenObj.toString());
        res.getWriter().flush();
        res.getWriter().close();
    }
}
