/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.LinkedList;
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
    int ocurrencesCount = 0;
    LinkedList<Integer> blackListOcurrences=new LinkedList<>();
    
    public HostBlackListThread(String ip, HostBlacklistsDataSourceFacade servers, int i, int f){
        this.ipaddress = ip;
        this.skds = servers;
        this.ini = i;
        this.fin = f;
    }
    
    public void run(){
        //System.out.println(ini+" - "+fin);
        for (int i=ini;i<fin;i++){
            
            if (skds.isInBlackListServer(i, ipaddress)){
                blackListOcurrences.add(i);
                
                ocurrencesCount++;
            }
        }
    }
    
    public int getOcurrencesCount(){
        return ocurrencesCount;
    }
    
    public LinkedList<Integer> getBlackListOcurrences(){
        return blackListOcurrences;
    }
    
    public int getRange() {
        return fin - ini;
    }
}
