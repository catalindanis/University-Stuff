//package controllers;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.messaging.simp.user.SimpUser;
//import org.springframework.messaging.simp.user.SimpUserRegistry;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.web.bind.annotation.*;
//import security.CustomUserDetailsService;
//import security.JwtUtil;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//@RestController
//@RequestMapping("/auth")
//public class AuthController {
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private CustomUserDetailsService userDetailsService;
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    @Autowired
//    private SimpUserRegistry simpUserRegistry;
//
//    @PostMapping("/login")
//    public ResponseEntity login(@RequestBody Map<String, String> authenticationRequest) throws Exception {
//        String nickname = authenticationRequest.get("nickname");
//
//        try {
//            authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(nickname, "")
//            );
//        } catch (BadCredentialsException e) {
//            return ResponseEntity.status(401).body("Incorrect nickname");
//        }
//
//        final UserDetails userDetails = userDetailsService
//                .loadUserByUsername(nickname);
//
//        final String jwt = jwtUtil.generateToken(userDetails.getUsername());
//
//        return ResponseEntity.ok(Map.of("token", jwt));
//    }
//
//    @GetMapping("/connected-users")
//    public Map<String, Object> getConnectedUsers() {
//        Set<SimpUser> users = simpUserRegistry.getUsers();
//
//        List<Map<String, Object>> userList = users.stream()
//                .map(user -> {
//                    Map<String, Object> userMap = new HashMap<>();
//                    userMap.put("username", user.getName());
//                    userMap.put("sessions", user.getSessions().size());
//                    return userMap;
//                })
//                .toList();
//
//        return Map.of("count", users.size(), "users", userList);
//    }
//
//    @GetMapping("/current-user-details")
//    public ResponseEntity<?> getCurrentUserDetails(@AuthenticationPrincipal UserDetails userDetails) {
//        return ResponseEntity.ok(userDetails);
//    }
//}
