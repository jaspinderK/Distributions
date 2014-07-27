/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mckilliam.distributions.processes;

import Jama.Matrix;
import flanagan.integration.IntegralFunction;
import flanagan.integration.Integration;
import org.mckilliam.distributions.RealRandomVariable;
import pubsim.optimisation.AutoIntegralFunction;

/**
 * This implements a stationary process with arbitrary pdf using a Gaussian process.  You can
 * set the correlation of the underlying Gaussian process but this doesn't let you control 
 * the correlation of the output process that is not Gaussian.
 * @author Robby McKilliam
 */
public class InverseCDFStationaryProcess implements StationaryProcess {
    
    protected final ColouredGaussianNoise X;
    protected final RealRandomVariable g, y;
    protected double[] ac;
    
    protected InverseCDFStationaryProcess(){ X = null; g = null; y = null;}
    
    public InverseCDFStationaryProcess(RealRandomVariable rv, double[] filter){
       X = new ColouredGaussianNoise(filter);
       g = X.marginal();
       y = rv;       
    }

    @Override
    public RealRandomVariable marginal() {
        return y;
    }

    @Override
    public double[] autocorrelation() {
        if(ac != null) return ac;
        
        ac = new double[X.autocorrelation().length];
        double Xvar = X.autocorrelation()[0];
        final double ir = 10*Math.sqrt(Xvar); //range to compute integral over        
        
        //compute the variance term ie. ac[0]
        ac[0] = (new Integration(new IntegralFunction() {
                        public double function(double x) {
                            double FGx = y.icdf(g.cdf(x)) ;
                            return FGx*FGx* X.marginal().pdf(x);
                        }
                    }, -ir, ir)).gaussQuad(150);
 
        //compute all the convariance terms
        double[] min = {-ir,-ir}; double[] max = {ir,ir};
        for(int k = 1; k < ac.length; k++){
            final int kf = k;
            ac[k] = new AutoIntegralFunction(150) {
                public double value(Matrix mat) {
                    double x1 = mat.get(0,0); 
                    double xk = mat.get(1,0);
                    return y.icdf(g.cdf(x1)) * y.icdf(g.cdf(xk)) * X.bivariatePdf(kf, x1, xk);
                }
            }.integral(min, max);
        }
        return ac;
    }
    
    public double[] autocorrelation(int intsteps) {
        if(ac == null) ac = new double[X.autocorrelation().length];
        double Xvar = X.autocorrelation()[0];
        final double ir = 10*Math.sqrt(Xvar); //range to compute integral over
        
        
        //compute the variance term ie. ac[0]
        ac[0] = (new Integration(new IntegralFunction() {
                        public double function(double x) {
                            double FGx = y.icdf(g.cdf(x)) ;
                            return FGx*FGx* X.marginal().pdf(x);
                        }
                    }, -ir, ir)).gaussQuad(intsteps);
 
        //compute all teh convariance terms
        double[] min = {-ir,-ir}; double[] max = {ir,ir};
        for(int k = 1; k < ac.length; k++){
            final int kf = k;
            ac[k] = new AutoIntegralFunction(intsteps) {
                public double value(Matrix mat) {
                    double x1 = mat.get(0,0); 
                    double xk = mat.get(1,0);
                    return y.icdf(g.cdf(x1)) * y.icdf(g.cdf(xk)) * X.bivariatePdf(kf, x1, xk);
                }
            }.integral(min, max);
        }
        return ac;
    }
    
    @Override
    public Double noise() {
        return y.icdf(g.cdf(X.noise()));
    }

    @Override
    public void randomSeed() {
        X.randomSeed();
    }

    @Override
    public void setSeed(long seed) {
        X.setSeed(seed);
    }
    
}
