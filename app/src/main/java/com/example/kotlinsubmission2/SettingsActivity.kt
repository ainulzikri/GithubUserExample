package com.example.kotlinsubmission2

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.example.kotlinsubmission2.alarm.AlarmNotification

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat(),
        SharedPreferences.OnSharedPreferenceChangeListener {
        private lateinit var notificationKey : String
        private lateinit var languageKey : String

        private lateinit var notificationPreference : SwitchPreference
        private lateinit var languagePreference : Preference

        private lateinit var alarmNotification: AlarmNotification
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            init()
        }

        private fun init(){
            alarmNotification = AlarmNotification()
            notificationKey = resources.getString(R.string.notification_key)
            languageKey = resources.getString(R.string.language_key)
            notificationPreference = findPreference<SwitchPreference>(notificationKey) as SwitchPreference
            languagePreference = findPreference<Preference>(languageKey) as Preference
        }

        override fun onResume() {
            preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
            super.onResume()
        }

        override fun onDestroyOptionsMenu() {
            preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
            super.onDestroyOptionsMenu()
        }

        override fun onSharedPreferenceChanged(
            sharedPreferences: SharedPreferences?,
            key: String?
        ) {
            when (key) {
                notificationKey -> {
                    val isOn = sharedPreferences?.getBoolean(notificationKey, false)

                        if(isOn != null){
                            if(isOn){
                                context?.let {
                                    alarmNotification.setAlarm(it)
                                }
                            }else{
                                context?.let {
                                    alarmNotification.cancelAlarm(it)
                                }
                            }
                        }
                }
            }
        }

        override fun onPreferenceTreeClick(preference: Preference?): Boolean {

            when(preference?.key){
                languageKey -> {
                    val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                    startActivity(intent)
                }
            }
            return super.onPreferenceTreeClick(preference)
        }
    }


}