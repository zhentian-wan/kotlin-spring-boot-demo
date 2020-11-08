package com.virtualpairprogrammers.theater.control

import com.virtualpairprogrammers.theater.data.PerformanceRepository
import com.virtualpairprogrammers.theater.data.SeatRepository
import com.virtualpairprogrammers.theater.domain.Booking
import com.virtualpairprogrammers.theater.domain.Performance
import com.virtualpairprogrammers.theater.domain.Seat
import com.virtualpairprogrammers.theater.services.BookingService
import com.virtualpairprogrammers.theater.services.TheaterService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.ModelAndView

@Controller
class MainController {

    @Autowired
    lateinit var theaterService: TheaterService

    @Autowired
    lateinit var bookingService: BookingService

    @Autowired
    lateinit var seatRepository: SeatRepository

    @Autowired
    lateinit var performanceRepository: PerformanceRepository

    @RequestMapping("")
    fun homePage() : ModelAndView {
        val model = mapOf(
                "bean" to CheckAvailabilityBackingBean(),
                "performances" to performanceRepository.findAll(),
                "seatNums" to 1..36,
                "seatRows" to 'A'..'O'
        )
        return ModelAndView("seatBooking", model)
        //return ModelAndView("seatBooking", "bean", CheckAvailabilityBackingBean())
    }

    @RequestMapping("checkAvailability", method= arrayOf(RequestMethod.POST))
    fun checkAvailability(bean: CheckAvailabilityBackingBean): ModelAndView {
        val selectedSeat : Seat = bookingService.findSeat(bean.selectedSeatNum, bean.selectedSeatRow)!!
        val selectedPerformance = performanceRepository.findById(bean.selectedPerformance!!).get() // because findById return Optional<Performance>, we call .get() to get value
        bean.seat = selectedSeat
        bean.performance = selectedPerformance
        val result =  bookingService.isSeatFree(selectedSeat, selectedPerformance)
        bean.available = result
        val model = mapOf(
                "bean" to bean,
                "performances" to performanceRepository.findAll(),
                "seatNums" to 1..36,
                "seatRows" to 'A'..'O'
        )
        return ModelAndView("seatBooking", model)
    }
/*
    @RequestMapping("bootstrap")
    fun createInitialData(): ModelAndView {
        val seats = theaterService.seats
        seatRepository.saveAll(seats)
        return homePage()
    }*/
}

/*
* A typical JavaServer Faces application includes one or more backing beans,
* each of which is a type of JavaServer Faces managed bean that can be associated with
* the components used in a particular page.
* This section introduces the basic concepts of creating, configuring,
* and using backing beans in an application.
* */
class CheckAvailabilityBackingBean() {

    // below must be var because those data are sent back and forward between
    // server and template
    var selectedSeatNum: Int = 1
    var selectedSeatRow: Char = 'A'
    var selectedPerformance: Long? = null
    var customerName: String = ""
    var available: Boolean? = null
    var seat: Seat? = null
    var performance: Performance? = null
    var booking: Booking? = null
}