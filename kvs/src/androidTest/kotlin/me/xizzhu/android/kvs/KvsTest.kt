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

import java.io.File
import kotlin.test.*

class KvsTest {
    private lateinit var kvs: Kvs

    @BeforeTest
    fun setup() {
        kvs = newKvs {
            dir = getTestDir().apply { mkdirs() }.absolutePath
        }
    }

    private fun getTestDir(): File = File(System.getProperty("java.io.tmpdir"), "KvsTest")

    @AfterTest
    fun tearDown() {
        kvs.close()
        getTestDir().deleteRecursively()
    }

    @Test(expected = KvsException::class)
    fun testContains_withEmptyKey() {
        kvs.contains(byteArrayOf())
    }

    @Test
    fun testContains_withNonExistingKey() {
        assertFalse(kvs.contains(byteArrayOf(1)))
    }

    @Test(expected = KvsException::class)
    fun testGet_withEmptyKey() {
        kvs[byteArrayOf()]
    }

    @Test
    fun testGet_withNonExistingKey() {
        assertNull(kvs[byteArrayOf(1)])
    }

    @Test(expected = KvsException::class)
    fun testSet_withEmptyKey() {
        kvs.edit()[byteArrayOf()] = byteArrayOf(1)
    }

    @Test(expected = KvsException::class)
    fun testSet_withEmptyValue() {
        kvs.edit()[byteArrayOf(1)] = byteArrayOf()
    }

    @Test(expected = KvsException::class)
    fun testRemove_withEmptyKey() {
        kvs.edit().remove(byteArrayOf())
    }

    @Test
    fun testRemove_withNonExistingKey() {
        with(kvs.edit()) {
            assertFalse(remove(byteArrayOf(1)))
            abort()
        }
    }

    @Test
    fun testSetThenGet() {
        assertFalse(kvs.contains(byteArrayOf(1)))
        assertNull(kvs[byteArrayOf(1)])

        with(kvs.edit()) {
            this[byteArrayOf(1)] = byteArrayOf(1, 2, 3, 4, 5)
            commit()
        }
        assertTrue(kvs.contains(byteArrayOf(1)))
        assertTrue(byteArrayOf(1, 2, 3, 4, 5).contentEquals(kvs[byteArrayOf(1)]))
    }

    @Test
    fun testSetGetAndRemove() {
        assertFalse(kvs.contains(byteArrayOf(1)))
        assertNull(kvs[byteArrayOf(1)])

        with(kvs.edit()) {
            this[byteArrayOf(1)] = byteArrayOf(1, 2, 3, 4, 5)
            commit()
        }
        assertTrue(kvs.contains(byteArrayOf(1)))
        assertTrue(byteArrayOf(1, 2, 3, 4, 5).contentEquals(kvs[byteArrayOf(1)]))

        with(kvs.edit()) {
            assertTrue(remove(byteArrayOf(1)))
            commit()
        }
        assertFalse(kvs.contains(byteArrayOf(1)))
        assertNull(kvs[byteArrayOf(1)])

        with(kvs.edit()) {
            assertFalse(remove(byteArrayOf(1)))
            abort()
        }
    }
}
