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
    LinkedList<Integer> blackListOcurrences=new LinkedList<>();
    HostBlackListsValidator obj;
    
    public HostBlackListThread(String ip, HostBlacklistsDataSourceFacade servers, int i, int f, HostBlackListsValidator o){
        this.ipaddress = ip;
        this.skds = servers;
        this.ini = i;
        this.fin = f;
        this.obj = o;
    }
    
    public void run(){
        //System.out.println(ini+" - "+fin);
        for (int i=ini;i<fin;i++){
            
            if (skds.isInBlackListServer(i, ipaddress)){
                obj.incrementOcurrences();
                blackListOcurrences.add(i);
            }
        }
    }
    
    public LinkedList<Integer> getBlackListOcurrences(){
        return blackListOcurrences;
    }
    
    public int getRange() {
        return blackListOcurrences.size();
    }
}
