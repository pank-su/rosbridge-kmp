package com.github.thoebert.krosbridge

import com.github.thoebert.krosbridge.messages.primitive.msg.Duration
import com.github.thoebert.krosbridge.messages.primitive.msg.Time
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.Duration.Companion.seconds

fun Time.toInstant(): Instant {
    return Instant.fromEpochSeconds(this.sec.toLong(), this.nanosec)
}

fun Time.Companion.fromInstant(instant: Instant): Time {
    return Time((instant.epochSeconds - instant.nanosecondsOfSecond / 1_000_000_000).toInt(), instant.nanosecondsOfSecond.toLong())
}

fun Time.Companion.now(): Time {
    return fromInstant(Clock.System.now())
}

fun Duration.toDuration(): kotlin.time.Duration {
    return sec.seconds + nanosec.nanoseconds
}

fun Duration.Companion.fromDuration(duration: kotlin.time.Duration): Duration {
    return Duration(
        duration.inWholeSeconds.toInt(),
        (duration.inWholeNanoseconds - duration.inWholeSeconds * 1_000_000_000)
    )
}