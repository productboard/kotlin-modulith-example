package com.productboard.modulith

/** Marker interface used to identify internal events that should propagate through the context hierarchy. */
abstract class ModulithEvent(private val source: ModuleApp) {
    fun getSource(): ModuleApp = source
}
