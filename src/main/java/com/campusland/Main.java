package com.campusland;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {

        LocalDateTime fecha = LocalDateTime.now();

        System.out.println("Fecha: " + fecha.getMonth().getValue());

    }
}
