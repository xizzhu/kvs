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

import me.xizzhu.android.kvs.KvsConfig

internal class Env(config: KvsConfig) : AutoCloseable {
    private val nativeEnv = Jni.createEnv()

    init {
        Jni.openEnv(nativeEnv, config.dir, 0, 0x180 /* 0600 */)
    }

    override fun close() {
        Jni.closeEnv(nativeEnv)
    }

    fun newTransaction(readOnly: Boolean): Transaction = Transaction(nativeEnv, readOnly)
}
