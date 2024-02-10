package los.worklast.isklad.fragments

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.rmartinper.filepicker.controller.DialogSelectionListener
import com.rmartinper.filepicker.model.DialogConfigs
import com.rmartinper.filepicker.model.DialogProperties
import com.rmartinper.filepicker.view.FilePickerDialog
import los.worklast.isklad.R
import java.io.File

class SeeAllFragment : Fragment(R.layout.see_all_fragment) {


      override fun onViewCreated(view: View, savedInstanceState: Bundle?) {



          val image_viewMain = view.findViewById<ImageView>(R.id.image_view_main)

              val properties =  DialogProperties()


          properties.selectionMode = DialogConfigs.MULTI_MODE
          properties.selectionType = DialogConfigs.FILE_AND_DIR_SELECT
          properties.root = (File(requireContext().filesDir.absoluteFile.toString()))
          properties.errorDir = File(DialogConfigs.DEFAULT_DIR)
          properties.offset =  File(DialogConfigs.DEFAULT_DIR)
          //properties.extensions = null or new String[]{".ext1", ".ext2"};
          properties.setHiddenFilesShown(true)



           val dialog = FilePickerDialog(requireActivity(), properties)
           dialog.setTitle("What your work")


           dialog.setDialogSelectionListener(object : DialogSelectionListener {

               override fun onSelectedFilePaths(p0: Array<out String>?) {

                   println(" my selected paths = ${p0!![0]}")
                   image_viewMain.setImageURI(Uri.fromFile(File(p0[0])))

               }
           })




          dialog.show()

    }


}