package com.pavlov.MyShadowGallery.util

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.Keep
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject

// ------------------------------------------------------------------------------------ гетеры и сетеры
@Keep
class APKM @Inject constructor(
    @ApplicationContext private val context: Context
) {
    // Переименовали функцию, чтобы избежать конфликта имён
    val sharedPreferences: SharedPreferences = retrieveSharedPreferences()

    private fun retrieveSharedPreferences() =
        context.getSharedPreferences(APK.PREFS_NAME, Context.MODE_PRIVATE)

    // ------------------------------------------------------------------------ Boolean гетеры и сетеры
    fun putBoolean(key: String, isChecked: Boolean) {
        sharedPreferences.edit {
            putBoolean(key, isChecked).apply()
        }
    }

    fun getBoolean(key: String, default: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, default)
    }

    // ---------------------------------------------------------------------------- Int гетеры и сетеры
    fun putInt(key: String, value: Int) {
        sharedPreferences.edit {
            putInt(key, value).apply()
        }
    }

    fun getInt(key: String): Int {
        return sharedPreferences.getInt(key, 0)
    }

    fun delFromSP(key: String) {
        sharedPreferences.edit {
            remove(key).apply()
        }
    }

    fun clearStorage(context: Context) {
        val filesDir = context.filesDir
        filesDir.listFiles()?.forEach { it.deleteRecursively() }

        val previewsDir = context.getDir("originalAndPreviews", Context.MODE_PRIVATE)
        previewsDir.listFiles()?.forEach { it.deleteRecursively() }
    }

    fun clearPreferences(context: Context) {
        val sharedPreferences = context.getSharedPreferences(APK.PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
    }

    fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(APK.PREFS_NAME, Context.MODE_PRIVATE)
    }

    //APKM(context).delFromSP(APK.DEFAULT_KEY)

    // ------------------------------------------------------------------------- String гетеры и сетеры
    fun getStringFromSharedPreferences(key: String): String {
        return sharedPreferences.getString(key, "упс...ах") ?: "упс...ах"
    }// AppPreferencesKeysMethods(context).getObjectFromSharedPreferences(AppPreferencesKeys.KEY_HISTORY_LIST)

    fun saveStringToSharedPreferences(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }// AppPreferencesKeysMethods(context).saveStringToSharedPreferences(AppPreferencesKeys.KEY_HISTORY_LIST, jsonString)

    fun delStringFromSharedPreferences(key: String) {
        val editor = sharedPreferences.edit()
        editor.remove(key)
        editor.apply()
    }// appPreferencesMethods.delStringFromSharedPreferences(AppPreferencesKeys.KEY_HISTORY_LIST)

// удаляется объект как простая стринга

    // -------------------------------------------------------------- encrypted String гетеры и сетеры
    fun getMastersSecret(key: String): String {
        val masterKeyAlias = MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val encryptedSharedPreferences: SharedPreferences =
            EncryptedSharedPreferences.create(
                context,
                APK.MY_SECRETS_PREFS_NAME,
                masterKeyAlias,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        return encryptedSharedPreferences.getString(key, "") ?: ""
    }

    fun saveMastersSecret(secret: String, key: String) {
        val masterKeyAlias = MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val encryptedSharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
            context,
            APK.MY_SECRETS_PREFS_NAME,
            masterKeyAlias,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        encryptedSharedPreferences.edit {
            putString(key, secret).apply()
        }
    } //    AppPreferencesKeysMethods(context = this).saveMastersSecret(keyValue, AppPreferencesKeys.KEY_BIG_SECRET)

    fun delMastersSecret(key: String) {
        val masterKeyAlias = MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val encryptedSharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
            context,
            APK.MY_SECRETS_PREFS_NAME,
            masterKeyAlias,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        encryptedSharedPreferences.edit {
            remove(key)
            apply()
        }
    } //    AppPreferencesKeysMethods(context = this).delMastersSecret(AppPreferencesKeys.KEY_BIG_SECRET)

    // -------------------------------------------------------------- encrypted Int гетеры и сетеры
    fun getCounter(key: String, default: Int): Int {
        val masterKeyAlias = MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
            context,
            APK.MY_SECRETS_PREFS_NAME,
            masterKeyAlias,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        return sharedPreferences.getInt(key, default)
    } // AppPreferencesKeysMethods(context = this).getCounter()

    fun saveCounter(key: String, counter: Int) {
        val masterKeyAlias = MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
            context,
            APK.MY_SECRETS_PREFS_NAME,
            masterKeyAlias,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        sharedPreferences.edit {
            putInt(key, counter).apply()
        }
    } //    AppPreferencesKeysMethods(context = this).saveCounter(counter)

    fun countBigSecrets(): Int {
        var count = 3
        val secret1 = APKM(context).getMastersSecret(APK.KEY_BIG_SECRET1)
        val secret2 = APKM(context).getMastersSecret(APK.KEY_BIG_SECRET2)
        val secret3 = APKM(context).getMastersSecret(APK.KEY_BIG_SECRET3)
        if (secret1.isNullOrBlank()) {
            count -= 1
        }
        if (secret2.isNullOrBlank()) {
            count -= 1
        }
        if (secret3.isNullOrBlank()) {
            count -= 1
        }
        return count
    }

    fun generateUniqueKeyName(): String {
        val name1 = APKM(context).getMastersSecret(APK.KEY_BIG_SECRET_NAME1)
        val name2 = APKM(context).getMastersSecret(APK.KEY_BIG_SECRET_NAME2)
        val name3 = APKM(context).getMastersSecret(APK.KEY_BIG_SECRET_NAME3)
        val existingNames = setOf(name1, name2, name3)

        var index = 1
        var newKeyName = "key $index"

        while (existingNames.contains(newKeyName)) {
            index++
            newKeyName = "key $index"
        }

        return newKeyName
    }

    fun getDefauldKey(): String {
        val defaultKey: Int = APKM(context).getInt(APK.DEFAULT_KEY)
        if (defaultKey == 1) {
            return APKM(context).getMastersSecret(APK.KEY_BIG_SECRET1)
        } else if (defaultKey == 2) {
            return APKM(context).getMastersSecret(APK.KEY_BIG_SECRET2)
        } else if (defaultKey == 3) {
            return APKM(context).getMastersSecret(APK.KEY_BIG_SECRET3)
        } else {
            return ""
        }
    }

    fun getDefauldKeyName(): String {
        val defaultKey: Int = APKM(context).getInt(APK.DEFAULT_KEY)
        if (defaultKey == 1) {
            return APKM(context).getMastersSecret(APK.KEY_BIG_SECRET_NAME1)
        } else if (defaultKey == 2) {
            return APKM(context).getMastersSecret(APK.KEY_BIG_SECRET_NAME2)
        } else if (defaultKey == 3) {
            return APKM(context).getMastersSecret(APK.KEY_BIG_SECRET_NAME3)
        } else {
            return ""
        }
    }

    fun getKeyByNumber(int: Int): String {
        if (int == 1) {
            return APKM(context).getMastersSecret(APK.KEY_BIG_SECRET1)
        } else if (int == 2) {
            return APKM(context).getMastersSecret(APK.KEY_BIG_SECRET2)
        } else if (int == 3) {
            return APKM(context).getMastersSecret(APK.KEY_BIG_SECRET3)
        } else {
            return ""
        }
    }

    fun clearEncryptedPreferences(context: Context) {
        val masterKeyAlias = MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val encryptedSharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
            context,
            APK.MY_SECRETS_PREFS_NAME,
            masterKeyAlias,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        // Удаляем все данные
        encryptedSharedPreferences.edit().clear().apply()
    }
}


