/*
 * @author Robby McKilliam
 */

package org.mckilliam.distributions.circular;

//import cern.jet.math.Bessel;
import java.util.Random;
import static pubsim.Util.besselI;

/**
 * von Mises distribution of
 * @author Robby McKilliam
 */
public class VonMises extends CircularRandomVariable{

    protected double mu, kappa;
    Random U;
    
    /** 
     * Construct a Von Mises circular random variable with circular (also intrinsic mean)
     * equal to mu and concentration parameter equal to kappa
     */
    public VonMises(double mu, double kappa){
        U = new Random();
        this.mu = mu;
        this.kappa = kappa;
    }
    

    @Override
    public Double mean() {
        return mu;
    }

    /** 
     * The actually gets the von Mises parameter (usually denoted kappa)
     * which is a dispersion parameter similar to variance.
     */
    @Override
    public Double variance() {
        return kappa;
    }

    /**
     * Generates von Mises noise using an algorithm of Best and Fisher.
     * See Mardia, Directional Statistics, p43.
     */
    @Override
    public Double noise() {
        double a = 1 + Math.sqrt(1 + 4*kappa*kappa);
        double b = (a - Math.sqrt(2*a))/(2*kappa);
        double r = (1 + b*b)/(2*b);
        
        while(true){
        
            double z = Math.cos(Math.PI*U.nextDouble());
            double f = (1 + r*z)/(r + z);
            double c = kappa*(r - f);
            
            double U2 = U.nextDouble();
            if(c*(2-c) - U2 > 0 || Math.log(c/U2) + 1 - c > 0)
                return mu + Math.signum(U.nextDouble() - 0.5)*Math.acos(f)/2/Math.PI;
            
        }
           
    }

    @Override
    public void randomSeed() {
        U = new Random();
    }

    @Override
    public void setSeed(long seed) {
        U.setSeed(seed);
    }

    @Override
    public double pdf(Double x) {
        double d = kappa*Math.cos(2*Math.PI*(x - mu));
        return Math.exp(d)/besselI(0,kappa);
    }
    
    /** 
     * Uses series formula for the indefinite integral of the Von Mises pdf. 
     * Formula taken from Wikipedia entry on Von Mises distribution.
     */
    public double indefinteIntegralPDF(double x){
        double sum = 0, bterm = 1, tol = 1e-12;
        int j = 1;
        while( Math.abs(bterm) > tol){
            bterm = besselI(j, kappa) / j;
            sum+= bterm * Math.sin(2*Math.PI*(x-mu)*j);
            j++;
        }
        return x + sum/besselI(0,kappa)/Math.PI;
    }

    @Override
    public Double intrinsicMean() {
        return mu;
    }

    @Override
    public Double circularMean() {
        return mu;
    }

    @Override
    public Double circularVariance() {
        //return 1.0 - Bessel.i1(kappa)/Bessel.i0(kappa);
        return 1.0 - besselI(1,kappa)/besselI(0,kappa);
    }

}
