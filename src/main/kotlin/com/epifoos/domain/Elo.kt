package com.epifoos.domain

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.serialization.*
import io.ktor.serialization.jackson.*
import io.ktor.util.reflect.*
import io.ktor.utils.io.*
import io.ktor.utils.io.charsets.*
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.vendors.currentDialect

class Elo(x: Number) : Number(), Comparable<Elo> {
    companion object {
        val ZERO = Elo(0.0)
    }

    private val amount: Double = x.toDouble()

    override fun toByte(): Byte = amount.toInt().toByte()
    override fun toChar(): Char = amount.toInt().toChar()
    override fun toDouble(): Double = amount
    override fun toFloat(): Float = amount.toFloat()
    override fun toInt(): Int = amount.toInt()
    override fun toLong(): Long = amount.toLong()
    override fun toShort(): Short = amount.toInt().toShort()

    operator fun minus(other: Number) = Elo(amount - other.toDouble())
    operator fun plus(other: Number) = Elo(amount + other.toDouble())
    operator fun times(other: Number) = Elo(amount * other.toDouble())
    operator fun div(other: Number) = Elo(amount / other.toDouble())
    operator fun rem(other: Number) = Elo(amount % other.toDouble())
    operator fun compareTo(other: Number): Int = amount.compareTo(other.toDouble())
    override fun compareTo(other: Elo): Int = amount.compareTo(other.toDouble())
}

class EloDataType : ColumnType() {
    override fun sqlType(): String = currentDialect.dataTypeProvider.doubleType()
    override fun notNullValueToDB(value: Any): Any {
        val v = if (value is Elo) value.toDouble() else value
        return super.notNullValueToDB(v)
    }
    override fun valueFromDB(value: Any): Elo = when (value) {
        is Double -> Elo(value)
        is Number -> Elo(value.toDouble())
        is String -> Elo(value.toDouble())
        else -> error("Unexpected value of type Elo: $value of ${value::class.qualifiedName}")
    }
}

fun Table.elo(name: String): Column<Elo> = registerColumn(name, EloDataType())

@JvmName("sumOfElo")
fun Iterable<Elo>.sum(): Elo {
    var sum: Elo = Elo.ZERO
    for (element in this) {
        sum += element
    }
    return sum
}

@JvmName("averageOfElo")
fun Iterable<Elo>.average(): Elo {
    var sum: Elo = Elo.ZERO
    var count = 0
    for (element in this) {
        sum += element
        ++count
    }
    return if (count == 0) Elo.ZERO else sum / count
}
