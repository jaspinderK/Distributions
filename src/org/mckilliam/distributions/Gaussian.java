

/*
 * GaussianNoise.java
 *
 * Created on 13 April 2007, 15:40
 */

package org.mckilliam.distributions;

import pubsim.Complex;
import org.mckilliam.distributions.circular.CircularRandomVariable;
import org.mckilliam.distributions.circular.WrappedGaussian;

/**
 * Creates single Gaussian variables
 * @author Robby McKilliam
 */
public class Gaussian extends AbstractRealRandomVariable implements RealRandomVariable {

    protected final double mean;
    protected final double stdDeviation;
    protected final double variance;
    
    /** Creates a new instance of GaussianNoise with specific variance and mean */
    public Gaussian(double mean, double variance){
        this.mean = mean;
        this.variance = variance;
        this.stdDeviation = Math.sqrt(variance);
    }

    @Override
    public Double mean(){ return mean; }

    @Override
    public Double variance(){ return variance; }
    
    /** Returns an instance of Gaussian noise */
    @Override
    public Double noise(){
        return stdDeviation * random.gaussian() + mean;
    }

    /** Return the Gaussian pdf */
    @Override
    public double pdf(Double x) {
        double s = 1.0/Math.sqrt(2*Math.PI*variance());
        double d = x - mean();
        return s * Math.exp( -(d*d)/(2*variance()) );
    }

    /** Return the cdf of the Gaussian evaluated at x */
    @Override
    public double cdf(Double x){
        //just using the Q function from util.
        return 0.5*(1 + pubsim.Util.erf((x - mean)/stdDeviation/Math.sqrt(2)));
    }
    
    /** Default is the return the wrapped version of this random variable */
    @Override
    public CircularRandomVariable wrapped() {
        return new WrappedGaussian(mean, variance);
    }
    
    @Override
    public Complex characteristicFunction(Double t){
        double m = Math.exp(-variance*t*t/2.0);
        return Complex.polar(m, mean*t);
    }


}
