package com.common;

import com.service.*;
import com.controller.*;
import com.utility.IDClass;

import java.util.Comparator;
import java.util.Date;
import java.util.logging.Logger;

public class WorkOrderQueueComparator implements Comparator<AcceptWorkOrder> {

    private final static Logger LOGGER = Logger.getLogger(AcceptWorkOrderController.class.getName());

    @Override
    public int compare(AcceptWorkOrder acceptWorkOrderObject1, AcceptWorkOrder acceptWorkOrderObject2) {

        //TO-DO: Fix issue in comparator
        double timeSpentInSecondsQueueForFirstItem= getMaxTimeSpentByItem(acceptWorkOrderObject1);
        double timeSpentInSecondsQueueForSecondItem= getMaxTimeSpentByItem(acceptWorkOrderObject2);

        if(acceptWorkOrderObject1.IDClass.equals(IDClass.Management)&& acceptWorkOrderObject2.IDClass.equals(IDClass.Management)) {
            if (timeSpentInSecondsQueueForFirstItem>timeSpentInSecondsQueueForSecondItem) return 1;else return 0;
        }

        else if(acceptWorkOrderObject1.IDClass.equals(IDClass.Management)) {
            return acceptWorkOrderObject1.IDClass.compareTo(acceptWorkOrderObject2.IDClass);
        }

        else if (acceptWorkOrderObject2.IDClass.equals(IDClass.Management)) {
            return acceptWorkOrderObject1.IDClass.compareTo(acceptWorkOrderObject2.IDClass);
        }

        return Integer.compare((int)timeSpentInSecondsQueueForFirstItem,(int)timeSpentInSecondsQueueForSecondItem);
    }


    public double getMaxTimeSpentByItem(AcceptWorkOrder acceptWorkOrderObject) {
        IDClass IDClass =acceptWorkOrderObject.IDClass;
        Date currentTime=new Date();
        double timeSpentInSeconds = (currentTime.getTime() - acceptWorkOrderObject.getRequestDate().getTime())/1000;
        switch(IDClass){
            case Normal:
                return timeSpentInSeconds;
            case Priority:
                return Math.max(3, timeSpentInSeconds*(Math.log(timeSpentInSeconds)));
            case Vip:
                return Math.max(4, 2*timeSpentInSeconds*(Math.log(timeSpentInSeconds)));
        }
        return timeSpentInSeconds;
    }

}
