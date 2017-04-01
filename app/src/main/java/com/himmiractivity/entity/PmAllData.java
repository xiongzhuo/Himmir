package com.himmiractivity.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/1.
 */

public class PmAllData implements Serializable {

    private String oNstate;//开关机
    private String mode;//模式
    private String exhaustMode;//新排风模式
    private int exhaustRatio;//排风比例
    private String lcdBacklight;//背光时间s
    private int auxHeating;//辅热温度值
    private String constTempMode;//恒温模式
    private String constHumiMode;//恒湿模式
    private int constTempPara;//恒温参数
    private int constHumiPara;//恒湿参数
    private int fanFreq;//风机频率
    private int blowerSpeed;//送风机转速
    private int exhaustSpeed;//排风机转速
    private int blowingRate;//风量
    private int thawStartTemp;//融冰启动温度
    private int thawStopTemp;//融冰停止温度
    private int thawTime;//融冰时间分钟
    private int thawInv;//融冰间隔时间分钟
    private int dustwiperClearPeriod;//除尘器清洗周期小时
    private int dustwiperClearRemain;//除尘器清洗周期剩余时间小时
    private String timeMode;//定时模式
    private String timerOneState;//定时器一
    private String timerTwoState;//定时器二
    private String timerThreeState;//定时器三
    private int timerOneStartHour;//定时器1开始时间小时值
    private int timerOneStartMin;//定时器1开始时间分钟值
    private int timerOneEndHour;//定时器1结束时间小时值
    private int timerOneEndMin;//定时器1结束时间分钟值
    private int timerTwoStartHour;//定时器2开始时间小时值
    private int timerTwoStartMin;//定时器2开始时间小时值
    private int timerTwoEndHour;//定时器2开始时间小时值
    private int timerTwoEndMin;//定时器2开始时间小时值
    private int timerThreeStartHour;//定时器3开始时间小时值
    private int timerThreeStartMin;//定时器3开始时间小时值
    private int timerThreeEndHour;//定时器3开始时间小时值
    private int timerThreeEndMin;//定时器3开始时间小时值
    private int co2Adj;//CO2调节值
    private String funcStatus;//功能状态
    private int pmAdj;//PM调节值
    private int sensorIndoorTemp;//室内温度
    private int sensorOutdoorTemp;//室外温度
    private int sensorFanTemp;//新风温度
    private int sensorExhaustTemp;//排风温度
    private int co2Thickness;//CO2浓度
    private int indoorPmThickness;//室内颗粒物浓度
    private int outdoorPmThickness;//室外颗粒物浓度
    private int systemClockYear;//系统时间年校准
    private int systemClockMouth;//系统时间月校准
    private int systemClockDay;//系统时间日校准
    private int systemClockHour;//系统时间时校准
    private int systemClockMin;//系统时间分校准
    private String faultStatus;//故障状态

    public String getoNstate() {
        return oNstate;
    }

    public String getMode() {
        return mode;
    }

    public String getExhaustMode() {
        return exhaustMode;
    }

    public int getExhaustRatio() {
        return exhaustRatio;
    }

    public String getLcdBacklight() {
        return lcdBacklight;
    }

    public int getAuxHeating() {
        return auxHeating;
    }

    public String getConstTempMode() {
        return constTempMode;
    }

    public String getConstHumiMode() {
        return constHumiMode;
    }

    public int getConstTempPara() {
        return constTempPara;
    }

    public int getConstHumiPara() {
        return constHumiPara;
    }

    public int getFanFreq() {
        return fanFreq;
    }

    public int getBlowerSpeed() {
        return blowerSpeed;
    }

    public int getExhaustSpeed() {
        return exhaustSpeed;
    }

    public int getBlowingRate() {
        return blowingRate;
    }

    public int getThawStartTemp() {
        return thawStartTemp;
    }

    public int getThawStopTemp() {
        return thawStopTemp;
    }

    public int getThawTime() {
        return thawTime;
    }

    public int getThawInv() {
        return thawInv;
    }


    public int getDustwiperClearPeriod() {
        return dustwiperClearPeriod;
    }

    public int getDustwiperClearRemain() {
        return dustwiperClearRemain;
    }

    public String getTimeMode() {
        return timeMode;
    }

    public String getTimerOneState() {
        return timerOneState;
    }

    public String getTimerTwoState() {
        return timerTwoState;
    }

    public String getTimerThreeState() {
        return timerThreeState;
    }

    public int getTimerOneStartHour() {
        return timerOneStartHour;
    }

    public int getTimerOneStartMin() {
        return timerOneStartMin;
    }

    public int getTimerOneEndHour() {
        return timerOneEndHour;
    }

    public int getTimerOneEndMin() {
        return timerOneEndMin;
    }

    public int getTimerTwoStartHour() {
        return timerTwoStartHour;
    }

    public int getTimerTwoStartMin() {
        return timerTwoStartMin;
    }

    public int getTimerTwoEndHour() {
        return timerTwoEndHour;
    }

    public int getTimerTwoEndMin() {
        return timerTwoEndMin;
    }

    public int getTimerThreeStartHour() {
        return timerThreeStartHour;
    }

    public int getTimerThreeStartMin() {
        return timerThreeStartMin;
    }

    public int getTimerThreeEndHour() {
        return timerThreeEndHour;
    }

    public int getTimerThreeEndMin() {
        return timerThreeEndMin;
    }

    public int getCo2Adj() {
        return co2Adj;
    }

    public String getFuncStatus() {
        return funcStatus;
    }

    public int getPmAdj() {
        return pmAdj;
    }

    public int getSensorIndoorTemp() {
        return sensorIndoorTemp;
    }

    public int getSensorOutdoorTemp() {
        return sensorOutdoorTemp;
    }

    public int getSensorFanTemp() {
        return sensorFanTemp;
    }

    public int getSensorExhaustTemp() {
        return sensorExhaustTemp;
    }

    public int getCo2Thickness() {
        return co2Thickness;
    }

    public int getIndoorPmThickness() {
        return indoorPmThickness;
    }

    public int getOutdoorPmThickness() {
        return outdoorPmThickness;
    }

    public int getSystemClockYear() {
        return systemClockYear;
    }

    public int getSystemClockMouth() {
        return systemClockMouth;
    }

    public int getSystemClockDay() {
        return systemClockDay;
    }

    public int getSystemClockHour() {
        return systemClockHour;
    }

    public int getSystemClockMin() {
        return systemClockMin;
    }

    public String getFaultStatus() {
        return faultStatus;
    }

    public void setoNstate(String oNstate) {
        this.oNstate = oNstate;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setExhaustMode(String exhaustMode) {
        this.exhaustMode = exhaustMode;
    }

    public void setExhaustRatio(int exhaustRatio) {
        this.exhaustRatio = exhaustRatio;
    }

    public void setLcdBacklight(String lcdBacklight) {
        this.lcdBacklight = lcdBacklight;
    }

    public void setAuxHeating(int auxHeating) {
        this.auxHeating = auxHeating;
    }

    public void setConstTempMode(String constTempMode) {
        this.constTempMode = constTempMode;
    }

    public void setConstHumiMode(String constHumiMode) {
        this.constHumiMode = constHumiMode;
    }

    public void setConstTempPara(int constTempPara) {
        this.constTempPara = constTempPara;
    }

    public void setConstHumiPara(int constHumiPara) {
        this.constHumiPara = constHumiPara;
    }

    public void setFanFreq(int fanFreq) {
        this.fanFreq = fanFreq;
    }

    public void setBlowerSpeed(int blowerSpeed) {
        this.blowerSpeed = blowerSpeed;
    }

    public void setExhaustSpeed(int exhaustSpeed) {
        this.exhaustSpeed = exhaustSpeed;
    }

    public void setBlowingRate(int blowingRate) {
        this.blowingRate = blowingRate;
    }

    public void setThawStartTemp(int thawStartTemp) {
        this.thawStartTemp = thawStartTemp;
    }

    public void setThawStopTemp(int thawStopTemp) {
        this.thawStopTemp = thawStopTemp;
    }

    public void setThawTime(int thawTime) {
        this.thawTime = thawTime;
    }

    public void setThawInv(int thawInv) {
        this.thawInv = thawInv;
    }


    public void setDustwiperClearPeriod(int dustwiperClearPeriod) {
        this.dustwiperClearPeriod = dustwiperClearPeriod;
    }

    public void setDustwiperClearRemain(int dustwiperClearRemain) {
        this.dustwiperClearRemain = dustwiperClearRemain;
    }

    public void setTimeMode(String timeMode) {
        this.timeMode = timeMode;
    }

    public void setTimerOneState(String timerOneState) {
        this.timerOneState = timerOneState;
    }

    public void setTimerTwoState(String timerTwoState) {
        this.timerTwoState = timerTwoState;
    }

    public void setTimerThreeState(String timerThreeState) {
        this.timerThreeState = timerThreeState;
    }

    public void setTimerOneStartHour(int timerOneStartHour) {
        this.timerOneStartHour = timerOneStartHour;
    }

    public void setTimerOneStartMin(int timerOneStartMin) {
        this.timerOneStartMin = timerOneStartMin;
    }

    public void setTimerOneEndHour(int timerOneEndHour) {
        this.timerOneEndHour = timerOneEndHour;
    }

    public void setTimerOneEndMin(int timerOneEndMin) {
        this.timerOneEndMin = timerOneEndMin;
    }

    public void setTimerTwoStartHour(int timerTwoStartHour) {
        this.timerTwoStartHour = timerTwoStartHour;
    }

    public void setTimerTwoStartMin(int timerTwoStartMin) {
        this.timerTwoStartMin = timerTwoStartMin;
    }

    public void setTimerTwoEndHour(int timerTwoEndHour) {
        this.timerTwoEndHour = timerTwoEndHour;
    }

    public void setTimerTwoEndMin(int timerTwoEndMin) {
        this.timerTwoEndMin = timerTwoEndMin;
    }

    public void setTimerThreeStartHour(int timerThreeStartHour) {
        this.timerThreeStartHour = timerThreeStartHour;
    }

    public void setTimerThreeStartMin(int timerThreeStartMin) {
        this.timerThreeStartMin = timerThreeStartMin;
    }

    public void setTimerThreeEndHour(int timerThreeEndHour) {
        this.timerThreeEndHour = timerThreeEndHour;
    }

    public void setTimerThreeEndMin(int timerThreeEndMin) {
        this.timerThreeEndMin = timerThreeEndMin;
    }

    public void setCo2Adj(int co2Adj) {
        this.co2Adj = co2Adj;
    }

    public void setFuncStatus(String funcStatus) {
        this.funcStatus = funcStatus;
    }

    public void setPmAdj(int pmAdj) {
        this.pmAdj = pmAdj;
    }

    public void setSensorIndoorTemp(int sensorIndoorTemp) {
        this.sensorIndoorTemp = sensorIndoorTemp;
    }

    public void setSensorOutdoorTemp(int sensorOutdoorTemp) {
        this.sensorOutdoorTemp = sensorOutdoorTemp;
    }

    public void setSensorFanTemp(int sensorFanTemp) {
        this.sensorFanTemp = sensorFanTemp;
    }

    public void setSensorExhaustTemp(int sensorExhaustTemp) {
        this.sensorExhaustTemp = sensorExhaustTemp;
    }

    public void setCo2Thickness(int co2Thickness) {
        this.co2Thickness = co2Thickness;
    }

    public void setIndoorPmThickness(int indoorPmThickness) {
        this.indoorPmThickness = indoorPmThickness;
    }

    public void setOutdoorPmThickness(int outdoorPmThickness) {
        this.outdoorPmThickness = outdoorPmThickness;
    }

    public void setSystemClockYear(int systemClockYear) {
        this.systemClockYear = systemClockYear;
    }

    public void setSystemClockMouth(int systemClockMouth) {
        this.systemClockMouth = systemClockMouth;
    }

    public void setSystemClockDay(int systemClockDay) {
        this.systemClockDay = systemClockDay;
    }

    public void setSystemClockHour(int systemClockHour) {
        this.systemClockHour = systemClockHour;
    }

    public void setSystemClockMin(int systemClockMin) {
        this.systemClockMin = systemClockMin;
    }

    public void setFaultStatus(String faultStatus) {
        this.faultStatus = faultStatus;
    }
}
