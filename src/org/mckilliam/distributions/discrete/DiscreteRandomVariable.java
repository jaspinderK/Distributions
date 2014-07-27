package org.mckilliam.distributions.discrete;

import org.mckilliam.distributions.NoiseGenerator;

/**
 *
 * @author Robby McKilliam
 */
public interface DiscreteRandomVariable extends NoiseGenerator<Integer> {
    
    public double mean();
    public double variance();

    /** Return the probability mass function evaluated at x */
    public double pmf(Integer k);

    /** Return the cumulative mass function evaluated at x */
    public double cmf(Integer k);

    /**
     * Return the inverse cumulative mass function.
     * This allows getNoise to work in a standard way by generating
     * uniform noise in [0,1] and applying icdf.
     * However, you don't have to implement this if you have a better
     * way of generating the noise.
     */
    public Integer icmf(double x);
    
}
