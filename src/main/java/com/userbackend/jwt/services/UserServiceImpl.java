package com.userbackend.jwt.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import com.userbackend.jwt.dto.PhoneRequestDTO;
import com.userbackend.jwt.dto.UserFieldError;
import com.userbackend.jwt.dto.UserRequestDTO;
import com.userbackend.jwt.dto.UserResponseDTO;
import com.userbackend.jwt.entity.Phone;
import com.userbackend.jwt.entity.User;
import com.userbackend.jwt.exception.InvalidEmailException;
import com.userbackend.jwt.exception.InvalidPasswordException;
import com.userbackend.jwt.exception.InvalidTokenException;
import com.userbackend.jwt.exception.UserExistException;
import com.userbackend.jwt.exception.UserNotExistException;
import com.userbackend.jwt.repository.UserRepository;
import com.userbackend.jwt.utils.UtilToken;
import com.userbackend.jwt.utils.UtilValidation;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class UserServiceImpl implements UserService{

    @Value("${capital.regex}")
    private String capitalRegex;

    @Value("${numbers.regex}")
    private String numbersRegex;

    @Value("${email.regex1}")
    private String emailRegex1;

    @Value("${email.regex2}")
    private String emailRegex2;

    public String getCapitalRegex() {
        return capitalRegex;
    }

    public void setCapitalRegex(String capitalRegex) {
        this.capitalRegex = capitalRegex;
    }

    public String getNumbersRegex() {
        return numbersRegex;
    }

    public void setNumbersRegex(String numbersRegex) {
        this.numbersRegex = numbersRegex;
    }

    public String getEmailRegex1() {
        return emailRegex1;
    }

    public void setEmailRegex1(String emailRegex1) {
        this.emailRegex1 = emailRegex1;
    }

    public String getEmailRegex2() {
        return emailRegex2;
    }

    public void setEmailRegex2(String emailRegex2) {
        this.emailRegex2 = emailRegex2;
    }


    private  UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    @Transactional(readOnly=true)
    @Override
    public Optional<User> findByEmail(String email) {

        Optional<User> userOptional= userRepository.findByEmail(email);
        return userOptional;
    }


    @Transactional
    public UserResponseDTO save(UserRequestDTO userRequestDTO){

        if( userRequestDTO.getPassword()==null || userRequestDTO.getPassword().equals("")){
            throw new InvalidPasswordException("password no puede ser nulo ni blanco");
        }
        if(userRequestDTO.getEmail()==null || userRequestDTO.getEmail().equals("")){
            throw new InvalidEmailException("email no puede ser nulo ni blanco");
        }
        if( userRequestDTO.getPassword().trim().length()<8||userRequestDTO.getPassword().trim().length()>12){
            throw new InvalidPasswordException("password debe tener nínimo 8 y máximo 12 caracteres");
        }
        UtilValidation util = new UtilValidation();
        boolean isValidPass=util.isValidPass(userRequestDTO.getPassword(), capitalRegex,numbersRegex );
        Boolean isValidEmail=util.isValidEmail(userRequestDTO.getEmail(),emailRegex1+emailRegex2);

        if(!isValidPass||!isValidEmail){
            if(!isValidPass) {
                throw new InvalidPasswordException("password debe tener una Mayúscula y dos números");            
            }  
             if(!isValidEmail) {
                throw new InvalidEmailException("email el formato no es válido");
            } 
        }

        Optional<User> userOptional= userRepository.findByEmail(userRequestDTO.getEmail());

        if (userOptional.isPresent()) {
            throw new UserExistException("email de usuario ya existe en la base de datos");  
        }

        UserResponseDTO userResponse = new UserResponseDTO();
        LocalDateTime now = LocalDateTime.now();

        User user = new User(userRequestDTO.getName(),userRequestDTO.getEmail(), userRequestDTO.getPassword());
        List<Phone> phones = new ArrayList<>();

        if (userRequestDTO.getPhones().size()!=0){
                userRequestDTO.getPhones().forEach(phoneRequestDTO -> {
                Phone phone = new Phone(phoneRequestDTO.getNumber(), phoneRequestDTO.getCitycode(), phoneRequestDTO.getCountrycode()
                ,user);
                phones.add(phone);
            });
        }


        user.setPhones(phones);
        user.setCreated(now);
        user.setModified(now);
        
        user.setActive(true);
        String token = getJWTToken(userRequestDTO.getEmail());
        user.setToken(token);
        User userBD =userRepository.save(user);

        userResponse.setId(userBD.getId());
        userResponse.setCreated(userBD.getCreated());
        userResponse.setModified(userBD.getModified());
        userResponse.setLastLogin(userBD.getLastLogin());
        userResponse.setActive(userBD.isActive());
        userResponse.setName(userBD.getName());
        userResponse.setEmail(userBD.getEmail());
        userResponse.setPassword(userBD.getPassword());

        List<PhoneRequestDTO> phonesDB = new ArrayList<>();
         if (userBD.getPhones().size()!=0){
            userBD.getPhones().forEach(phone -> {
                PhoneRequestDTO phoneRequestDB = new PhoneRequestDTO(phone.getNumber(),phone.getCitycode(), phone.getCountrycode());
                phonesDB.add(phoneRequestDB);
            });
         }

        userResponse.setPhones(phonesDB);
        userResponse.setToken(token);

        return userResponse;

    }

    @Transactional
    public Optional<UserResponseDTO> loadUser( String authorizationHeader){

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
           throw new InvalidTokenException("Token no válido");
        }

        Optional<UserResponseDTO> userResponseOptional =null;
        UserResponseDTO userResponse=null;

        String email=UtilToken.getEmailFromToken(authorizationHeader);
        if (email == null || email.equals("")) {
           throw new InvalidTokenException("Token no válido");
        }
        Optional<User> userOptional=userRepository.findByEmail(email);
        if (userOptional.isPresent()) {

            userResponse = new UserResponseDTO();
            User userDB = userOptional.get();


            if (!UtilToken.getToken(authorizationHeader).equals(userDB.getToken())){
               throw new InvalidTokenException("Token no válido");
            }

            LocalDateTime now = LocalDateTime.now();
            userDB.setModified(now);
            userDB.setLastLogin(now);
            String token = getJWTToken(email);
            userDB.setToken(token);
            userRepository.save(userDB);

            userResponse.setId(userDB.getId());
            userResponse.setCreated(userDB.getCreated());
            userResponse.setModified(userDB.getModified());
            userResponse.setLastLogin(userDB.getLastLogin());
            userResponse.setActive(userDB.isActive());
            userResponse.setName(userDB.getName());
            userResponse.setEmail(userDB.getEmail());
            userResponse.setPassword(userDB.getPassword());

            List<PhoneRequestDTO> phonesDB = new ArrayList<>();
            if (userDB.getPhones().size()!=0){
                userDB.getPhones().forEach(phone -> {
                    PhoneRequestDTO phoneRequestDB = new PhoneRequestDTO(phone.getNumber(),phone.getCitycode(), phone.getCountrycode());
                    phonesDB.add(phoneRequestDB);
                });
            }

            userResponse.setPhones(phonesDB);
            userResponse.setToken(token);

            userResponseOptional= Optional.of(userResponse);

            return userResponseOptional;

        }   else{
              throw new UserNotExistException("email de usuario no existe en la base de datos");
          
        }

    }


    private String getJWTToken(String username) {
		String secretKey = "mySecretKey";
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils
				.commaSeparatedStringToAuthorityList("ROLE_USER");
		
		String token = Jwts
				.builder()
				.setSubject(username)
				.claim("authorities",
						grantedAuthorities.stream()
								.map(GrantedAuthority::getAuthority)
								.collect(Collectors.toList()))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 60000))//1 minuto
				.signWith(SignatureAlgorithm.HS512,
						secretKey.getBytes()).compact();

		return token;
	}
}
