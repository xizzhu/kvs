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

#include <cstdlib>
#include <jni.h>
#include "lmdb/libraries/liblmdb/lmdb.h"

void throwLmdbException(JNIEnv *env, int errorCode) {
    if (errorCode == MDB_SUCCESS) {
        return;
    }

    jclass lmdbExceptionClass = env->FindClass("me/xizzhu/android/kvs/lmdb/LmdbException");
    jmethodID ctorMethod = env->GetMethodID(lmdbExceptionClass, "<init>", "(ILjava/lang/String;)V");
    jobject lmdbException = env->NewObject(lmdbExceptionClass, ctorMethod, errorCode, env->NewStringUTF(mdb_strerror(errorCode)));
    env->Throw((jthrowable) lmdbException);
}

extern "C" JNIEXPORT void JNICALL
Java_me_xizzhu_android_kvs_lmdb_Jni_initialize(JNIEnv *env, jobject thisObj) {
    jclass thisCls = (*env).GetObjectClass(thisObj);
    (*env).SetStaticIntField(thisCls, (*env).GetStaticFieldID(thisCls, "MDB_SUCCESS", "I"), (jint) MDB_SUCCESS);
    (*env).SetStaticIntField(thisCls, (*env).GetStaticFieldID(thisCls, "MDB_KEYEXIST", "I"), (jint) MDB_KEYEXIST);
    (*env).SetStaticIntField(thisCls, (*env).GetStaticFieldID(thisCls, "MDB_NOTFOUND", "I"), (jint) MDB_NOTFOUND);
    (*env).SetStaticIntField(thisCls, (*env).GetStaticFieldID(thisCls, "MDB_PAGE_NOTFOUND", "I"), (jint) MDB_PAGE_NOTFOUND);
    (*env).SetStaticIntField(thisCls, (*env).GetStaticFieldID(thisCls, "MDB_CORRUPTED", "I"), (jint) MDB_CORRUPTED);
    (*env).SetStaticIntField(thisCls, (*env).GetStaticFieldID(thisCls, "MDB_PANIC", "I"), (jint) MDB_PANIC);
    (*env).SetStaticIntField(thisCls, (*env).GetStaticFieldID(thisCls, "MDB_VERSION_MISMATCH", "I"), (jint) MDB_VERSION_MISMATCH);
    (*env).SetStaticIntField(thisCls, (*env).GetStaticFieldID(thisCls, "MDB_INVALID", "I"), (jint) MDB_INVALID);
    (*env).SetStaticIntField(thisCls, (*env).GetStaticFieldID(thisCls, "MDB_MAP_FULL", "I"), (jint) MDB_MAP_FULL);
    (*env).SetStaticIntField(thisCls, (*env).GetStaticFieldID(thisCls, "MDB_DBS_FULL", "I"), (jint) MDB_DBS_FULL);
    (*env).SetStaticIntField(thisCls, (*env).GetStaticFieldID(thisCls, "MDB_READERS_FULL", "I"), (jint) MDB_READERS_FULL);
    (*env).SetStaticIntField(thisCls, (*env).GetStaticFieldID(thisCls, "MDB_TLS_FULL", "I"), (jint) MDB_TLS_FULL);
    (*env).SetStaticIntField(thisCls, (*env).GetStaticFieldID(thisCls, "MDB_TXN_FULL", "I"), (jint) MDB_TXN_FULL);
    (*env).SetStaticIntField(thisCls, (*env).GetStaticFieldID(thisCls, "MDB_CURSOR_FULL", "I"), (jint) MDB_CURSOR_FULL);
    (*env).SetStaticIntField(thisCls, (*env).GetStaticFieldID(thisCls, "MDB_PAGE_FULL", "I"), (jint) MDB_PAGE_FULL);
    (*env).SetStaticIntField(thisCls, (*env).GetStaticFieldID(thisCls, "MDB_MAP_RESIZED", "I"), (jint) MDB_MAP_RESIZED);
    (*env).SetStaticIntField(thisCls, (*env).GetStaticFieldID(thisCls, "MDB_INCOMPATIBLE", "I"), (jint) MDB_INCOMPATIBLE);
    (*env).SetStaticIntField(thisCls, (*env).GetStaticFieldID(thisCls, "MDB_BAD_RSLOT", "I"), (jint) MDB_BAD_RSLOT);
    (*env).SetStaticIntField(thisCls, (*env).GetStaticFieldID(thisCls, "MDB_BAD_TXN", "I"), (jint) MDB_BAD_TXN);
    (*env).SetStaticIntField(thisCls, (*env).GetStaticFieldID(thisCls, "MDB_BAD_VALSIZE", "I"), (jint) MDB_BAD_VALSIZE);
    (*env).SetStaticIntField(thisCls, (*env).GetStaticFieldID(thisCls, "MDB_BAD_DBI", "I"), (jint) MDB_BAD_DBI);
}

extern "C" JNIEXPORT jlong JNICALL
Java_me_xizzhu_android_kvs_lmdb_Jni_createEnv(JNIEnv *env, jobject thisObj) {
    MDB_env *mdb_env;
    int rc = mdb_env_create(&mdb_env);
    if (rc != MDB_SUCCESS) {
        throwLmdbException(env, rc);
    }
    return (jlong) mdb_env;
}

extern "C" JNIEXPORT void JNICALL
Java_me_xizzhu_android_kvs_lmdb_Jni_closeEnv(JNIEnv *env, jobject thisObj, jlong mdb_env) {
    mdb_env_close((MDB_env *) mdb_env);
}

extern "C" JNIEXPORT void JNICALL
Java_me_xizzhu_android_kvs_lmdb_Jni_openEnv(JNIEnv *env, jobject thisObj, jlong mdb_env, jstring path, jint flags, jint mode) {
    const char *pathPtr = (*env).GetStringUTFChars(path, nullptr);
    int rc = mdb_env_open((MDB_env *) mdb_env, pathPtr, (unsigned int) flags, (mdb_mode_t) mode);
    (*env).ReleaseStringUTFChars(path, pathPtr);
    if (rc != MDB_SUCCESS) {
        throwLmdbException(env, rc);
    }
}

extern "C" JNIEXPORT jlong JNICALL
Java_me_xizzhu_android_kvs_lmdb_Jni_beginTransaction(JNIEnv *env, jobject thisObj, jlong mdb_env, jboolean readOnly) {
    MDB_txn *mdb_txn;
    int rc = mdb_txn_begin((MDB_env *) mdb_env, nullptr, readOnly ? MDB_RDONLY : 0, &mdb_txn);
    if (rc != MDB_SUCCESS) {
        throwLmdbException(env, rc);
    }
    return (jlong) mdb_txn;
}

extern "C" JNIEXPORT void JNICALL
Java_me_xizzhu_android_kvs_lmdb_Jni_commitTransaction(JNIEnv *env, jobject thisObj, jlong mdb_txn) {
    int rc = mdb_txn_commit((MDB_txn *) mdb_txn);
    if (rc != MDB_SUCCESS) {
        throwLmdbException(env, rc);
    }
}

extern "C" JNIEXPORT void JNICALL
Java_me_xizzhu_android_kvs_lmdb_Jni_abortTransaction(JNIEnv *env, jobject thisObj, jlong mdb_txn) {
    mdb_txn_abort((MDB_txn *) mdb_txn);
}

extern "C" JNIEXPORT jlong JNICALL
Java_me_xizzhu_android_kvs_lmdb_Jni_openDatabase(JNIEnv *env, jobject thisObj, jlong mdb_txn, jboolean createIfNotExists) {
    MDB_dbi dbi;
    int rc = mdb_dbi_open((MDB_txn *) mdb_txn, nullptr, createIfNotExists ? MDB_CREATE : 0, &dbi);
    if (rc != MDB_SUCCESS) {
        throwLmdbException(env, rc);
    }
    return (jlong) dbi;
}

extern "C" JNIEXPORT void JNICALL
Java_me_xizzhu_android_kvs_lmdb_Jni_closeDatabase(JNIEnv *env, jobject thisObj, jlong mdb_env, jlong mdb_dbi) {
    mdb_dbi_close((MDB_env *) mdb_env, (MDB_dbi) mdb_dbi);
}

extern "C" JNIEXPORT jbyteArray JNICALL
Java_me_xizzhu_android_kvs_lmdb_Jni_getData(JNIEnv *env, jobject thisObj, jlong mdb_txn, jlong mdb_dbi, jbyteArray key) {
    MDB_val mdb_key, mdb_value;
    mdb_key.mv_size = (*env).GetArrayLength(key);
    mdb_key.mv_data = malloc(mdb_key.mv_size);
    (*env).GetByteArrayRegion(key, 0, mdb_key.mv_size, (jbyte *) mdb_key.mv_data);

    int rc = mdb_get((MDB_txn *) mdb_txn, (MDB_dbi) mdb_dbi, &mdb_key, &mdb_value);
    free(mdb_key.mv_data);
    if (rc == MDB_NOTFOUND) {
        return nullptr;
    } else if (rc != MDB_SUCCESS) {
        throwLmdbException(env, rc);
    }

    jbyteArray result = (*env).NewByteArray(mdb_value.mv_size);
    (*env).SetByteArrayRegion(result, 0, mdb_value.mv_size, (jbyte *) mdb_value.mv_data);
    return result;
}

extern "C" JNIEXPORT void JNICALL
Java_me_xizzhu_android_kvs_lmdb_Jni_setData(JNIEnv *env, jobject thisObj, jlong mdb_txn, jlong mdb_dbi, jbyteArray key, jbyteArray value) {
    MDB_val mdb_key, mdb_value;
    mdb_key.mv_size = (*env).GetArrayLength(key);
    mdb_key.mv_data = malloc(mdb_key.mv_size);
    (*env).GetByteArrayRegion(key, 0, mdb_key.mv_size, (jbyte *) mdb_key.mv_data);
    mdb_value.mv_size = (*env).GetArrayLength(value);
    mdb_value.mv_data = malloc(mdb_value.mv_size);
    (*env).GetByteArrayRegion(value, 0, mdb_value.mv_size, (jbyte *) mdb_value.mv_data);

    int rc = mdb_put((MDB_txn *) mdb_txn, (MDB_dbi) mdb_dbi, &mdb_key, &mdb_value, 0);
    free(mdb_key.mv_data);
    free(mdb_value.mv_data);
    if (rc != MDB_SUCCESS) {
        throwLmdbException(env, rc);
    }
}
