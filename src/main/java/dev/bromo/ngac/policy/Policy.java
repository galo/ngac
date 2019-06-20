package dev.bromo.ngac.policy;

import gov.nist.csd.pm.decider.Decider;
import gov.nist.csd.pm.decider.PReviewDecider;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.graph.Graph;
import gov.nist.csd.pm.graph.MemGraph;
import gov.nist.csd.pm.graph.model.nodes.Node;
import gov.nist.csd.pm.graph.model.nodes.NodeType;
import gov.nist.csd.pm.prohibitions.model.Prohibition;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Policy {

    protected Policy() {
    }


    public static class Builder {
        public static Policy build() throws PMException {

            Random r = new Random();

            Graph graph = new MemGraph();

            // Create users
            Node user1 = graph.createNode( r.nextLong(), "Shell", NodeType.U, null);
            Node user2 = graph.createNode(r.nextLong(), "Galo", NodeType.U, null);

            // Create users attributes
            Node admin = graph.createNode( r.nextLong(), "admin", NodeType.UA, null);


            // Make shell and admin
            graph.assign(user1.getID(), admin.getID());


            // Create objects
            Node printer1 = graph.createNode(r.nextLong(), "Printer1", NodeType.O, null);
            Node printer2 = graph.createNode(r.nextLong(), "Printer2", NodeType.O, null);
            Node printer3 = graph.createNode(r.nextLong(), "Printer2", NodeType.O, null);

            // Create the Policy Class
            Node printerPolicy = graph.createNode( r.nextLong(), "Printer Access Policy", NodeType.PC, null);

            // Create the customer attribute
            Node customerA = graph.createNode( r.nextLong(), "Customers", NodeType.OA, null);

            // Create Kmpg and Carterpillar
            Node kpmgA = graph.createNode( r.nextLong(), "kpmg", NodeType.OA, null);
            graph.assign(kpmgA.getID(), customerA.getID());

            Node carterpillarA = graph.createNode( r.nextLong(), "carterpillar", NodeType.OA, null);
            graph.assign(carterpillarA.getID(), customerA.getID());

            // Assing Printers to Customers
            graph.assign(printer1.getID(), kpmgA.getID());
            graph.assign(printer2.getID(), carterpillarA.getID());
            graph.assign(printer3.getID(), carterpillarA.getID());


            // Create regions
            Node regionA = graph.createNode( r.nextLong(), "Regions", NodeType.OA, null);

            Node amsA = graph.createNode( r.nextLong(), "ams", NodeType.OA, null);
            graph.assign(amsA.getID(), regionA.getID());
            Node apjA = graph.createNode( r.nextLong(), "apj", NodeType.OA, null);
            graph.assign(apjA.getID(), regionA.getID());

            // Assign Printers to Regions
            graph.assign(printer1.getID(), amsA.getID());
            graph.assign(printer2.getID(), amsA.getID());
            graph.assign(printer3.getID(), apjA.getID());

            // 7. Assign the `Customer` and `Regions` objects attribute to the `Policy` policy class node.
            graph.assign(customerA.getID(), printerPolicy.getID());
            graph.assign(regionA.getID(), printerPolicy.getID());

            //This will give shell read and write on `carterpillar` and `kpmg`
            graph.associate(admin.getID(), kpmgA.getID(), new HashSet<>(Arrays.asList("r", "w")));
            graph.associate(admin.getID(), carterpillarA.getID(), new HashSet<>(Arrays.asList("r", "w")));

            // Create a Prohibition
            //Prohibition prohibition = new Prohibition();
            //prohibition.addNode(user1.getID(), apjA.getID());

            Decider decider = new PReviewDecider(graph);
            Set<String> permissions = decider.listPermissions(user1.getID(),printer1.getID());
            assertTrue(permissions.contains("r"));
            assertTrue(permissions.contains("w"));

            return new Policy();
        }
    }

}
