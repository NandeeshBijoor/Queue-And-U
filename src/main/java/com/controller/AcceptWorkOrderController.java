package com.controller;

import com.common.Constants;
import com.common.WorkOrderQueueComparator;
import com.service.AcceptWorkOrder;
import com.utility.IDClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

@RestController
    public class AcceptWorkOrderController {

        @Autowired
        Environment env;
        Comparator<AcceptWorkOrder> comparator = new WorkOrderQueueComparator();

        Queue<AcceptWorkOrder> acceptWorkOrderQueue = new PriorityQueue<>(Constants.MAX_QUEUE_SIZE,comparator);//defining queue with custom comparator

        private final static Logger LOGGER = Logger.getLogger(AcceptWorkOrderController.class.getName());

        @RequestMapping(value="/add",method = RequestMethod.POST,produces= MediaType.APPLICATION_JSON_VALUE)
        public String addItem(@RequestBody Map requestMap  ) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.ENGLISH);
            long requesterID = Long.parseLong((String) requestMap.get("id"));
            Date requestDate;
            try {
                requestDate =format.parse(requestMap.get("date").toString());
            } catch (ParseException e) {
                e.printStackTrace();
                return "Request cannot be processed";
            }
            //@RequestParam(value = "id") long requesterID, @RequestParam(value = "date") @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ssXXX") Date requestDate

            if(requesterID<0||requesterID>Constants.MAX_RRQUEST_ID)
                return "RequestId should be within range of 1 to 9223372036854775807";

            else if (requesterID%3==0 && requesterID%5==0)
                return addDataToQueue(acceptWorkOrderQueue, requesterID, requestDate, IDClass.Management);

            else if (requesterID%5==0)
                return addDataToQueue(acceptWorkOrderQueue, requesterID, requestDate, IDClass.Vip);

            else if (requesterID%3==0)
                return addDataToQueue(acceptWorkOrderQueue, requesterID, requestDate, IDClass.Priority);

            else if (requesterID%3!=0 && requesterID%5!=0)
                return addDataToQueue(acceptWorkOrderQueue, requesterID, requestDate, IDClass.Normal);

            else
                return "Request cannot be processed";

        }
        @RequestMapping(value="/top",method = RequestMethod.GET,produces= MediaType.APPLICATION_JSON_VALUE)
        public AcceptWorkOrder getItem() {
            return pollDataFromQueue(acceptWorkOrderQueue);
        }

        @RequestMapping(value="/list",method = RequestMethod.GET,produces= MediaType.APPLICATION_JSON_VALUE)
        public Iterator listItems() {
            return listQueue(acceptWorkOrderQueue);
        }

        @RequestMapping(value="/remove",method = RequestMethod.POST,produces= MediaType.APPLICATION_JSON_VALUE)
        public String removeItem(@RequestBody Map requestMap) {
            long requesterID = Long.parseLong((String) requestMap.get("id"));
            boolean removedStatus= removeQueueItem(acceptWorkOrderQueue,requesterID);
            if (removedStatus)
            return requesterID+" removed from queue";
            else
                return requesterID+" not present in queue";
        }

        @RequestMapping(value="/position",method = RequestMethod.POST,produces= MediaType.APPLICATION_JSON_VALUE)
        public int getItemPosition(@RequestBody Map requestMap) {
            long requesterID = Long.parseLong((String) requestMap.get("id"));
            return positionCheck(acceptWorkOrderQueue,requesterID);
        }

        @RequestMapping(value="/meantime",method = RequestMethod.POST,produces= MediaType.APPLICATION_JSON_VALUE)
        public long getAverageTime(@RequestBody Map requestMap) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.ENGLISH);
            Date requestDate;
            try {
                requestDate =format.parse(requestMap.get("date").toString());
            } catch (ParseException e) {
                e.printStackTrace();
                requestDate=new Date();//bad code :)
            }
            return averageTime(acceptWorkOrderQueue,requestDate);
        }

        private static String addDataToQueue(Queue<AcceptWorkOrder> acceptWorkOrderQueue, Long requesterID, Date requestDate, IDClass IDClass) {
            AcceptWorkOrder workOrder = new AcceptWorkOrder(requesterID, requestDate, IDClass);
            if (alreadyExists(acceptWorkOrderQueue,requesterID)){
                return "Request ID: "+requesterID+ "already present in the queue";
            }
            else {
                acceptWorkOrderQueue.add(new AcceptWorkOrder(requesterID, requestDate, IDClass));

                return "Work request with request id" + requesterID + " has been added to request Queue on " + requestDate;
            }
        }

        private Iterator listQueue(Queue<AcceptWorkOrder> acceptWorkOrderQueue) {
            Iterator <AcceptWorkOrder> itr=null;
            if (!acceptWorkOrderQueue.isEmpty())
            {
                itr= acceptWorkOrderQueue.iterator();
            }
            return itr;
        }

        private AcceptWorkOrder pollDataFromQueue(Queue<AcceptWorkOrder> customerPriorityQueue) {
            while (true) {
                AcceptWorkOrder workorder = customerPriorityQueue.poll();
                if (workorder == null) break;
                return workorder;
            }
            return null;
        }

        private static boolean alreadyExists(Queue<AcceptWorkOrder> customerPriorityQueue, Long requestId) {
           if (customerPriorityQueue.isEmpty())
                    return false;
           else {
               Iterator <AcceptWorkOrder> itr = customerPriorityQueue.iterator();
               while(itr.hasNext())
               {
                   if (itr.next().getRequesterId()==requestId)
                   return true;
               }
                return false;
            }

        }
        private static boolean removeQueueItem(Queue<AcceptWorkOrder> workOrderQueue, Long requestId) {
            if (!workOrderQueue.isEmpty())
            {
                Iterator <AcceptWorkOrder> itr = workOrderQueue.iterator();
                while(itr.hasNext())
                {
                    if (itr.next().getRequesterId()==requestId)
                        itr.remove();
                        return true;
                }
                        return false;
            }
            else
                return true;

        }
        private static int positionCheck(Queue<AcceptWorkOrder> workOrderQueue, Long requestId) {
            int position=-1;
            if (!workOrderQueue.isEmpty())
            {
                Iterator <AcceptWorkOrder> itr = workOrderQueue.iterator();
                while(itr.hasNext())
                {
                    position++;
                    if (itr.next().getRequesterId()==requestId)
                        return position;
                }

            }
            return -1;
        }

        private static long averageTime(Queue<AcceptWorkOrder> workOrderQueue, Date currentTime) {

            long seconds=0;
            if (!workOrderQueue.isEmpty())
            {
                Iterator <AcceptWorkOrder> itr = workOrderQueue.iterator();
                while(itr.hasNext())
                {

                    seconds=seconds+(currentTime.getTime()-itr.next().getRequestDate().getTime())/1000;
                    LOGGER.info("seconds"+seconds);

                }

            }
            return seconds;
        }
    }
