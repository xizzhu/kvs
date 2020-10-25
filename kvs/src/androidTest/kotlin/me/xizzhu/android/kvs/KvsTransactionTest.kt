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

class KvsTransactionTest {
    private lateinit var kvs: Kvs

    @BeforeTest
    fun setup() {
        kvs = newKvs {
            dir = getTestDir().apply { mkdirs() }.absolutePath
        }
    }

    private fun getTestDir(): File = File(System.getProperty("java.io.tmpdir"), "KvsTransactionTest")

    @AfterTest
    fun tearDown() {
        kvs.close()
        getTestDir().deleteRecursively()
    }

    @Test
    fun testReadOnlyTransaction() {
        kvs["key"] = "value"
        assertEquals("value", kvs.getString("key"))

        kvs.withTransaction(readOnly = true) {
            assertEquals("value", it.getString("key"))
            assertNull(it["non exist"])
        }
    }

    @Test(expected = KvsException::class)
    fun testReadOnlyTransaction_tryingToWrite() {
        kvs.withTransaction(readOnly = true) {
            it["key"] = "value"
        }
    }

    @Test
    fun testAbortTransaction() {
        kvs["key"] = "value"
        assertEquals("value", kvs.getString("key"))

        var exceptionCaught = false
        try {
            kvs.withTransaction(readOnly = false) {
                assertEquals("value", it.getString("key"))

                it.remove("key")
                assertNull(it["key"])
                assertEquals("value", kvs.getString("key")) // `kvs` is NOT in this transaction

                // crash to abort the transaction
                throw RuntimeException("random exception")
            }
        } catch (e: RuntimeException) {
            assertEquals("random exception", e.message)
            exceptionCaught = true
        }
        assertTrue(exceptionCaught)

        assertEquals("value", kvs.getString("key"))
    }

    @Test
    fun testCommitTransaction() {
        kvs["key"] = "value"
        assertEquals("value", kvs.getString("key"))

        kvs.withTransaction(readOnly = false) {
            assertEquals("value", it.getString("key"))

            it.remove("key")
            assertNull(it["key"])
            assertEquals("value", kvs.getString("key")) // `kvs` is NOT in this transaction

            it["key"] = "new value"
            assertEquals("new value", it.getString("key"))
            assertEquals("value", kvs.getString("key")) // `kvs` is NOT in this transaction
        }

        assertEquals("new value", kvs.getString("key"))
    }

    @Test(expected = KvsException::class)
    fun testNestedTransaction() {
        kvs.withTransaction { it.withTransaction { } }
    }

    @Test(expected = KvsException::class)
    fun testClose() {
        kvs.withTransaction { it.close() }
    }
}
