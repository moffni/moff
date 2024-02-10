package los.worklast.isklad.fragments



import android.Manifest
import android.app.Application
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Environment
import android.util.Base64.DEFAULT
import android.util.Base64.encodeToString
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import io.fotoapparat.Fotoapparat
import io.fotoapparat.parameter.ScaleType
import io.fotoapparat.result.BitmapPhoto
import io.fotoapparat.selector.back
import io.fotoapparat.view.CameraView
import los.worklast.isklad.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Base64
import java.util.Calendar

class CameraFrag2 : Fragment(R.layout.camera_tw) {


    lateinit var cameraView : CameraView
    lateinit var fotoAppa : Fotoapparat
    lateinit var ok_addbutB : Button
    lateinit var text_input_final : TextInputEditText


    var finalSklad = 0
    var finalOperations = 0
    var BitmapFinal : Bitmap? = null

    private var locationManager : LocationManager? = null

    var write = true



    fun ConvertInName (num : Int ):  String {
           val nameDocuments = when( num ){
            1 -> "Entering"
            2 -> "Exit"
            3 -> "Waiting"
            else -> { "nothing to create"}
        }
        return nameDocuments
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


         cameraView = view.findViewById(R.id.camera_view)
         finalSklad = requireArguments().getInt("sklad")
         finalOperations = requireArguments().getInt("final")

        if(finalSklad > 3 || finalSklad < 0){
            write = false
        }

       ok_addbutB = view.findViewById(R.id.ok_addbut)
       text_input_final = view.findViewById(R.id.text_input_a)

       val db = FirebaseFirestore.getInstance()



        val nameDocuments = ConvertInName(finalOperations)
//        val nameDocuments = when( finalOperations){
//            1 -> "Entering"
//            2 -> "Exit"
//            3 -> "Waiting"
//            else -> { "nothing to create"}
//        }


        // println(" my final values = $finalInt")

        locationManager = requireActivity().getSystemService(LOCATION_SERVICE) as LocationManager?

          try {

            locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)

          } catch(ex: SecurityException) {
            Log.d("myTag", "Security Exception, no location available")
        }



        this.context?.let {

          fotoAppa =  Fotoapparat(
                context = it,
                view = cameraView,
                scaleType = ScaleType.CenterCrop,
                lensPosition = back(),
                cameraErrorCallback = { error -> }
            )

            }


         ok_addbutB.setOnClickListener {



//             try {
//                val imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)
//                imm.hideSoftInputFromWindow(requireActivity().currentFocus!!.windowToken, 0)
//            } catch ( e : Exception) {
//                // TODO: handle exception
//            }


             val a = locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)


           val sendBitmap = drawTextToBitmap(requireContext(), BitmapFinal!!,"${text_input_final.text} \n ${a!!.latitude} \n ${a.longitude}")

             // save file to system



             println(" my path to file = ${requireContext().filesDir.absoluteFile}" )


             val formatter = SimpleDateFormat("dd.MM.yyyy hh:mm:ss")
             val calendar = Calendar.getInstance().timeInMillis
             val timeToSend = formatter.format(calendar).toString()

             val Dir = requireContext().filesDir.absoluteFile.toString() + "newDay"
             val dir2 = Environment.getExternalStorageDirectory().toString() + "/data" + "/${finalSklad}" + "/${ConvertInName(finalOperations)}"

             val df : File = File(Environment.getExternalStorageDirectory().toString() + "/data" )

             println(" my df = ${df}")
             if(!df.isDirectory){
                 df.mkdir()
             }





              val formatter1 = SimpleDateFormat("dd.MM")
             val calendar1 = Calendar.getInstance().timeInMillis
             val timeToWrite = formatter1.format(calendar1).toString()

              println(" my write data = ${timeToWrite}")


             val a11 : File =  File (requireContext().filesDir.absoluteFile.toString() + "/${timeToWrite}")
             if(!a11.isDirectory){
                 println(" no exists a11 ")
                 a11.mkdir()
             }

             val a12  = File (a11.toString() + "/${finalSklad}")

              if(!a12.isDirectory){
                  println(" no exists a12 ")
                 a12.mkdir()
             }


                val a13  = File (a12.toString() + "/${ConvertInName(finalOperations)}")

              if(!a13.isDirectory){
                  println(" no exists a13 ")
                 a13.mkdir()
             }


             val a14 = File (a13.toString() + "/${timeToSend}.png")

             println(" my final file PNG = $a14")


             val fOut = FileOutputStream(a14)
             sendBitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut)


             ///data/user/0/los.worklast.isklad/filesnewDay
            // a /data/user/0/los.worklast.isklad/filesnewDay and /storage/emulated/0
             println(" a ${Dir} and ${dir2}")

//             val ne = Dir + "NewDay"
//             ne.mkdir()
//
//             println(" my dir = ${ne}")


            // println(" my enterings text == ${text_input_final.text} ")

            val stringsOurBitmap = encodeImage(sendBitmap)




             val user = hashMapOf(
            "photo" to stringsOurBitmap,
            "time" to formatter.format(calendar),
            "latitude" to a!!.latitude,
            "longitude" to a.longitude)

               db.collection(finalSklad.toString()).document("$nameDocuments $timeToSend")
            .set(user)
            .addOnSuccessListener { documentReference ->
                println("DocumentSnapshot added with ID: ${documentReference.toString()}")
            }
            .addOnFailureListener { e ->
                println("Error adding document $e")
            }

            text_input_final.setText("")
            text_input_final.setVisibility(View.INVISIBLE)
            ok_addbutB.visibility = View.INVISIBLE


            }



             val takePick = view.findViewById<ImageButton>(R.id.camera_take_pick).setOnClickListener {


            val photoResult = fotoAppa.takePicture()

            //  ok_addbutB.setOnClickListener {

            photoResult.toBitmap().whenAvailable {

                // val stringsOurBitmap = encodeImage(it!!.bitmap)
                BitmapFinal = it!!.bitmap
            }


            ok_addbutB.visibility = View.VISIBLE
            text_input_final.visibility = View.VISIBLE

        }


        }









    private fun encodeImage(bitmap: Bitmap): String {
    val previewWidth = 150
    val previewHeight = bitmap.height * previewWidth / bitmap.width
    val previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false)
    val byteArrayOutputStream = ByteArrayOutputStream()
    previewBitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream)
    val bytes = byteArrayOutputStream.toByteArray()
    return encodeToString(bytes, DEFAULT)
}


    private val locationListener: LocationListener = object : LocationListener {
    override fun onLocationChanged(location: Location) {
        //thetext.text = ("" + location.longitude + ":" + location.latitude)
        println(" my location == ${location.longitude} and ${location.latitude}")
    }
    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    override fun onProviderEnabled(provider: String) {}
    override fun onProviderDisabled(provider: String) {}
}



    override fun onStart() {
    super.onStart()
    fotoAppa.start()
}

override fun onStop() {
    super.onStop()
    fotoAppa.stop()
}


}