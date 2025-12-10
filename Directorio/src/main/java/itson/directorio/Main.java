/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package itson.directorio;

import itson.directorio.implementacion.Directorio;
import itson.directorio.interfaces.IDirectorio;
import java.util.Arrays;

/**
 *
 * @author Dana Chavez
 */
public class Main {

    public static void main(String[] args) {
        IDirectorio dir = new Directorio();

        dir.registerClient("p1", "192.168.0.10", 9001);
        dir.registerClient("p2", "192.168.0.11", 9002);

        System.out.println("p1 -> " + dir.getEndpoint("p1"));
        System.out.println("p2 -> " + dir.getEndpoint("p2"));

        System.out.println("bulk: " + dir.getEndpoints(Arrays.asList("p1", "p2", "noExiste")));

        boolean removed = dir.removeClient("p2");
        System.out.println("removed p2: " + removed);
    }
}
