package com.golfzonTech4.worktalk.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

@Slf4j
public class SecurityUtil {


   private SecurityUtil() {}

   /**
    * Security Context의 Authentication 객체를 이용해 username을 리턴해주는 메서드
    */
   public static Optional<String> getCurrentUsername() {
      // Security context에 Authenrication객체가 저장되는 시점
      // JwtFilter의 doFilter메서드에서 request가 들어올때
      // SecurityContext에 Authentication 객체를 저장해서 사용하게 된다.
      final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

      if (authentication == null) {
         log.debug("Security Context에 인증 정보가 없습니다.");
         return Optional.empty();
      }

      String username = null;
      if (authentication.getPrincipal() instanceof UserDetails) {
         UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
         username = springSecurityUser.getUsername();
      } else if (authentication.getPrincipal() instanceof String) {
         username = (String) authentication.getPrincipal();
      }

      return Optional.ofNullable(username);
   }
}
