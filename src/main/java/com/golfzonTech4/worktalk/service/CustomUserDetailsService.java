package com.golfzonTech4.worktalk.service;

import com.golfzonTech4.worktalk.domain.Member;
import com.golfzonTech4.worktalk.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component("userDetailsService")
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

   private final MemberRepository memberRepository;

   @Override
   @Transactional
   public UserDetails loadUserByUsername(final String username) {
      log.info("loadUserByUsername : {}", username);
      Optional<Member> result = memberRepository.findByEmail(username);
      User createdUser = createUser(username, result.get());

      return createdUser;
   }

   private User createUser(String username, Member member) {
      log.info("createUser : {}", username);
      List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
      String authority = member.getMemberType().toString();
      grantedAuthorities.add(new SimpleGrantedAuthority(authority));

      return new User(member.getName(), member.getPw(), grantedAuthorities);
   }
}
