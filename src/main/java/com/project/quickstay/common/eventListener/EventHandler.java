package com.project.quickstay.common.eventListener;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class EventHandler {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleReservationRegister(ReservationEvent event) {
        event.getRoom().plusReservedCount();
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleReservationCancel(ReservationEvent event) {
        event.getRoom().minusReservedCount();
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleReviewWrite(ReviewEvent event) {
        event.getPlace().reviewAdded(event.getReviewScore());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleReviewDelete(ReviewEvent event) {
        event.getPlace().reviewDeleted(event.getReviewScore());
    }

}
