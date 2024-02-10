package los.worklast.isklad

import android.app.Application
import com.google.firebase.FirebaseApp

class APPlicationsSta : Application() {

    override fun onCreate() {
        super.onCreate()

         FirebaseApp.initializeApp(applicationContext)


    }
}