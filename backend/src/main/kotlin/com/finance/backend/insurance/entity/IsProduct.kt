package com.finance.backend.insurance.entity

import javax.persistence.*

@Entity(name = "Is_product")
class IsProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false, columnDefinition = "INT UNSIGNED")
    var id: Long = 0
        protected set

    @Column(length = 14)
    var isPdType : String = ""
        protected set
    @Column(length = 60)
    var isPdName : String = ""
        protected set
}