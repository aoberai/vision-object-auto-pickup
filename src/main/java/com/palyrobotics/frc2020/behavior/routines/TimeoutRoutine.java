package com.palyrobotics.frc2020.behavior.routines;

import com.palyrobotics.frc2020.behavior.Routine;
import com.palyrobotics.frc2020.config.Commands;
import com.palyrobotics.frc2020.subsystems.Subsystem;
import edu.wpi.first.wpilibj.Timer;

public class TimeoutRoutine extends Routine {

    private double mTimeout;
    private final Timer mTimer = new Timer();

    /**
     * Routine that waits the specified amount of time <br />
     * Does not require any subsystems
     *
     * @param waitTime Time to wait in seconds
     */
    public TimeoutRoutine(double waitTime) {
        mTimeout = waitTime;
    }

    @Override
    public void start() {
        mTimer.reset();
        mTimer.start();
    }

    @Override
    public Commands update(Commands commands) {
        return commands;
    }

    @Override
    public Commands cancel(Commands commands) {
        return commands;
    }

    @Override
    public boolean isFinished() {
        return mTimer.get() > mTimeout;
    }

    @Override
    public Subsystem[] getRequiredSubsystems() {
        return new Subsystem[]{};
    }

    @Override
    public String getName() {
        return "Timeout Routine";
    }

}
