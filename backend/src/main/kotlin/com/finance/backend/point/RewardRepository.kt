package com.finance.backend.point

import com.finance.backend.user.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface RewardRepository : JpaRepository<Reward, Long> {
    fun findAllByUser(user : User) : List<Reward>?
    fun findAllByUserAndPointLessThan(user: User, point : Int) : List<Reward>?
    fun findAllByUserAndPointGreaterThan(user : User, point : Int) : List<Reward>?
}