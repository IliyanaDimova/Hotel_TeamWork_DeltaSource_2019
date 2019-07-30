package eu.deltasource.internship.hotel.controllers;

import eu.deltasource.internship.hotel.domain.Guest;
import eu.deltasource.internship.hotel.service.GuestService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("guests")

public class GuestControllers {
    GuestService guestService = new GuestService();

    @RequestMapping(value = "/{id}", method = GET)
    @ResponseBody
    public Guest returnGuestById(@PathVariable int id){
        return guestService.returnGuestById(id);
    }

}
