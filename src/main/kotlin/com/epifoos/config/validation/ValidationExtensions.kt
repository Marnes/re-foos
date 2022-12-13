package com.epifoos.plugins

import io.konform.validation.Constraint
import io.konform.validation.ValidationBuilder

inline fun <reified T> ValidationBuilder<T>.size(size: Int): Constraint<T> = addConstraint(
    "must have {0} items",
    size.toString()
) {
    when (it) {
        is Iterable<*> -> it.count() == size
        is Array<*> -> it.count() == size
        is Map<*, *> -> it.count() == size
        else -> throw IllegalStateException("size can not be applied to type ${T::class}")
    }
}

inline fun <reified T> ValidationBuilder<T>.rangeInclusive(range: IntRange): Constraint<T> = addConstraint(
    "must be greater than or equal to {0} and less than or equal to {1}",
    range.first.toString(),
    range.last.toString()
) {
    when (it) {
        is Number -> range.contains(it)
        else -> throw IllegalStateException("rangeInclusive can not be applied to type ${T::class}")
    }
}
