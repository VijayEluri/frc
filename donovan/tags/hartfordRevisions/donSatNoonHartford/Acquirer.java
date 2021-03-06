package edu.stuy;

import edu.wpi.first.wpilibj.*;

/**
 * This acquires the ball so that the robot may kick it.
 * It will keep the ball near the robot and prevent the ball from drifting.
 * @author Prog
 */
public class Acquirer extends Victor {

    Donovan donnie;

    public Acquirer(int channel, Donovan d) {
        super(channel);
        donnie = d;
    }

    /**
     * This will run the acquirer so it can keep the ball
     */
    public void start() {
        set(-1.0); //double pwm value between -1 and 1
    }

    public void startReverse() {
        set(1.0);
    }

    /**
     * This stops the acquirer
     */
    public void stop() {
        set(0);
    }
}
