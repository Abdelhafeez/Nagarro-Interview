package com.nagarro.interview.security.services;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nagarro.security.entities.Role;
import com.nagarro.security.entities.User;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	 Map<String, User> users = new HashMap<>();

	 @Autowired
	 PasswordEncoder encoder;
 
	 @PostConstruct 
	 void loadDummyUsers()
	 {
		 users = new HashMap<>();
		// Set<Role> roles = new Set<Role>();
		 
		Set<Role> admin= (new HashSet<Role>());
		admin.add(new Role(1l,"ROLE_ADMIN"));
		Set<Role> user= (new HashSet<Role>());
		user.add(new Role(2l,"ROLE_USER"));
		 users.put("testadmin", new User("testadmin",admin, encoder.encode("adminpassword")));
		 users.put("user", new User("testUser",user, encoder.encode("userpassword")));

	 }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        User user =  users
    		      .values()
    		      .stream()
    		      .filter(u -> Objects.equals(username, u.getUsername()))
    		      .findFirst().orElseThrow(() ->
                new UsernameNotFoundException("Could not find user with the following username: " +username));
    	System.out.println("user"+user);

        return UserPrinciple.build(user);
    }

}