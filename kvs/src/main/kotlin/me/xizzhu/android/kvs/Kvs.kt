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
     * Returns the value corresponding to the given [key], or `null` if such a key does not exist.
     * @throws [KvsException]
     */
    operator fun get(key: ByteArray): ByteArray?

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
}

/**
 * Returns the value corresponding to the given [key], or `null` if such a key does not exist.
 * @throws [KvsException]
 */
operator fun Kvs.get(key: String): ByteArray? = get(key.toByteArray())

/**
 * Associates the specified [value] with the given [key].
 * @throws [KvsException]
 */
operator fun Kvs.set(key: String, value: ByteArray) {
    set(key.toByteArray(), value)
}

/**
 * Returns the boolean value corresponding to the given [key].
 * @throws [KvsException]
 */
fun Kvs.getBoolean(key: String, defValue: Boolean = false): Boolean =
        get(key)?.first()?.let { it != 0.toByte() } ?: defValue

/**
 * Associates the specified [value] with the given [key].
 * @throws [KvsException]
 */
operator fun Kvs.set(key: String, value: Boolean) {
    set(key.toByteArray(), byteArrayOf(if (value) 1 else 0))
}

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
 * Associates the specified [value] with the given [key].
 * @throws [KvsException]
 */
operator fun Kvs.set(key: String, value: Double) {
    set(key.toByteArray(), ByteBuffer.allocate(8).putDouble(value).array())
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
 * Associates the specified [value] with the given [key].
 * @throws [KvsException]
 */
operator fun Kvs.set(key: String, value: Float) {
    set(key.toByteArray(), ByteBuffer.allocate(4).putFloat(value).array())
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
 * Associates the specified [value] with the given [key].
 * @throws [KvsException]
 */
operator fun Kvs.set(key: String, value: Int) {
    set(key.toByteArray(), ByteBuffer.allocate(4).putInt(value).array())
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
 * Associates the specified [value] with the given [key].
 * @throws [KvsException]
 */
operator fun Kvs.set(key: String, value: Long) {
    set(key.toByteArray(), ByteBuffer.allocate(8).putLong(value).array())
}

/**
 * Returns the string value corresponding to the given [key].
 * @throws [KvsException]
 */
fun Kvs.getString(key: String, defValue: String = ""): String =
        get(key)?.let { String(it) } ?: defValue

/**
 * Associates the specified [value] with the given [key].
 * @throws [KvsException]
 */
operator fun Kvs.set(key: String, value: String) {
    set(key.toByteArray(), value.toByteArray())
}

/**
 * Removes the specified [key] and its corresponding value.
 * @return `true` if the value was removed.
 * @throws [KvsException]
 */
fun Kvs.remove(key: String): Boolean = remove(key.toByteArray())
