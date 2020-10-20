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

package me.xizzhu.android.kvs.lmdb

internal object Jni {
    // Return codes, see: http://www.lmdb.tech/doc/group__errors.html

    @JvmField
    var MDB_SUCCESS: Int = -1

    @JvmField
    var MDB_KEYEXIST: Int = -1

    @JvmField
    var MDB_NOTFOUND: Int = -1

    @JvmField
    var MDB_PAGE_NOTFOUND: Int = -1

    @JvmField
    var MDB_CORRUPTED: Int = -1

    @JvmField
    var MDB_PANIC: Int = -1

    @JvmField
    var MDB_VERSION_MISMATCH: Int = -1

    @JvmField
    var MDB_INVALID: Int = -1

    @JvmField
    var MDB_MAP_FULL: Int = -1

    @JvmField
    var MDB_DBS_FULL: Int = -1

    @JvmField
    var MDB_READERS_FULL: Int = -1

    @JvmField
    var MDB_TLS_FULL: Int = -1

    @JvmField
    var MDB_TXN_FULL: Int = -1

    @JvmField
    var MDB_CURSOR_FULL: Int = -1

    @JvmField
    var MDB_PAGE_FULL: Int = -1

    @JvmField
    var MDB_MAP_RESIZED: Int = -1

    @JvmField
    var MDB_INCOMPATIBLE: Int = -1

    @JvmField
    var MDB_BAD_RSLOT: Int = -1

    @JvmField
    var MDB_BAD_TXN: Int = -1

    @JvmField
    var MDB_BAD_VALSIZE: Int = -1

    @JvmField
    var MDB_BAD_DBI: Int = -1

    init {
        System.loadLibrary("lmdb")
        initialize()
    }

    private external fun initialize()

    external fun createEnv(): Long

    external fun closeEnv(nativeEnv: Long)

    external fun openEnv(nativeEnv: Long, path: String, flags: Int, mode: Int)
}
