package com.formula1.simulacion;

import java.util.Random;

public enum Clima {
    SECO, LLUVIOSO, EXTREMO;

    // Genera un clima aleatorio para la sesión (RF20)
    public static Clima generarAleatorio() {
        Clima[] valores = Clima.values();
        Random random = new Random();
        return valores[random.nextInt(valores.length)];
    }
}
