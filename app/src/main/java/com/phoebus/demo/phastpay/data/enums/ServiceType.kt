package com.phoebus.demo.phastpay.data.enums

enum class ServiceType {
    ALL,
    BIZUM,
    MBWAY;

    companion object {
        fun fromString(value: String, default: ServiceType = ALL): ServiceType {
            return try {
                ServiceType.valueOf(value.uppercase())
            } catch (e: IllegalArgumentException) {
                default
            }
        }
    }
}