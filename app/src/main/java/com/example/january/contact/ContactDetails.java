package com.example.january.contact;

/**
 * Created by january on 18-5-9.
 */

public class ContactDetails {
    private String name;
    private String first_char;

    public ContactDetails(String _name, String _first_char){
        name = _name;
        first_char = _first_char;
    }

    public String getName(){
        return name;
    }

    public String getFirstChar(){
        return first_char;
    }

}
