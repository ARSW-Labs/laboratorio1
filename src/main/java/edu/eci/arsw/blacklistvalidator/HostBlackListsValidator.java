/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hcadavid
 */
public class HostBlackListsValidator {

    private static final int BLACK_LIST_ALARM_COUNT=5;
    
    /**
     * Check the given host's IP address in all the available black lists,
     * and report it as NOT Trustworthy when such IP was reported in at least
     * BLACK_LIST_ALARM_COUNT lists, or as Trustworthy in any other case.
     * The search is not exhaustive: When the number of occurrences is equal to
     * BLACK_LIST_ALARM_COUNT, the search is finished, the host reported as
     * NOT Trustworthy, and the list of the five blacklists returned.
     * @param ipaddress suspicious host's IP address.
     * @return  Blacklists numbers where the given host's IP address was found.
     */
    public List<Integer> checkHost(String ipaddress, int N) throws InterruptedException{
        
        boolean multiplo = false;
        
        LinkedList<Integer> blackListOcurrences=new LinkedList<>();
        
        HostBlacklistsDataSourceFacade skds=HostBlacklistsDataSourceFacade.getInstance();
        
        int intervalo = skds.getRegisteredServersCount() / N;
        
        List<HostBlackListThread> threads = new ArrayList<>();
        
        int ocurrencesCount=0;
        
        int checkedListsCount=0;
        
        boolean confiable = true;
        
        multiplo = skds.getRegisteredServersCount() / N == intervalo;
        
        for (int i = 0; i < N; i++) {
            if (!multiplo && i == N-1){
                threads.add(new HostBlackListThread(ipaddress, skds, intervalo*i, skds.getRegisteredServersCount()));
            } else {
                threads.add(new HostBlackListThread(ipaddress, skds, intervalo*i, intervalo*(i + 1)));
            }
        }
        
        for (int i = 0; i < N; i++) {
            threads.get(i).start();
        }
        
        for (int i = 0; i < N; i++) {
            threads.get(i).join();
        }
        
        for (int i = 0; i < N && confiable; i++) {
            checkedListsCount += threads.get(i).getRange();
            ocurrencesCount += threads.get(i).getOcurrencesCount();
            blackListOcurrences.addAll(threads.get(i).getBlackListOcurrences());
            if (ocurrencesCount>=BLACK_LIST_ALARM_COUNT) {
                confiable = false;
                skds.reportAsNotTrustworthy(ipaddress);
            }
        }
        
        if (confiable) {
            skds.reportAsTrustworthy(ipaddress);
        }
        
        LOG.log(Level.INFO, "Checked Black Lists:{0} of {1}", new Object[]{checkedListsCount, skds.getRegisteredServersCount()});
        
        return blackListOcurrences;
    }
    
    
    private static final Logger LOG = Logger.getLogger(HostBlackListsValidator.class.getName());   
}


