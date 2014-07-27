package org.mckilliam.distributions.processes;

import pubsim.CircularBufferDouble;
import pubsim.VectorFunctions;
import org.mckilliam.distributions.Gaussian;
import org.mckilliam.distributions.RealRandomVariable;

/**
 * Returns a zero mean stationary correlated Gaussian noise process.  The 
 * process is correlated according to a finite impulse response filter.
 * @author Robby McKilliam
 */
public class ColouredGaussianNoise implements StationaryProcess {

    protected final double[] f;
    protected final double[] autocor;
    protected final Gaussian noisegen = new Gaussian(0, 1);
    protected final CircularBufferDouble X;
   
    public ColouredGaussianNoise(double[] filter){
        f = filter;
        
        //conmpute the autocorrelation associated with this filter.
        double[] convf = VectorFunctions.conv(VectorFunctions.reverse(f), f);
        autocor = new double[f.length];
        for(int i = 0; i < f.length; i++) autocor[i] = convf[i + f.length - 1];

        X = new CircularBufferDouble(f.length);
        //fill up the buffer to remove `start up' effect.
        for(int i = 0; i < f.length; i++) X.add(noisegen.noise());
    }

    @Override
    public RealRandomVariable marginal() {
        return new Gaussian(0, autocor[0]);
    }
    
    /** Return the value of the bivariate pdf between X1 and Xk */
    public double bivariatePdf(int k, double x1, double xk){
        double v = autocor[0];
        double cor = autocor[k]/v;
        //compute correlation here
        
        double z = (x1*x1 - 2*cor*x1*xk + xk*xk)/v/2/(1 - cor*cor); 
        return 1/(2*Math.PI*Math.sqrt(1 - cor*cor)*v)*Math.exp(-z);
    }

    @Override
    public double[] autocorrelation() { return autocor; }
    
    private int n = 0;
    @Override
    public Double noise(){
        X.add(noisegen.noise());
        double Y = 0;
        for(int k = 0; k < f.length; k++)
            Y += f[k]*X.get(n-k);
        n++;
        return Y;
    }

    @Override
    public void randomSeed() {
        noisegen.randomSeed();
    }

    @Override
    public void setSeed(long seed) {
        noisegen.setSeed(seed);
    }
    
    
}
