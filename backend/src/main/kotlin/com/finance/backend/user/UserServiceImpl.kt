package com.finance.backend.user

import com.finance.backend.auth.Exceptions.DuplicatedPhoneNumberException
import com.finance.backend.auth.Exceptions.DuplicatedUserException
import com.finance.backend.auth.Exceptions.InvalidPasswordException
import com.finance.backend.user.Exceptions.InvalidUserException
import com.finance.backend.auth.Exceptions.TokenExpiredException
import com.finance.backend.auth.LoginDTO
import com.finance.backend.auth.LoginDao
import com.finance.backend.auth.Token
import com.finance.backend.auth.SignupDto
import com.finance.backend.common.util.JwtUtils
import lombok.RequiredArgsConstructor
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.text.SimpleDateFormat
import java.util.*

@Service("UserService")
@RequiredArgsConstructor
class UserServiceImpl (
        private val userRepository: UserRepository,
        private val passwordEncoder: BCryptPasswordEncoder,
//        private val authenticationManager: AuthenticationManager,
        private val jwtUtils: JwtUtils
        ) : UserService {
    override fun saveUser(signupDto: SignupDto) : LoginDao {
        if(userRepository.existsByNameAndPhoneAndBirth(signupDto.username, signupDto.phone, SimpleDateFormat("yyyy.MM.dd").parse(signupDto.birth))) throw DuplicatedUserException()
        else if(userRepository.existsByPhone(signupDto.phone)) throw DuplicatedPhoneNumberException();
        else {
            signupDto.password = passwordEncoder.encode(signupDto.password)
            var user : User = signupDto.toEntity()
            // 토큰 발급
            user = userRepository.save(user)
            try {
                val token : Token = jwtUtils.createToken(user.id, user.name, signupDto.type)
                user.accessToken(token.accessToken)
                user.refreshToken(token.refreshToken)
                return userRepository.save(user).toLoginEntity()
            } catch (e : Exception) {
                throw Exception()
            }
        }
    }

    override fun login(loginDto: LoginDTO) : LoginDao? {
        if(try {jwtUtils.refreshValidation(loginDto.refreshToken)} catch (e: Exception) {throw TokenExpiredException()}){
            var userId : UUID = UUID.fromString(jwtUtils.parseUserId(loginDto.refreshToken))
            var user : User = userRepository.findById(userId).orElseGet(null)
            if(user == null) throw InvalidUserException()
            if(passwordEncoder.matches(loginDto.password, user.password)) {
                user.accessToken(jwtUtils.refresh(loginDto.refreshToken))
                return userRepository.save(user).toLoginEntity()
            } else throw InvalidPasswordException()
        } else throw Exception()
    }
}