package com.organizer.auth.userauth.security;

import com.google.gson.Gson;
import com.organizer.auth.userauth.model.UserModel;
import com.organizer.auth.userauth.services.Authservice;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class AuthFilter extends UsernamePasswordAuthenticationFilter {

    private Authservice authservice;
    private Environment environment;

    public AuthFilter(AuthenticationManager authenticationManager, Authservice authservice, Environment environment){
        this.authservice = authservice;
        this.environment = environment;
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            String req = request.getReader().lines().collect(Collectors.joining());
            UserModel userCredentials = new Gson().fromJson(req, UserModel.class);

            //Collection<? extends GrantedAuthority> authorities = CalendarUserAuthorityUtils.createAuthorities(user);

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    userCredentials.getEmail(),
                    userCredentials.getPassword(),
                    new ArrayList<>()
            );

            Authentication auth = getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);

            return auth;

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {

        //userName == email
        String userName = ((User) authResult.getPrincipal()).getUsername();
        UserModel userDetails = authservice.getUserDetails(userName);

        String token = Jwts.builder()
                .setSubject(userDetails.getEmail())
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(environment.getProperty("token.expiration_time"))))
                .signWith(SignatureAlgorithm.HS512, "secret-key" )
                .compact();

        response.addHeader("token", token);
        response.addHeader("userId", userDetails.getId());

    }
}
