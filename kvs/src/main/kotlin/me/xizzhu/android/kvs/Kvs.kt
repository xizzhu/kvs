/*
 * Copyright (C) 2020 Xizhi Zhu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.xizzhu.android.kvs

import java.io.Closeable
import java.nio.ByteBuffer

interface Kvs : Closeable {
    /**
     * Returns `true` if a value associated with the given [key] exists, or `false` otherwise.
     */
    operator fun contains(key: ByteArray): Boolean

    /**
     * Returns the value corresponding to the given [key], or `null` if such a key does not exist.
     * @throws [KvsException]
     */
    operator fun get(key: ByteArray): ByteArray?

    /**
     * Creates a new [Editor] to mutate the store.
     * @return a new [Editor] instance.
     */
    fun edit(): Editor

    interface Editor {
        /**
         * Associates the specified [value] with the given [key].
         * @throws [KvsException]
         */
        operator fun set(key: ByteArray, value: ByteArray)

        /**
         * Removes the specified [key] and its corresponding value.
         * @return `true` if the value was removed.
         * @throws [KvsException]
         */
        fun remove(key: ByteArray): Boolean

        /**
         * Commits the changes.
         * @throws [KvsException]
         */
        fun commit()

        /**
         * Discards the changes.
         * @throws [KvsException]
         */
        fun abort()
    }
}

/**
 * Returns `true` if a value associated with the given [key] exists, or `false` otherwise.
 */
operator fun Kvs.contains(key: String): Boolean = contains(key.toByteArray())

/**
 * Returns the value corresponding to the given [key], or `null` if such a key does not exist.
 * @throws [KvsException]
 */
operator fun Kvs.get(key: String): ByteArray? = get(key.toByteArray())

/**
 * Returns the boolean value corresponding to the given [key].
 * @throws [KvsException]
 */
fun Kvs.getBoolean(key: String, defValue: Boolean = false): Boolean =
        get(key)?.first()?.let { it != 0.toByte() } ?: defValue

/**
 * Returns the double value corresponding to the given [key].
 * @throws [KvsException]
 */
fun Kvs.getDouble(key: String, defValue: Double = 0.0): Double {
    val bytes = get(key) ?: return defValue
    if (bytes.size < 8) throw KvsException("Cannot convert value for '$key' to double")
    return ByteBuffer.wrap(bytes).double
}

/**
 * Returns the float value corresponding to the given [key].
 * @throws [KvsException]
 */
fun Kvs.getFloat(key: String, defValue: Float = 0.0F): Float {
    val bytes = get(key) ?: return defValue
    if (bytes.size < 4) throw KvsException("Cannot convert value for '$key' to float")
    return ByteBuffer.wrap(bytes).float
}

/**
 * Returns the int value corresponding to the given [key].
 * @throws [KvsException]
 */
fun Kvs.getInt(key: String, defValue: Int = 0): Int {
    val bytes = get(key) ?: return defValue
    if (bytes.size < 4) throw KvsException("Cannot convert value for '$key' to int")
    return ByteBuffer.wrap(bytes).int
}

/**
 * Returns the long value corresponding to the given [key].
 * @throws [KvsException]
 */
fun Kvs.getLong(key: String, defValue: Long = 0L): Long {
    val bytes = get(key) ?: return defValue
    if (bytes.size < 8) throw KvsException("Cannot convert value for '$key' to long")
    return ByteBuffer.wrap(bytes).long
}

/**
 * Returns the string value corresponding to the given [key].
 * @throws [KvsException]
 */
fun Kvs.getString(key: String, defValue: String = ""): String =
        get(key)?.let { String(it) } ?: defValue

/**
 * Allows mutating the store.
 * @throws [KvsException]
 */
inline fun <R> Kvs.edit(block: (Kvs.Editor) -> R): R {
    var exception: Throwable? = null
    val editor = edit()
    try {
        return block(editor)
    } catch (e: Throwable) {
        exception = e
        throw e
    } finally {
        try {
            if (exception == null) editor.commit() else editor.abort()
        } catch (e: Throwable) {
            if (exception == null) throw e
        }
    }
}

/**
 * Associates the specified [value] with the given [key].
 * @throws [KvsException]
 */
operator fun Kvs.Editor.set(key: String, value: ByteArray) {
    set(key.toByteArray(), value)
}

/**
 * Associates the specified [value] with the given [key].
 * @throws [KvsException]
 */
operator fun Kvs.Editor.set(key: String, value: Boolean) {
    set(key.toByteArray(), byteArrayOf(if (value) 1 else 0))
}

/**
 * Associates the specified [value] with the given [key].
 * @throws [KvsException]
 */
operator fun Kvs.Editor.set(key: String, value: Double) {
    set(key.toByteArray(), ByteBuffer.allocate(8).putDouble(value).array())
}

/**
 * Associates the specified [value] with the given [key].
 * @throws [KvsException]
 */
operator fun Kvs.Editor.set(key: String, value: Float) {
    set(key.toByteArray(), ByteBuffer.allocate(4).putFloat(value).array())
}

/**
 * Associates the specified [value] with the given [key].
 * @throws [KvsException]
 */
operator fun Kvs.Editor.set(key: String, value: Int) {
    set(key.toByteArray(), ByteBuffer.allocate(4).putInt(value).array())
}

/**
 * Associates the specified [value] with the given [key].
 * @throws [KvsException]
 */
operator fun Kvs.Editor.set(key: String, value: Long) {
    set(key.toByteArray(), ByteBuffer.allocate(8).putLong(value).array())
}

/**
 * Associates the specified [value] with the given [key].
 * @throws [KvsException]
 */
operator fun Kvs.Editor.set(key: String, value: String) {
    set(key.toByteArray(), value.toByteArray())
}

/**
 * Removes the specified [key] and its corresponding value.
 * @return `true` if the value was removed.
 * @throws [KvsException]
 */
fun Kvs.Editor.remove(key: String): Boolean = remove(key.toByteArray())
