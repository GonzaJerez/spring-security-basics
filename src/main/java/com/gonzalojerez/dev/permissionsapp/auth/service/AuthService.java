package com.gonzalojerez.dev.permissionsapp.auth.service;

import com.gonzalojerez.dev.permissionsapp.auth.dto.LoginDto;
import com.gonzalojerez.dev.permissionsapp.exceptions.BadRequestException;
import com.gonzalojerez.dev.permissionsapp.users.dto.CreateUserDto;
import com.gonzalojerez.dev.permissionsapp.users.model.User;
import com.gonzalojerez.dev.permissionsapp.users.service.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {
    @Value("${jwt.secret}")
    String jwtSecret;

    @Value("${oauth.google.client_id}")
    private String GOOGLE_CLIENT_ID;

    @Value("${oauth.github.client_id}")
    private String GITHUB_CLIENT_ID;

    @Value("${oauth.github.secret}")
    private String GITHUB_SECRET;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserService userService;

    public Map<String,Object> login(LoginDto loginDto) {
        String email = loginDto.getEmail();
        String password = loginDto.getPassword();

        User user = userService.findOneByEmail(email).orElseThrow();

        boolean isValidPassword = passwordEncoder.matches(password, user.getPassword());

        if (!isValidPassword) {
            throw new BadRequestException("Credenciales no válidas");
        }

        Map<String,Object> loginResponse = new HashMap<>();
        loginResponse.put("token", generateToken(email));
        loginResponse.put("user", user);

        return loginResponse;

//        return generateToken(email);
    }

    public Map<String,Object> googleSignIn(String googleToken) {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                .setAudience(Collections.singletonList(GOOGLE_CLIENT_ID))
                .build();

        GoogleIdToken idToken = null;
        try {
            idToken = verifier.verify(googleToken);
        } catch (GeneralSecurityException | IOException e) {
            System.out.println(e.getMessage());
        }
        if (idToken == null) {
            throw new BadRequestException("No se pudo autenticar al usuario con el token" + googleToken);
        }

        GoogleIdToken.Payload googlePayload = idToken.getPayload();
        System.out.println(googlePayload);
        String email = googlePayload.getEmail();

        Optional<User> userFound = userService.findOneByEmail(email);
        User user;

        if (userFound.isEmpty()) {
            CreateUserDto createUserDto = new CreateUserDto();
            createUserDto.setEmail(email);
            createUserDto.setName((String) googlePayload.get("name"));
            createUserDto.setAuthTypes("google");
            user = userService.create(createUserDto);
        } else {
            user = userFound.get();
        }

        Map<String,Object> loginResponse = new HashMap<>();
        loginResponse.put("token", generateToken(email));
        loginResponse.put("user", user);

        return loginResponse;
    }

    public Map<String,Object> githubSignIn(String githubCode) {
        String accessTokenUrl = "https://github.com/login/oauth/access_token";
        RestTemplate restTemplate = new RestTemplate();

        Map<String, String> body = new HashMap<>();
        body.put("client_id", this.GITHUB_CLIENT_ID);
        body.put("client_secret", this.GITHUB_SECRET);
        body.put("code", githubCode);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body);

        ResponseEntity<Map> response = restTemplate.postForEntity(accessTokenUrl, request, Map.class);
        Map responseBody = response.getBody();
        
        if (responseBody == null) {
            throw new BadRequestException("No se pudo obtener la información con el código proporcionado por el cliente");
        }
        String accessToken = (String) response.getBody().get("access_token");

        String userInfoUrl = "https://api.github.com/user";

        HttpHeaders authHeaders = new HttpHeaders();
        authHeaders.setBearerAuth(accessToken);

        HttpEntity<String> userInfoRequest = new HttpEntity<>(authHeaders);
        ResponseEntity<Map> userInfoResponse = restTemplate.exchange(userInfoUrl, HttpMethod.GET, userInfoRequest, Map.class);
        Map userInfo = userInfoResponse.getBody();

        if (userInfo == null) {
            throw new BadRequestException("No se pudo autenticar al usuario.");
        }

        String username = (String) userInfo.get("login");

        Optional<User> userFound = userService.findOneByUsername(username);
        User user;

        if (userFound.isEmpty()) {
            CreateUserDto createUserDto = new CreateUserDto();
            createUserDto.setUsername(username);
            createUserDto.setName((String) userInfo.get("name"));
            createUserDto.setEmail((String) userInfo.get("email"));
            createUserDto.setAuthTypes("github");
            user = userService.create(createUserDto);
        } else {
            user = userFound.get();
        }

        Map<String,Object> loginResponse = new HashMap<>();
        loginResponse.put("token", generateToken(username));
        loginResponse.put("user", user);

        return loginResponse;
    }

    public String generateToken(String principal) {
        return Jwts
                .builder()
                .claim("email", principal)
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .compact();
    }
}
