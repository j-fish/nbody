package com.jellyfish.jfgnbody.nbody.simulations;

import com.jellyfish.jfgnbody.nbody.NBody;
import com.jellyfish.jfgnbody.nbody.constants.NBodyConst;
import com.jellyfish.jfgnbody.nbody.entities.MassiveBody;

/**
 *
 * @author thw
 */
public class Simulation3 extends AbstractSimulation {

    @Override
    public void start(final int N, final NBody nBody) {
        new Simulation1().start(N, nBody);
        nBody.bodyMap.put(nBody.bodyMap.size() + 1, 
            new MassiveBody(nBody.bodyMap.size() + 1,
                NBodyConst.NBODY_MASS_CONST * (1.4), 1, 
                -28617.639985581613, 
                -1787.297295869821, 
                1e6 * (NBodyConst.SOLARMASS / 2), 
                NBodyConst.M_BODY_COLOR));
    }
    
    @Override
    public String toString() {
        return "N bodies + 1 colliding massive body (solormass/2)";
    }
    
}