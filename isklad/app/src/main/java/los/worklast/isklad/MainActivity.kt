package los.worklast.isklad

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.add
import androidx.fragment.app.commit
import los.worklast.isklad.fragments.MainFragment
import los.worklast.isklad.fragments.SeeAllFragment
import los.worklast.isklad.fragments.SkladChoiseFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        hideSystemUI()

            if (allPermissionsGranted()) {

            println(" erorr no final! ")
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }


//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, 0, systemBars.right, 0)
//            insets
//        }


        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<MainFragment>(R.id.main_fragment_container)
        }


        val but1 = findViewById<Button>(R.id.but_photo_main).setOnClickListener {

            println(" but 1 press ")



            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<SkladChoiseFragment>(R.id.main_fragment_container)
            }


            val but2 = findViewById<Button>(R.id.but_see_otchet).setOnClickListener {

                println(" but 1 press ")


                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    add<SeeAllFragment>(R.id.main_fragment_container)
                }


            }
        }
    }


        fun hideSystemUI() {
         WindowCompat.setDecorFitsSystemWindows(window, false)
         WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

       private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }


     companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION )
//        @JvmStatic  var sumValues: Int = 0
        private const val REQUEST_CODE_PERMISSIONS = 17
//        private const val  raz = "\n"
    }

}