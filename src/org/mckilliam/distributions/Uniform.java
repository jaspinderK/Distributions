/*
 * UniformNoise.java
 *
 * Created on 7 May 2007, 12:40
 */

package org.mckilliam.distributions;

import org.mckilliam.distributions.circular.CircularRandomVariable;
import org.mckilliam.distributions.circular.WrappedUniform;

/**
 *
 * @author Robby McKilliam
 */
public class Uniform extends AbstractRealRandomVariable implements RealRandomVariable {
    protected final double range;
    protected final double mean;
    protected final double stdDeviation;
    protected final double variance;

    /** Creates a new instance of GaussianNoise with specific variance and mean */
    public Uniform(double mean, double variance){
        if(variance < 0.0) throw new RuntimeException("Argument variance cannot be negative when constructing UniformNoise.");
        this.mean = mean;
        this.variance = variance;
        this.stdDeviation = Math.sqrt(variance);
        range = 2.0 * Math.sqrt( 3.0 * variance );
    }

    public static Uniform constructFromMinMax(double min, double max) {
        if(min >= max) throw new RuntimeException("Argument max must be larger than min when constructing UniformNoise in range [min,max].");
        double mean = (max + min)/2.0;
        double range = max - min;
        return constructFromMeanAndRange(mean, range);
    }
    
    public static Uniform constructFromMeanAndRange(double mean, double range) {
        if(range < 0.0) throw new RuntimeException("Range cannot be negative when constructing UniformNoise.");
        double variance = Math.pow(range/2.0 , 2)/3.0;
        return new Uniform(mean, variance);
    }
    
    @Override
    public Double mean(){ return mean; }

    @Override
    public Double variance(){ return variance; }

    public Double getRange() { return range; }
    
    /** Returns a uniformly distributed value */
    @Override
    public Double noise(){
        return mean + range * (random.raw() - 0.5);
    }

    @Override
    public double pdf(Double x){
        double h = 1.0/range;
        double min = mean - 0.5*range;
        double max = mean + 0.5*range;
        if( x < min || x > max ) return 0.0;
        return h;
    }
    
    @Override
    public double cdf(Double x){
        double h = 1.0/range;
        double min = mean - 0.5*range;
        double max = mean + 0.5*range;
        if(x < min) return 0.0;
        if(x > max) return 1.0;
        else return h*(x - min);
    }
    
    @Override
    public CircularRandomVariable wrapped() {
        return new WrappedUniform(mean, variance);
    }
    
}
