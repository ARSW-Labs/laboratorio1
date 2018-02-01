/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

/**
 *
 * @author estudiante
 */
public class HostBlackListThread extends Thread{
    
    String ipaddress;
    HostBlacklistsDataSourceFacade skds;
    int ini;
    int fin;
    int limitCount;
    int i;
    AtomicInteger ocurrencesCount;
    AtomicBoolean confiable;
    LinkedList<Integer> blackListOcurrences=new LinkedList<>();
    
    public HostBlackListThread(String ip, HostBlacklistsDataSourceFacade servers, int in, int f, AtomicInteger ocuC, AtomicBoolean cnf, int limit){
        this.ipaddress = ip;
        this.skds = servers;
        this.ini = in;
        this.fin = f;
        this.ocurrencesCount = ocuC;
        this.limitCount = limit;
        this.confiable = cnf;
    }
    
    public void run(){
        //System.out.println(ini+" - "+fin);
        for (i=ini;i<fin && confiable.get();i++){
            //System.out.println(i);
            if (skds.isInBlackListServer(i, ipaddress)){
                blackListOcurrences.add(i);
                if(ocurrencesCount.incrementAndGet() >= limitCount) {
                    confiable.set(false);
                }
            }
        }
        //System.out.println(i-ini);
    }
    
    public LinkedList<Integer> getBlackListOcurrences(){
        return blackListOcurrences;
    }
    
    public int getRange() {
        return i-ini;
    }
}
