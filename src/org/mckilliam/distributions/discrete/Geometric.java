package org.mckilliam.distributions.discrete;

import rngpack.RandomSeedable;
import rngpack.Ranlux;

/**
 * Implementation of the geometric distribution. This is the variant that can
 * not return 0 i.e. the pdf is P(k) = (1-p)^(k-1)p
 *
 * @author Robby McKilliam
 */
public class Geometric extends AbstractDiscreteRandomVariable {

    public final double p;

    /**
     * Constructor sets the parameter for this geometric distribution. p must be
     * between 0 and 1.  Mean is 1/p
     */
    public Geometric(double p) {
        if (p <= 0 || p > 1) {
            throw new RuntimeException("p must be between 0 and 1.");
        }
        this.p = p;
    }

    @Override
    public Integer noise() {
        Integer v = 1;
        while (random.raw() > p) {
            v++;
        }
        return v;
    }

    @Override
    public double pmf(Integer k) {
        return p * Math.pow(1 - p, k - 1);
    }

    @Override
    public Integer icmf(double x) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public double mean() {
        return 1.0 / p;
    }

    @Override
    public double variance() {
        return (1 - p) / (p * p);
    }

    /**
     * Randomise the seed for the internal Random
     */
    @Override
    public void randomSeed() {
        random = new Ranlux(RandomSeedable.ClockSeed());
    }

    /**
     * Set the seed for the internal Random
     */
    @Override
    public void setSeed(long seed) {
        random = new Ranlux(seed);
    }

    @Override
    public double cmf(Integer k) {
        return 1.0 - Math.pow(1 - p, k);
    }
    
    
    /** A geometric random variable starting at zero */
    public static class StartingAtZero extends Geometric {

        public StartingAtZero(double p) {
            super(p);
        }

        @Override
        public Integer noise() {
            Integer v = 0;
            while(random.raw() > p) {
                v++;
            }
            return v;
        }

        @Override
        public double pmf(Integer k) {
            return p * Math.pow(1 - p, k);
        }

        @Override
        public double mean() {
            return (1.0 - p) / p;
        }

        @Override
        public double variance() {
            return (1 - p) / (p * p);
        }

        @Override
        public double cmf(Integer k) {
            return 1.0 - Math.pow(1 - p, k+1);
        }
    
    }
    
}
