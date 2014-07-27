/*
 * SignalGenerator.java
 *
 * Created on 13 April 2007, 14:48
 */

package org.mckilliam.distributions.processes;

import java.io.Serializable;
import org.mckilliam.distributions.NoiseGenerator;

/**
 * Interface for the generation received signals
 * @author Robby McKillam
 */
public interface SignalGenerator<T> extends Serializable {

        public T[] generateReceivedSignal();

        public void setNoiseGenerator(NoiseGenerator<T> noise);
        public NoiseGenerator<T> getNoiseGenerator();

        /** Return the length of the signal generated */
        public int getLength();

}
