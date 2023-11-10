package com.bpdev.hotelservice;

import com.bpdev.hotelservice.entities.Hotel;
import com.bpdev.hotelservice.entities.embedabbles.Address;
import com.bpdev.hotelservice.entities.enums.Amenities;
import com.bpdev.hotelservice.repositories.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.Arrays;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private HotelRepository hotelRepository;

    @Override
    public void run(String... args) throws Exception {
        createHotel();
    }

    private void createHotel() {
        Hotel hotel = Hotel.builder()
                .name("Fake Hotel")
                .address(new Address("123 Main St", "Cityville", "Stateville", "12345"))
                .stars(4)
                .email("fakehotel@example.com")
                .standardCheckoutTime(LocalTime.of(12, 0))
                .amenities(Arrays.asList(Amenities.Wifi, Amenities.Pool))
                .lateCheckoutFee(20)
                .build();
        Hotel savedHotel = hotelRepository.save(hotel);
        System.out.println("Hotel saved: " + savedHotel.getId());
    }
}