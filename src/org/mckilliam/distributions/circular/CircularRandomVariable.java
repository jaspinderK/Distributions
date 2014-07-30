/*
 * @author Robby McKilliam
 */

package org.mckilliam.distributions.circular;

import pubsim.Integration;
import pubsim.Complex;
import org.mckilliam.distributions.RealRandomVariable;
import org.mckilliam.distributions.SeedGenerator;
import rngpack.RandomElement;
import rngpack.Ranmar;

/**
 * Abstract class for circular random variables.  Automates computation
 * of unwrapped and circular means.
 * @author Robby McKilliam
 */
public abstract class CircularRandomVariable implements RealRandomVariable {

    protected InstrinsicMeanAndVariance unwrped;
    protected CircularMeanVariance circ;

    protected RandomElement random;

    public CircularRandomVariable(){
        random = new Ranmar(SeedGenerator.getSeed());
    }
    
    /**
     * Return the intrinsic variance.
     */
    public Double intrinsicVariance(){
        if(unwrped == null) unwrped = new InstrinsicMeanAndVariance(this);
        return unwrped.getIntrinsicVariance();
    }

    /**
     * Return the intrinsic (or wrapped) variance assuming that the mean is 
     * intrinsic mean. This is much faster and more accurate if you know the mean in advance.
     */
    public Double intrinsicVariance(double truemean){
        if(unwrped == null || unwrped.getIntrinsicMean() != truemean)
            unwrped = new InstrinsicMeanAndVariance(this,truemean);
        return unwrped.getIntrinsicVariance();
    }

    /**
     * Return the intrinsic (or wrapped) mean
     */
    public Double intrinsicMean(){
        if(unwrped == null) unwrped = new InstrinsicMeanAndVariance(this);
        return unwrped.getIntrinsicMean();
    }

    /**
     * Return the circular mean
     */
    public Double circularMean(){
        if(circ == null) circ = new CircularMeanVariance(this);
        return circ.circularMean();
    }

    /**
     * Return the circular variance
     */
    public Double circularVariance(){
        if(circ == null) circ = new CircularMeanVariance(this);
        return circ.circularVariance();
    }

    /** Randomise the seed for the internal Random */
    @Override
    public void randomSeed(){ random = new Ranmar(new java.util.Date()); }


    /** Set the seed for the internal Random */
    @Override
    public void setSeed(long seed) { random = new Ranmar(seed); }

    /**
     * Binary search of the cdf to find the inverse cdf (i.e. bisection method).
     * This uses the fact that circular random variable are in
     * [0.5, 0.5].
     */
    @Override
    public Double icdf(double x){
        double TOL = 1e-8;
        int maxiters = 50, iters = 0;
        double high = 0.5;
        double low = -0.5;
        double cdfhigh = cdf(high);
        double cdflow = cdf(low);
        while(Math.abs(high - low) > TOL && iters < maxiters){

            double half = (high + low)/2.0;
            double cdfhalf = cdf(half);

            //System.out.println("half = " + half + ", cdfhalf = " + cdfhalf);

            if(Math.abs(cdfhalf - x) < TOL ) return half;
            else if(cdfhalf <= x){
                low = half;
                cdflow = cdfhalf;
            }
            else{
               high = half;
               cdfhigh = cdfhalf;
            }
            
            iters++;

        }
        return (high + low)/2.0;
    }

    /**
     * Take standard inverse cumulative density function approach
     * by default.
     */
    @Override
    public Double noise(){
        return icdf(random.raw());
    };

    /**
     * integrate the pdf by default
     */
    @Override
    public double cdf(Double x){
        double startint = -0.5;
        final int INTEGRAL_STEPS = 1000;
        double cdfval = (new Integration() {
                public double f(double x) {
                    return pdf(x);
                }
            }).trapezoid(startint,x,INTEGRAL_STEPS);
        return cdfval;
    }

    /**
     * This returns the `mean' i.e. the expected value of the circular
     * random variable.  This does not necessarily correspond to the
     * `mean direction.  See my thesis or papers.
     */
    @Override
    public Double mean(){
        final int INTEGRAL_STEPS = 1000;
        double tmean = (new Integration() {
                public double f(double x) {
                    return x*pdf(x);
                }
            }).trapezoid(-0.5,0.5,INTEGRAL_STEPS);
        return tmean;
    }

    /**
     * This returns the `variance' i.e. the expected value of the circular
     * random variable squared subtract it's mean.  This does not necessarily correspond to the
     * circular variance or the unwrapped variance.  See my thesis or papers.
     */
    @Override
    public Double variance(){
        final int INTEGRAL_STEPS = 1000;
        double tvar = (new Integration() {
                public double f(double x) {
                    return x*x*pdf(x);
                }
            }).trapezoid(-0.5, 0.5, INTEGRAL_STEPS);
            double tmean = mean();
        return tvar - tmean*tmean;
    }

    /** Default is the return the wrapped version of this random variable */
    @Override
    public CircularRandomVariable wrapped() { return this; }
    
     /** 
     * Numerical integration to compute characteristic function.
     * Apart from very strange circular distributions, this should be reasonably accurate,
     */
    @Override
    public Complex characteristicFunction(final Double t){
        int integralsteps = 5000;
        double rvar = (new Integration() {
            public double f(double x) {
                return Math.cos(t*x)*pdf(x);
            }
        }).trapezoid(-0.5, 0.5, integralsteps);
        double cvar = (new Integration() {
            public double f(double x) {
                return Math.sin(t*x)*pdf(x);
            }
        }).trapezoid(-0.5, 0.5,integralsteps);
           
        return new Complex(rvar, cvar);
    }
     
}
