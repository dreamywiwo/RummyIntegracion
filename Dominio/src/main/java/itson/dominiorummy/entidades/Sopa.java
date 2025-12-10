/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.dominiorummy.entidades;

import java.util.List;
import java.util.Random;

/**
 *
 * @author Adrián
 */
public class Sopa {

    private final List<Ficha> fichas;
    private final Random random = new Random();

    public Sopa(List<Ficha> fichas) {
        this.fichas = fichas;
    }

    public Ficha tomarFicha() {
        if (fichas.isEmpty()) {
            return null; 
        }
        
        int indice = random.nextInt(fichas.size());
        return fichas.remove(indice);
    }

    public void agregarFicha(Ficha ficha) {
        fichas.add(ficha);
        System.out.println("La ficha ha sido añadida");
    }

    public void descartarFicha(Ficha ficha) {
        fichas.remove(ficha);
    }
    
    public int getFichasRestantes() {
        return fichas.size();
    }

}
