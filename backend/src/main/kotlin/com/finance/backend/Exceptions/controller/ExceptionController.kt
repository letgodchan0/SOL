package com.finance.backend.Exceptions.controller

import com.finance.backend.Exceptions.*
import org.springframework.http.ResponseEntity
import org.springframework.kafka.listener.ListenerExecutionFailedException
import org.springframework.web.bind.MissingRequestHeaderException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import javax.naming.AuthenticationException

@ControllerAdvice
class ExceptionController {

    @ExceptionHandler(InsufficientBalanceException::class)
    fun handleInsufficient(e : Exception) : ResponseEntity<String> {
        return ResponseEntity.status(400).body("Out of Balance")
    }

    @ExceptionHandler(AccountNotSubToUserException::class)
    fun handleAccountNotSubToUser(e : Exception) : ResponseEntity<String> {
        return ResponseEntity.status(403).body(e.message)
    }

    @ExceptionHandler(DuesNotExistsException::class)
    fun handleDuesNotExist(e : Exception) : ResponseEntity<String> {
        return ResponseEntity.status(404).body("Internal Server Error")
    }

    @ExceptionHandler(DuplicatedPhoneNumberException::class)
    fun handleDuplicatedPhoneNumberException(e : Exception) : ResponseEntity<String> {
        return ResponseEntity.status(400).body(e.message)
    }

    @ExceptionHandler(DuplicatedUserException::class)
    fun handleDuplicatedUser(e : Exception) : ResponseEntity<String> {
        return ResponseEntity.status(409).body(e.message)
    }

    @ExceptionHandler(InvalidPasswordException::class)
    fun handleInvalidPassword(e : Exception) : ResponseEntity<String> {
        return ResponseEntity.status(401).body("Invalid Password")
    }

    @ExceptionHandler(NoProfileException::class)
    fun handleNoProfile(e : Exception) : ResponseEntity<String> {
        return ResponseEntity.status(408).body("Unexpected Server Error")
    }

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNoSuchElementException(e : Exception) : ResponseEntity<String> {
        return ResponseEntity.status(404).body("Cannot Found")
    }

    @ExceptionHandler(TokenExpiredException::class)
    fun handleTokenExpired(e : Exception) : ResponseEntity<String> {
        return ResponseEntity.status(403).body("Token Expired")
    }

    @ExceptionHandler(WrongAmountException::class)
    fun handleWrongAmount(e : Exception) : ResponseEntity<String> {
        return ResponseEntity.status(412).body(e.message)
    }

    @ExceptionHandler(NullPointerException::class)
    fun handleNullPointer(e : Exception) : ResponseEntity<String> {
        println("e "+ e)
        return ResponseEntity.status(404).body("Unexpected Server Error")
    }

    @ExceptionHandler(AuthenticationException::class)
    fun handleAuthentication(e : Exception) : ResponseEntity<String> {
        return ResponseEntity.status(403).body("Cannot Access")
    }

    @ExceptionHandler(NoAccountException::class)
    fun handleNoAccount(e : Exception) : ResponseEntity<String> {
        return ResponseEntity.status(404).body("Cannot Found")
    }
    @ExceptionHandler(NoTradeHistoryException::class)
    fun handleNoTradeHistory(e: Exception) : ResponseEntity<String>{
        return ResponseEntity.status(404).body("Cannot Found")
    }

    @ExceptionHandler(NoCorporationException::class)
    fun handleNoCorporation(e: Exception) : ResponseEntity<String>{
        return ResponseEntity.status(404).body("Cannot Found")
    }

    @ExceptionHandler(RemitFailedException::class)
    fun handleRemitFailed(e: Exception) : ResponseEntity<String>{
        return ResponseEntity.status(418).body("Remit failed")
    }

    @ExceptionHandler(MissingRequestHeaderException::class)
    fun handleMissingRequestHeaderException(e: Exception) : ResponseEntity<String>{
        return ResponseEntity.status(403).body("Token required")
    }

    @ExceptionHandler(NoCardException::class)
    fun handleNoCardException(e: Exception) : ResponseEntity<String>{
        return ResponseEntity.status(404).body("Cannot Found")
    }

    @ExceptionHandler(ListenerExecutionFailedException::class)
    fun handleListenerExecutionFailedException(e : Exception){
        println("에러 메세지: " + e.message)
    }

    @ExceptionHandler(Exception::class)
    fun handleAll(e : Exception) : ResponseEntity<String> {
        println("에러 메세지: " + e)
        return ResponseEntity.status(500).body("Internal Server Error")
    }
}
