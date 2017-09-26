package com.service;

import com.controller.AcceptWorkOrderController;
import com.utility.IDClass;

import java.util.Date;
import java.util.logging.Logger;

public class AcceptWorkOrder{

    public long requesterId;
    public Date requestDate;
    public com.utility.IDClass IDClass;

    private final static Logger LOGGER = Logger.getLogger(AcceptWorkOrderController.class.getName());

    public AcceptWorkOrder(long requesterId, Date requestDate, IDClass idClass) {
        this.requesterId = requesterId;
        this.requestDate = requestDate;
        this.IDClass = idClass;
    }

    public long getRequesterId() {
        return requesterId;
    }

    public Date getRequestDate() {
        return requestDate;
    }
}

