package com.palyrobotics.frc2020.util;

import com.palyrobotics.frc2020.config.Commands;
import com.palyrobotics.frc2020.config.RobotState;
import com.palyrobotics.frc2020.config.VisionConfig;
import com.palyrobotics.frc2020.config.dashboard.LiveGraph;
import com.palyrobotics.frc2020.util.config.Configs;
import com.palyrobotics.frc2020.util.control.SynchronousPID;
import edu.wpi.first.wpilibj.controller.PIDController;

/**
 * Auto Aligns to Power Cells
 */
public class KumquatDriveHelper {
    private final SparkDriveSignal mSignal = new SparkDriveSignal();
    private final CheesyDriveHelper mCDH = new CheesyDriveHelper();
    private final VisionConfig mConfig = Configs.get(VisionConfig.class);

    private boolean mAligned = true;
    private double mDxToPowerCell = 0.0;
    private double mDetectedObjectArea = 0.0;
    private double mLeftOutput, mRightOutput, mPreviousLeftOutput, mPreviousRightOutput;

    public SparkDriveSignal visionDrive(Commands commands, RobotState robotState) {
        mDetectedObjectArea = RobotState.getInstance().detectedObjectArea;
        mDxToPowerCell = RobotState.getInstance().dxFromPowerCell;
        if(mDetectedObjectArea > mConfig.minPowerCellArea && mDetectedObjectArea < mConfig.maxPowerCellArea &&  RobotState.getInstance().feedAvailable) {
            mAligned = false;
            double angularPLoop = Math.abs(mConfig.kumAngleAlignGains.f + (mDxToPowerCell * mConfig.kumAngleAlignGains.p));
            double forwardPLoop = mConfig.kumDistanceApproachGains.p / mDetectedObjectArea + mConfig.kumDistanceApproachGains.f;

            mLeftOutput = forwardPLoop;
            mRightOutput = forwardPLoop;

//            mLeftOutput = angularPLoop;
//            mRightOutput = angularPLoop;
             if (mDxToPowerCell < -15) {
                mLeftOutput += angularPLoop;
            } else if (mDxToPowerCell > 15) {
                mRightOutput += angularPLoop;
            }

            if (mLeftOutput > 0.6) {
                mLeftOutput = 0.6;
            } else if (mRightOutput > 0.6) {
                mRightOutput = 0.6;
            } else if (mLeftOutput < -0.6) {
                mLeftOutput = -0.6;
            } else if (mRightOutput < -0.6) {
                mRightOutput = -0.6;
            }
        }
        else {
            mLeftOutput = 0;
            mRightOutput = 0;
            mPreviousLeftOutput = 0;
            mPreviousRightOutput = 0;
        }
        if (mAligned) {
            return mCDH.cheesyDrive(commands, robotState);
        } else {
            System.out.println("left output: " + mLeftOutput + " right output " + mRightOutput );
            mSignal.leftOutput.setPercentOutput((mLeftOutput + mPreviousLeftOutput)/2);
            mSignal.rightOutput.setPercentOutput((mRightOutput + mPreviousRightOutput)/2);
            LiveGraph.getInstance().add("left output", (mLeftOutput + mPreviousLeftOutput)/2);
            LiveGraph.getInstance().add("right output", (mRightOutput + mPreviousRightOutput)/2);
            mPreviousLeftOutput = mLeftOutput;
            mPreviousRightOutput = mRightOutput;
        }

        return mSignal;
    }
}
