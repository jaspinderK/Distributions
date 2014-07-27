/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mckilliam.distributions;

import java.io.Serializable;

/**
 *
 * @author Robby McKilliam
 */
public interface NoiseGenerator<T> extends Serializable {
    
    /**
     * Returns a random variable from the noise
     * distribution.
     */
    public T noise();
    
    /** Randomise the seed for the internal Random */ 
    public void randomSeed();
    
    /** Set the seed for the internal Random */
    public void setSeed(long seed);
    
}
