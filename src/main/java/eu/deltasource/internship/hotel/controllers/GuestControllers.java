package eu.deltasource.internship.hotel.controllers;

import eu.deltasource.internship.hotel.domain.Gender;
import eu.deltasource.internship.hotel.domain.Guest;
import eu.deltasource.internship.hotel.repository.GuestRepository;
import eu.deltasource.internship.hotel.service.GuestService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("guests")
public class GuestControllers {
    GuestRepository guestRepository = new GuestRepository();
    GuestService guestService = new GuestService(guestRepository);

    @GetMapping
    @ResponseBody
    public List<Guest> returnAllGuests(){
        return guestService.returnAllGuests();
    }

    @PostMapping
    @ResponseBody
    public void returnReturnAllGuests(@PathVariable String firstName, @PathVariable String lastName, @PathVariable Gender gender){
        guestService.createGuest(firstName, lastName, gender);
    }

    @RequestMapping(value = "/", method = GET)
    @ResponseBody
    public void returnReturnAllGuess(@PathVariable String firstName, @PathVariable String lastName, @PathVariable Gender gender){
        guestService.createGuest(firstName, lastName, gender);
    }
}
