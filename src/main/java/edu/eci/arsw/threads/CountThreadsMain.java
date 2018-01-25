/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.threads;

/**
 *
 * @author hcadavid
 */
public class CountThreadsMain {
    
    public static void main(String a[]){
        CountThread hilo1 = new CountThread(0, 1000);
        CountThread hilo2 = new CountThread(1000,2000);
        CountThread hilo3 = new CountThread(2000, 3000);
        hilo1.run();
        hilo2.run();
        hilo3.run();
    }
    
}
