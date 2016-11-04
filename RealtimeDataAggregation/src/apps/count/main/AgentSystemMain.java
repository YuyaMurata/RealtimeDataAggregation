/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apps.count.main;

import apps.count.manager.AggregateAgentManager;

/**
 *
 * @author kaeru
 */
public class AgentSystemMain {
    public static void main(String[] args) {
        //Test
        AggregateAgentManager manager = AggregateAgentManager.getInstance();
        manager.setDestinationAgent();
        manager.createAgent();
    }
}
