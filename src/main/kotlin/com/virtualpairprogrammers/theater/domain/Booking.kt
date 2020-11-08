package com.virtualpairprogrammers.theater.domain

import javax.persistence.*

@Entity
data class Booking(
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long,
        val customerName: String
) {
    // relationship
    // multi seats can exists in one booking
    @ManyToOne
    lateinit var seat: Seat
    @ManyToOne
    lateinit var  performance: Performance
}