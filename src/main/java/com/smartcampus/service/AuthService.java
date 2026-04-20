// Author: W2151955/ 20241937 / Lakindu Jayathilaka
package com.smartcampus.service;

import com.smartcampus.model.User;
import com.smartcampus.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.mindrot.jbcrypt.BCrypt;

import javax.ws.rs.NotAuthorizedException;
import java.security.Key;
import java.util.Date;

public class AuthService { // logic for login and user registration

    private final UserRepository userRepository = new UserRepository();
    
    private static final Key KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String authenticate(String username, String password) {
        User user = userRepository.findByUsername(username);

        // check password
        if (user == null || !BCrypt.checkpw(password, user.getPassword())) {
            throw new NotAuthorizedException("Bad login");
        }

        // build jwt
        return Jwts.builder()
                .setSubject(username)
                .claim("role", user.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) 
                .signWith(KEY)
                .compact();
    }

    public static Key getSigningKey() {
        return KEY;
    }

    public void register(String username, String password, String role) {
        // hash password before saving
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User user = new User(username, hashedPassword, role);
        userRepository.save(user);
    }
}
