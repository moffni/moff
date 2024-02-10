package los.testingslos.testblue

import android.app.Application

class Applicat : Application() {

    override fun onCreate() {
        super.onCreate()
     //   BluetoothHandler.initialize(this.applicationContext)
         BluetoothHandler.initialize(this.applicationContext)
    }
}