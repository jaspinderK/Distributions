package org.mckilliam.distributions.circular;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Robby McKilliam
 */
public class CircularMeanVarianceTest {

    public CircularMeanVarianceTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of circularVariance method, of class CircularMeanVariance.
     */
    @Test
    public void testCircularVariance() {
        System.out.println("circularVariance");

        VonMises dist = new VonMises(0.25, 10);

        CircularMeanVariance instance = new CircularMeanVariance(dist);


        double cvar = instance.circularVariance();
        assertEquals(dist.circularVariance(), cvar, 0.00001);
    }

        /**
     * Test of circularVariance method, of class CircularMeanVariance.
     */
    @Test
    public void testCircularMean() {
        System.out.println("circularMean");

        VonMises dist = new VonMises(0.25, 10);

        CircularMeanVariance instance = new CircularMeanVariance(dist);

        double cmean = instance.circularMean();
        assertEquals(dist.circularMean(), cmean, 0.00001);

    }

}