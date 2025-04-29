package com.cinema_seat_booking.CinemaSeatBooking.unit;
import com.cinema_seat_booking.model.*;


import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;

public class MovieTest {

    @Test
    void TestGettersAndSetters(){
        Movie movie =new Movie();
        
        movie.setTitle("title1");   
        movie.setDuration(1);
        movie.setGenre("horror");
        movie.setCast("cast1");
        ArrayList a = new ArrayList<Screening>();
        movie.setScreenings(a);

        assertEquals("title1",movie.getTitle() );
        assertEquals(1,movie.getDuration());
        assertEquals("cast1",movie.getCast());
        assertEquals("horror",movie.getGenre());

        assertEquals(a,movie.getScreenings());

    }
    
}
