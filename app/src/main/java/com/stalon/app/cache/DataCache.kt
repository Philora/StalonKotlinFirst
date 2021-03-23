package com.stalon.app.cache

/**
 * This is singleton class to cache profile data and provides helper methods to access the same.
 */
class DataCache {

    companion object {
        private var sInstance: DataCache? = null
        /**
         * Factory method to get profile data cache instance
         *
         * @return object
         */
        @get:Synchronized
        val instance: DataCache
            get() {
                if (sInstance == null) {
                    sInstance = DataCache()
                }
                return sInstance!!
            }
    }
}