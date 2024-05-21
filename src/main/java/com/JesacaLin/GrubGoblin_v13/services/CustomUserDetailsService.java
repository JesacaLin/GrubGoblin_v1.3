package com.JesacaLin.GrubGoblin_v13.services;
import com.JesacaLin.GrubGoblin_v13.daos.UserDAO;
import com.JesacaLin.GrubGoblin_v13.models.User;
import eu.fraho.spring.securityJwt.base.dto.JwtUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class CustomUserDetailsService implements UserDetailsService {
   private UserDAO userDAO;
    public CustomUserDetailsService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User dbUser = userDAO.getUserByUsername(username);
        List<String> dbRoles = userDAO.getRolesForUser(username);

        if (dbUser == null) {
            throw new UsernameNotFoundException("User not found");
        }
        JwtUser jwtUser = new JwtUser();
        jwtUser.setUsername(dbUser.getUsername());
        jwtUser.setPassword(dbUser.getPassword());

        List<GrantedAuthority> authorities = new ArrayList<>();

        for (String role : dbRoles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        jwtUser.setAuthorities(authorities);

        jwtUser.setAccountNonExpired(true);
        jwtUser.setAccountNonLocked(true);
        jwtUser.setApiAccessAllowed(true);
        jwtUser.setCredentialsNonExpired(true);
        jwtUser.setEnabled(true);
        return jwtUser;
    }
}