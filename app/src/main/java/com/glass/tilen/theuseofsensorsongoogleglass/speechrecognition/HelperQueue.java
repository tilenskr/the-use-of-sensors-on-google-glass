package com.glass.tilen.theuseofsensorsongoogleglass.speechrecognition;

import java.util.ArrayDeque;

/**
 * Created by Tilen on 15.8.2015.
 */
public class HelperQueue {

    public static int INITIALIZE = 0;
    public static int SHUTDOWN = 1;
    private class QueueItem
    {
        private Runnable runnable;
        /** 0 - initialize 1 - shutdown **/
        private int itemId;

        public QueueItem(Runnable runnable, int itemId) {
            this.runnable = runnable;
            this.itemId = itemId;
        }
    }
    private ArrayDeque<QueueItem> queue;
    private int currentItemId;

    public HelperQueue()
    {
        queue = new ArrayDeque<QueueItem>();
        currentItemId = -1;
    }

    /** when runnable finish **/
    public Runnable poll()
    {
        currentItemId = -1;
        QueueItem queueItem = queue.poll();
        if(queueItem != null)
        {
            currentItemId = queueItem.itemId;
            return queueItem.runnable;
        }
        return null;
    }

    public Runnable addRunnable(Runnable runnable, int itemId)
    {
        if(currentItemId == -1) // no runnable running
        {
            currentItemId = itemId;
            return runnable;
        }
        else if(currentItemId == itemId)
        {
            queue.clear();
        }
        else
        {
            queue.add(new QueueItem(runnable, itemId));
        }
        return null;

        /*
        if(itemId == INITIALIZE)
        {

        }
        else if (itemId == SHUTDOWN)
        {

        }*/
    }
}
