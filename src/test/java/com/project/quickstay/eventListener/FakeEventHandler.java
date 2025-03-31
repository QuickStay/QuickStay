package com.project.quickstay.eventListener;

import com.project.quickstay.common.eventListener.ReservationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class FakeEventHandler {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle_T_After_Commit(ReservationEvent event) {
        event.getRoom().plusReservedCount();
    }

    @EventListener
    public void handle_E(ReservationEvent event) {
        event.getRoom().plusReservedCount();
    }

}
