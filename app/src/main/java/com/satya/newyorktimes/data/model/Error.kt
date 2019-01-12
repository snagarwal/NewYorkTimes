package com.satya.newyorktimes.data.model


/**
 * All errors are dispatched in this format from repositories.
 */
class Error(val message: String?) {

    companion object {
        fun build(
            message: String?
        ): Error {
            return Error( message)
        }
    }

}
