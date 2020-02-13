package com.palyrobotics.frc2020.config;

import com.palyrobotics.frc2020.util.config.AbstractConfig;
import com.palyrobotics.frc2020.util.control.Gains;

public class VisionConfig extends AbstractConfig {
    public Gains gains;
    public Gains kumAngleAlignGains;
    public Gains kumDistanceApproachGains;
    public double minPowerCellArea;
    public double maxPowerCellArea;
}
