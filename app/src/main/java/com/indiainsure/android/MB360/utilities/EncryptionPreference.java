package com.indiainsure.android.MB360.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import androidx.security.crypto.MasterKey;

import com.indiainsure.android.MB360.BuildConfig;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class EncryptionPreference {


    private SharedPreferences pref;
    private SharedPreferences.Editor editor;


    private KeyGenParameterSpec createKeyGenParameterSpec() {
        return new KeyGenParameterSpec.Builder(
                BuildConfig.MASTER_KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(Integer.parseInt(BuildConfig.KEY_SIZE))
                .build();
    }

    private MasterKey getMasterKey(Context context, KeyGenParameterSpec keyGenParameterSpec) throws GeneralSecurityException, IOException {
        return new MasterKey.Builder(context, BuildConfig.MASTER_KEY_ALIAS)
                .setKeyGenParameterSpec(keyGenParameterSpec)
                .build();
    }

    public EncryptionPreference(Context context) {
        try {
            pref = androidx.security.crypto.EncryptedSharedPreferences.create(
                    context,
                    BuildConfig.MASTER_KEY_ALIAS,
                    getMasterKey(context, createKeyGenParameterSpec()),
                    androidx.security.crypto.EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    androidx.security.crypto.EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
            editor = pref.edit();
            editor.apply();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }

    }

    public String getEncryptedDataString(String KEY) {
        return pref.getString(KEY, null);
    }

    public String getEncryptedDataToken(String KEY) {
        return pref.getString(KEY, "");
    }

    public Boolean getEncryptedDataBool(String KEY) {
        return pref.getBoolean(KEY, true);
    }

    public void setEncryptedBoolString(String KEY, boolean value) {
        editor.putBoolean(KEY, value);
        editor.apply();
        editor.commit();
    }

    public void setEncryptedDataString(String KEY, String value) {
        editor.putString(KEY, value);
        editor.apply();
        editor.commit();
    }




}
