/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mckilliam.distributions.discrete;

import static org.junit.Assert.assertEquals;
import org.junit.*;

/**
 *
 * @author Robby McKilliam
 */
public class PoissonRandomVariableTest {

    public PoissonRandomVariableTest() {
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
     * Test of getNoise method, of class GeometricRandomVariable.
     */
    @Test
    public void testGetNoise() {
        //System.out.println("getNoise");
        double p = 10;
        int n = 1000000;
        Poisson instance = new Poisson(p);
        double var = 0;
        double mean = 0;
        for(int i = 0; i < n; i++){
            double s  = instance.noise();
            //System.out.println(instance.getNoise());
            mean += s;
            double d = s - p;
            var += d*d;
        }
        mean /= n;
        var /= n;
        assertEquals(p, var, 0.01);
        assertEquals(p, mean, 0.01);
    }

}