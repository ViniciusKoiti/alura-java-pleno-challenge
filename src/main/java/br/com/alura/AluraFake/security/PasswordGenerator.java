package br.com.alura.AluraFake.security;

import java.util.Random;

public class PasswordGenerator {

    public static String generatePassword() {
        Random random = new Random();
        int password = 100000 + random.nextInt(900000);
        return String.valueOf(password);
    }
}