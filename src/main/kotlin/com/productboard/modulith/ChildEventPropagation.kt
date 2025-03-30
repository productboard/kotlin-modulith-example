package com.productboard.modulith

import mu.KLogging
import org.springframework.context.ApplicationListener
import org.springframework.context.PayloadApplicationEvent

/**
 * Listener to events that are instances of [PayloadApplicationEvent]. Repacks to custom implementation of
 * [PayloadApplicationEvent] to avoid loops (because child context forwards to root again automatically). Forwards
 * events to all children contexts and uses default event handling behaviour.
 */
class ForwardToChildrenListener : ApplicationListener<PayloadApplicationEvent<out ModulithEvent>> {

    override fun onApplicationEvent(event: PayloadApplicationEvent<out ModulithEvent>) {
        if (event is NotForwardingPayloadApplicationEvent) {
            logger.trace { "action=not_forward_event event=${event.payload::class.simpleName}" }
        } else {
            logger.trace { "action=forward_event event=${event.payload::class.simpleName}" }
            ChildContextRegistry.getAll().forEach { (id, context) ->
                // do not publish event back to source module
                if (event.payload.getSource().configName != id) {
                    context.publishEvent(NotForwardingPayloadApplicationEvent(event.source, event.payload))
                }
            }
        }
    }

    companion object : KLogging()
}

/** Custom implementation of [PayloadApplicationEvent] which is used to prevent loops in event handling */
class NotForwardingPayloadApplicationEvent<T : ModulithEvent>(source: Any, payload: T) :
    PayloadApplicationEvent<T>(source, payload, null)
