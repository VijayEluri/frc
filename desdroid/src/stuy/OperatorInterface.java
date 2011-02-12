/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stuy;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStationEnhancedIO;
import edu.wpi.first.wpilibj.DriverStationEnhancedIO.EnhancedIOException;

/**
 * Handles input from the operator interface using the Cypress FirstTouch I/O module.
 * @author Kevin Wang
 */
public class OperatorInterface implements Constants {
    DriverStationEnhancedIO enhancedIO;
    DESdroid des;

    /**
     * Operator interface constructor, setting digital inputs pulled down.
     */
    public OperatorInterface(DESdroid d) {
        enhancedIO = DriverStation.getInstance().getEnhancedIO();  //get driverstation IO instance
        des = d;
        try {
            enhancedIO.setDigitalConfig(BROKEN_LIGHT, DriverStationEnhancedIO.tDigitalConfig.kOutput);

            enhancedIO.setDigitalConfig(LIGHT_BIT_1_CHANNEL, DriverStationEnhancedIO.tDigitalConfig.kOutput);
            enhancedIO.setDigitalConfig(LIGHT_BIT_2_CHANNEL, DriverStationEnhancedIO.tDigitalConfig.kOutput);
            enhancedIO.setDigitalConfig(LIGHT_BIT_3_CHANNEL, DriverStationEnhancedIO.tDigitalConfig.kOutput);
            enhancedIO.setDigitalConfig(LIGHT_BIT_4_CHANNEL, DriverStationEnhancedIO.tDigitalConfig.kOutput);
            enhancedIO.setDigitalConfig(LIGHT_ENABLE_CHANNEL, DriverStationEnhancedIO.tDigitalConfig.kOutput);

            enhancedIO.setDigitalConfig(BIT_1_CHANNEL, DriverStationEnhancedIO.tDigitalConfig.kInputPullDown);
            enhancedIO.setDigitalConfig(BIT_2_CHANNEL, DriverStationEnhancedIO.tDigitalConfig.kInputPullDown);
            enhancedIO.setDigitalConfig(BIT_3_CHANNEL, DriverStationEnhancedIO.tDigitalConfig.kInputPullDown);
            enhancedIO.setDigitalConfig(BIT_4_CHANNEL, DriverStationEnhancedIO.tDigitalConfig.kInputPullDown);

            enhancedIO.setDigitalConfig(OI_MINIBOT_SWITCH_PORT, DriverStationEnhancedIO.tDigitalConfig.kInputPullDown);
        }
        catch (EnhancedIOException e) {
            System.out.println("Error initializing the operator interface.");
        }
    }

    /**
     * Sets the broken light.
     */
    public void setStuffsBrokenLED(boolean val) {
        try {
            enhancedIO.setDigitalOutput(BROKEN_LIGHT, val);
        }
        catch (EnhancedIOException e) {
            System.out.println("Error LED is broken.");
        }
    }
    /**
     * Use a binary switch to set the autonomous mode setting.
     * @return Autonomous setting to run.
     */
    public int getAutonSetting(DESdroid d) {
        des = d;
        try {
            int switchNum = 0;
            int[] binaryValue = new int[4];

            boolean[] dIO = new boolean[]{enhancedIO.getDigital(BIT_1_CHANNEL), enhancedIO.getDigital(BIT_2_CHANNEL), enhancedIO.getDigital(BIT_3_CHANNEL), enhancedIO.getDigital(BIT_4_CHANNEL)};

            for (int i = 0; i < 4; i++) {
                if (dIO[i]) {
                    binaryValue[i] = 1;
                }
                else {
                    binaryValue[i] = 0;
                }
            }

            binaryValue[0] *= 8; // convert all binaryValues to decimal values
            binaryValue[1] *= 4;
            binaryValue[2] *= 2;

            for (int i = 0; i < 4; i++) { // finish binary -> decimal conversion
                switchNum += binaryValue[i];
            }

            if (switchNum > 5) {
                switchNum = 1; // that getAutonSetting() doesn't return a nonexistent switchNum
            }

            return switchNum;
        }
        catch (EnhancedIOException e) {
            setStuffsBrokenLED(true);
            return 5; // Do nothing in case of failure
        }
    }

    public int getAnalogButton() {
        double analogVoltage;
        try {
            analogVoltage = enhancedIO.getAnalogIn(OI_BUTTON_ANALOG_PORT);
        }
        catch (EnhancedIOException e) {
            analogVoltage = 0;
        }
        int buttonNum = (int) ((analogVoltage / 0.4125) + .5);
        return buttonNum;
    }

    public boolean getMinibotSwitch() {
        boolean value;
        try {
            value = enhancedIO.getDigital(OI_MINIBOT_SWITCH_PORT);
        }
        catch (EnhancedIOException e) {
            value = false;
        }
        return value;
    }

    public double getTrimMultiplier(double maxTrim) {
        double trimVoltage;
        try {
            trimVoltage = enhancedIO.getAnalogIn(OI_TRIM_POT_PORT);
        }
        catch (EnhancedIOException e) {
            trimVoltage = 1.65;
        }
        double trimMultiplier = (((trimVoltage - 1.65) * 2) / 3.3) * maxTrim;
        return trimMultiplier;
    }

    public void setLight(int lightNum) {
        String binaryString = DECIMAL_BINARY_TABLE[lightNum];
        boolean[] binaryOutputs = new boolean[4];
        for (int i = 0; i < 4; i++) {
            binaryOutputs[i] = binaryString.toCharArray()[i] == '1';
        }
        try {
            enhancedIO.setDigitalOutput(LIGHT_ENABLE_CHANNEL, true);
            enhancedIO.setDigitalOutput(LIGHT_BIT_1_CHANNEL, binaryOutputs[0]);
            enhancedIO.setDigitalOutput(LIGHT_BIT_2_CHANNEL, binaryOutputs[1]);
            enhancedIO.setDigitalOutput(LIGHT_BIT_3_CHANNEL, binaryOutputs[2]);
            enhancedIO.setDigitalOutput(LIGHT_BIT_4_CHANNEL, binaryOutputs[3]);
        }
        catch (EnhancedIOException e) {

        }
    }

    public void lightsOff() {
        try {
            enhancedIO.setDigitalOutput(LIGHT_ENABLE_CHANNEL, false);
        }
        catch (EnhancedIOException e) {

        }
    }
}
