package com.userbackend.jwt.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.userbackend.jwt.dto.UserError;
import com.userbackend.jwt.dto.UserFieldError;



import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;


public class JWTAuthorizationFilter extends OncePerRequestFilter {

	private final String HEADER = "Authorization";
	private final String PREFIX = "Bearer ";
	private final String SECRET = "mySecretKey";
	private final String URL_SIGN_UP = "/sign-up";



	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
		String servletPath="";
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.setCharacterEncoding("UTF-8");		
		try {
			if (checkJWTToken(request, response)) {
				Claims claims = validateToken(request);
				if (claims.get("authorities") != null) {
					setUpSpringAuthentication(claims);
				} else {
					SecurityContextHolder.clearContext();
						
					String json =getErrorResponse("authorities no válido");
					PrintWriter out = response.getWriter();
        			out.print(json);
            		out.flush();

					return;
				}
			} else {
				SecurityContextHolder.clearContext();	
				servletPath = request.getServletPath();
				if (!servletPath.equals(URL_SIGN_UP)){
					String json =getErrorResponse("Token no válido");
					PrintWriter out = response.getWriter();
        			out.print(json);
            		out.flush();

					return;
				}
			}
			chain.doFilter(request, response);
		
		} catch (Exception e) {	

			String json =getErrorResponse(e.getMessage());
			PrintWriter out = response.getWriter();
        	out.print(json);
            out.flush();

			return;
		}
	}	

	private Claims validateToken(HttpServletRequest request) {
		String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
		return Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(jwtToken).getBody();
	}

	private void setUpSpringAuthentication(Claims claims) {
		@SuppressWarnings("unchecked")
		List<String> authorities = (List<String>) claims.get("authorities");

		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
				authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
		SecurityContextHolder.getContext().setAuthentication(auth);

	}

	private boolean checkJWTToken(HttpServletRequest request, HttpServletResponse res) {
		String authenticationHeader = request.getHeader(HEADER);
		if (authenticationHeader == null || !authenticationHeader.startsWith(PREFIX))
			return false;
		return true;
	}

	private String getErrorResponse(String message) throws JsonProcessingException {
		
			String json="";
			LocalDateTime now = LocalDateTime.now();
			String nowString = "\""+now.toString()+"\"";

			UserError userError= new UserError();
         	List<UserFieldError> errors = new ArrayList<>();
         	UserFieldError userFieldError = new UserFieldError();
    
            userFieldError.setDetail (message);
            userFieldError.setCodigo(HttpServletResponse.SC_FORBIDDEN);
            errors.add(userFieldError);
            userError.setErrors(errors); 

			ObjectMapper objectMapper = new ObjectMapper();
            json = objectMapper.writeValueAsString(userError);
			json=json.replace("\"timestamp\":null","\"timestamp\":"+nowString);

			return json;

	}

}
